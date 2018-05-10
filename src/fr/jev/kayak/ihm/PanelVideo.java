package fr.jev.kayak.ihm;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.Event;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.SeekFlags;
import org.freedesktop.gstreamer.SeekType;
import org.freedesktop.gstreamer.elements.PlayBin;
import org.freedesktop.gstreamer.event.LatencyEvent;
import org.freedesktop.gstreamer.event.SeekEvent;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import fr.jev.kayak.controlers.ConfigurationController;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.x.XFullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class PanelVideo extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6238373404940413630L;

	/*
	 * for vlc
	 */
	private EmbeddedMediaPlayer embeddedMediaPlayer;

	private Canvas canvas;

	private MediaPlayerFactory mediaPlayerFactory;

	/*
	 * for gstreamer
	 */
	private SimpleVideoComponent simpleVideoComponent;

	private PlayBin playBin;

	private double rate = 1.0;

	/*
	 * common
	 */
	private boolean isPlay = false;

	private long totalTime = 0l;

	private Thread threadTime;

	public PanelVideo() {
		this.setLayout(new BorderLayout());
		if(ConfigurationController.isVlcLib()) {
			if(ConfigurationController.isWindows()) {
				NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),"C:/Program Files (x86)/VideoLAN/VLC");
			}
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);
			this.add(getCanvas(), BorderLayout.CENTER);
		}else if(ConfigurationController.isGstreamerLib()) {
			//getPlaybin to init gstreamer
			getPlayBin();
			this.add(getSimpleVideoComponent(), BorderLayout.CENTER);
		}else {
			System.err.println("[FATAL] no movie liv init.");
			System.exit(1);
		}

	}

	/**
	 * @return the embeddedMediaPlayer
	 */
	private EmbeddedMediaPlayer getEmbeddedMediaPlayer() {
		if(embeddedMediaPlayer == null) {
			if(ConfigurationController.isWindows()) {
				embeddedMediaPlayer = getMediaPlayerFactory().
						newEmbeddedMediaPlayer(new Win32FullScreenStrategy(MainFrame.get()));
			}else {
				embeddedMediaPlayer = getMediaPlayerFactory().
						newEmbeddedMediaPlayer(new XFullScreenStrategy(MainFrame.get()));
			}
			embeddedMediaPlayer.setVideoSurface(
					getMediaPlayerFactory().newVideoSurface(getCanvas()));
			embeddedMediaPlayer.prepareMedia(ConfigurationController.file);

			embeddedMediaPlayer.parseMedia();
			MediaMeta mediaMetaData = embeddedMediaPlayer.getMediaMeta();

			totalTime = mediaMetaData.getLength();
		}
		return embeddedMediaPlayer;
	}

	/**
	 * @return the canvas
	 */
	private Canvas getCanvas() {
		if(canvas == null) {
			canvas = new Canvas();
			canvas.setBackground(Color.black);
		}
		return canvas;
	}

	/**
	 * @return the mpf
	 */
	private MediaPlayerFactory getMediaPlayerFactory() {
		if(mediaPlayerFactory == null) {
			mediaPlayerFactory = new MediaPlayerFactory();
		}
		return mediaPlayerFactory;
	}

	public void playPause() {
		if(!isPlay) {
			play();
		}else {
			pause();
		}
	}

	public void play() {
		startThreadTime();
		if(ConfigurationController.isVlcLib()) {
			getEmbeddedMediaPlayer().play();
			isPlay = true;

		}else if(ConfigurationController.isGstreamerLib()) {
			getPlayBin().setBaseTime(ClockTime.fromMillis(1000));
			Event evt = new SeekEvent(16.0, Format.DEFAULT,
					SeekFlags.FLUSH | SeekFlags.ACCURATE,
					SeekType.SET, 0, SeekType.NONE, 0);
			getPlayBin().play();
			getPlayBin().sendEvent(evt);
			totalTime = getPlayBin().queryDuration(TimeUnit.NANOSECONDS);
			isPlay = true;
		}else {
			System.err.println("[FATAL] no movie liv init.");
			System.exit(1);
		}
	}

	public void pause() {
		stopThreadTime();
		if(ConfigurationController.isVlcLib()) {
			getEmbeddedMediaPlayer().pause();
			isPlay = false;
			stopThreadTime();
		}else if(ConfigurationController.isGstreamerLib()) {
			getPlayBin().pause();
			isPlay = false;
		}else {
			System.err.println("[FATAL] no movie liv init.");
			System.exit(1);
		}
	}

	private void startThreadTime() {
		if(threadTime==null) {
			threadTime = new Thread(new Runnable() {

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					System.out.println("Start Thread Time");
					while(threadTime.isAlive()) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						long time = getCurrentTime();
						totalTime = getPlayBin().queryDuration(TimeUnit.MILLISECONDS);
						long dur = getPlayBin().queryDuration(TimeUnit.NANOSECONDS);
						long pos = getPlayBin().queryPosition(TimeUnit.NANOSECONDS);
						Date dateCurrent = new Date(time);
						Date dateEnd = new Date(totalTime);
						String stEnd = String.valueOf(
								dateEnd.getMinutes()
								+":"+dateEnd.getSeconds());

						String stCurrent = String.valueOf(
								dateCurrent.getMinutes()
								+":"+dateCurrent.getSeconds());
						MainFrame.get().getPanelControl()
						.getLabelTime().setText(stCurrent+" of "+stEnd);
						if (dur > 0) {
							double relPos = (double) pos / dur;
							MainFrame.get().getPanelControl().setSliderValue((int) (relPos * 1000));
						}

					}
					System.out.println("STOP Thread Time");
				}
			});
			threadTime.setName("ThreadTime");
			threadTime.start();
		}
	}

	@SuppressWarnings("deprecation")
	private void stopThreadTime() {
		if(threadTime!=null) {
			threadTime.stop();
			threadTime = null;
		}
	}

	public void close() {
		if(this.embeddedMediaPlayer!= null) {
			pause();
			this.embeddedMediaPlayer.release();
		}
	}

	private SimpleVideoComponent getSimpleVideoComponent() {
		if(simpleVideoComponent == null) {
			simpleVideoComponent = new SimpleVideoComponent();
		}
		return simpleVideoComponent;
	}

	public PlayBin getPlayBin() {
		if(playBin == null) {
			Gst.init();
			playBin = new PlayBin("playbin");
			File file = new File(ConfigurationController.file);
			playBin.setURI(file.toURI());
			playBin.setVideoSink(getSimpleVideoComponent().getElement());
			totalTime = playBin.queryDuration(TimeUnit.MILLISECONDS);
		}
		return playBin;
	}

	public long getCurrentTime() {
		if(ConfigurationController.isVlcLib()) {
			return getEmbeddedMediaPlayer().getTime();
		}else if(ConfigurationController.isGstreamerLib()) {
			return getPlayBin().queryPosition(TimeUnit.MILLISECONDS);
		}else {
			System.err.println("[FATAL] no movie liv init.");
			System.exit(1);
		}
		return -1l;
	}

	/**
	 * 
	 * @param time en ms
	 */
	public void setCurrentTime(long time) {
		stopThreadTime();
		if(ConfigurationController.isVlcLib()) {
			this.getEmbeddedMediaPlayer().setTime(time);
		}else if(ConfigurationController.isGstreamerLib()) {
			getPlayBin().seek(time, TimeUnit.MILLISECONDS);
		}else {
			System.err.println("[FATAL] no movie liv init.");
			System.exit(1);
		}
		startThreadTime();
	}

	public long getTotalTime() {
		return this.totalTime;
	}

	public void incrRate() {
		rate *=2.0;
		long pos = getPlayBin().queryPosition(TimeUnit.NANOSECONDS);
		Event evt = new SeekEvent(rate, Format.TIME,
				SeekFlags.FLUSH | SeekFlags.ACCURATE,
				SeekType.SET, pos, SeekType.NONE, 0);
		getPlayBin().sendEvent(evt);
	}

	public void decRate() {
		rate /=2.0;
		long pos = getPlayBin().queryPosition(TimeUnit.NANOSECONDS);
		Event evt = new SeekEvent(rate, Format.TIME,
				SeekFlags.FLUSH | SeekFlags.ACCURATE,
				SeekType.SET, pos, SeekType.NONE, 0);
		getPlayBin().sendEvent(evt);
	}

}
