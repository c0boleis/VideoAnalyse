package fr.jev.kayak.ihm;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.util.Date;

import javax.swing.JPanel;

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

	private EmbeddedMediaPlayer embeddedMediaPlayer;

	private Canvas canvas;

	private MediaPlayerFactory mediaPlayerFactory;

	private boolean isPlay = false;

	private long totalTime = 0l;

	private Thread threadTime;

	public PanelVideo() {
		if(ConfigurationController.isWindows()) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),"C:/Program Files (x86)/VideoLAN/VLC");
		}
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);
		this.setLayout(new BorderLayout());
		this.add(getCanvas(), BorderLayout.CENTER);
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
			MainFrame.get().getPanelControl().setSliderMax((int) (totalTime/1000l));
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
			getEmbeddedMediaPlayer().play();
			isPlay = true;
			startThreadTime();
		}else {
			getEmbeddedMediaPlayer().pause();
			isPlay = false;
			stopThreadTime();
		}
	}

	public void play() {
		getEmbeddedMediaPlayer().play();
		isPlay = true;
		startThreadTime();
	}

	public void pause() {
		getEmbeddedMediaPlayer().pause();
		isPlay = false;
		stopThreadTime();
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
						long time = getEmbeddedMediaPlayer().getTime();
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
						MainFrame.get().getPanelControl().setSliderValue((int)(time/1000l));

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

	public void setTime(long time) {
		this.getEmbeddedMediaPlayer().setTime(time);
	}
	
	public void close() {
		if(this.embeddedMediaPlayer!= null) {
			pause();
			this.embeddedMediaPlayer.release();
		}
	}

}
