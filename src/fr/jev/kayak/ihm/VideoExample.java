package fr.jev.kayak.ihm;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;
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


public class VideoExample {
	
//	private MediaP

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setLocation(100, 100);
		f.setSize(500, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Canvas c = new Canvas();
		c.setBackground(Color.black);
		JPanel p = new JPanel();
		
		p.setLayout(new BorderLayout());
		p.add(c,BorderLayout.CENTER);
		f.add(p);
		f.setVisible(true);
	
		if(ConfigurationController.isWindows()) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),"C:/Program Files (x86)/VideoLAN/VLC");
		}
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);
		MediaPlayerFactory mpf = new MediaPlayerFactory();
		EmbeddedMediaPlayer emp = null;
		if(ConfigurationController.isWindows()) {
			emp = mpf.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(f));
		}else {
			emp = mpf.newEmbeddedMediaPlayer(new XFullScreenStrategy(f));
		}
		
		emp.setVideoSurface(mpf.newVideoSurface(c));
//		emp.toggleFullScreen();
		String file = args[0];
		
		emp.prepareMedia(file);
		emp.play();
	}

}
