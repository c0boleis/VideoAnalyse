package fr.jev.kayak.ihm;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1856100179992074258L;
	
	private static final MainFrame INSTANCE = new MainFrame();
	
	private PanelVideo panelVideo;
	
	private PanelControl panelControl;
	
	private JSplitPane splitPane;
	
	private MainFrame() {
		this.setTitle("Video Analyse");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.add(getSplitPane(), BorderLayout.CENTER);
	}
	
	public static MainFrame get() {
		return INSTANCE;
	}

	/**
	 * @return the panelVideo
	 */
	public PanelVideo getPanelVideo() {
		if(panelVideo == null) {
			panelVideo = new PanelVideo();
		}
		return panelVideo;
	}

	/**
	 * @return the panelControl
	 */
	public PanelControl getPanelControl() {
		if(panelControl == null) {
			panelControl = new PanelControl();
		}
		return panelControl;
	}

	/**
	 * @return the splitPane
	 */
	private JSplitPane getSplitPane() {
		if(splitPane == null) {
			splitPane= new JSplitPane();
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setRightComponent(getPanelVideo());
			splitPane.setLeftComponent(getPanelControl());
		}
		return splitPane;
	}


}
