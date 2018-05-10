package fr.jev.kayak.ihm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedesktop.gstreamer.elements.PlayBin;

import sun.applet.Main;

public class PanelControl extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6576443325666724652L;

	private JButton buttonPlay;
	
	private JLabel labelTime;
	
	private JSlider sliderTime;
	
	private JButton buttonIncr;
	
	private JButton buttonDec;

	public PanelControl() {
		this.setLayout(new BorderLayout());
		this.add(getButtonPlay(), BorderLayout.CENTER);

		this.add(getButtonIncr(), BorderLayout.EAST);
		
		this.add(getButtonDec(), BorderLayout.WEST);

		this.add(getSliderTime(), BorderLayout.SOUTH);
	}

	/**
	 * @return the buttonPlay
	 */
	public JButton getButtonPlay() {
		if(buttonPlay == null) {
			buttonPlay = new JButton();
			buttonPlay.setText("Play/pause");
			buttonPlay.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					MainFrame.get().getPanelVideo().playPause();
				}
			});
		}
		return buttonPlay;
	}

	/**
	 * @return the labelTime
	 */
	public JLabel getLabelTime() {
		if(labelTime == null) {
			labelTime = new JLabel();
			labelTime.setText("0.0");
		}
		return labelTime;
	}

	/**
	 * @return the sliderTime
	 */
	private JSlider getSliderTime() {
		if(sliderTime == null) {
			sliderTime = new JSlider();
			sliderTime.setMinimum(0);
			sliderTime.setMaximum(1000);
			sliderTime.setValue(0);
			sliderTime.addChangeListener(e -> {
				if (sliderTime.getValueIsAdjusting()) {
					long dur = MainFrame.get().getPanelVideo().getTotalTime();
					if (dur > 0) {
						System.out.println(sliderTime.getValue());
						double relPos = sliderTime.getValue() / 1000.0;
						MainFrame.get().getPanelVideo().setCurrentTime((long) (relPos * dur));
					} 
				}
			});
		}
		return sliderTime;
	}
	
	public void setSliderValue(int time) {
		if(!getSliderTime().getValueIsAdjusting()) {
			getSliderTime().setValue(time);
		}
	}

	public JButton getButtonIncr() {
		if(buttonIncr == null) {
			buttonIncr = new JButton();
			buttonIncr.setText(">>");
			buttonIncr.addActionListener(e -> {
				MainFrame.get().getPanelVideo().incrRate();
			});
		}
		return buttonIncr;
	}

	public JButton getButtonDec() {
		if(buttonDec == null) {
			buttonDec = new JButton();
			buttonDec.setText("<<");
			buttonDec.addActionListener(e -> {
				MainFrame.get().getPanelVideo().decRate();;
			});
		}
		return buttonDec;
	}

}
