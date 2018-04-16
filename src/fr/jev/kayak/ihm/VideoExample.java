package fr.jev.kayak.ihm;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.impl.SimpleLoggerConfiguration;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
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
	
//		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),"/usr/bin/vlc");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);
//		Native
		MediaPlayerFactory mpf = new MediaPlayerFactory();
		EmbeddedMediaPlayer emp = mpf.newEmbeddedMediaPlayer(new XFullScreenStrategy(f));
		emp.setVideoSurface(mpf.newVideoSurface(c));
//		emp.toggleFullScreen();
		String file = "/home/corentin/Documents/video_test/RWFS8684.MP4";
		
		emp.prepareMedia(file);
		emp.play();
	}

}
