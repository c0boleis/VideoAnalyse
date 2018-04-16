package fr.jev.kayak.ihm;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JPanel;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import fr.jev.kayak.controlers.ConfigurationController;
import uk.co.caprica.vlcj.binding.LibVlc;
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
	public EmbeddedMediaPlayer getEmbeddedMediaPlayer() {
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
		}else {
			getEmbeddedMediaPlayer().pause();
			isPlay = false;
		}
		
	}

}
