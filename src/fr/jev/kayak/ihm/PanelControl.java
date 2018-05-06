package fr.jev.kayak.ihm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PanelControl extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6576443325666724652L;

	private JButton buttonPlay;
	
	private JLabel labelTime;
	
	private JSlider sliderTime;

	public PanelControl() {
		this.setLayout(new BorderLayout());
		this.add(getButtonPlay(), BorderLayout.CENTER);

		this.add(getLabelTime(), BorderLayout.EAST);

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
			sliderTime.setMaximum(100);
			sliderTime.setValue(0);
			sliderTime.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					//TODO il reste un erreur a coriger
					int value = sliderTime.getValue();
					long time = value*1000l;
					MainFrame.get().getPanelVideo().setTime(time);
					MainFrame.get().getPanelVideo().play();
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					MainFrame.get().getPanelVideo().pause();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {}
			});
		}
		return sliderTime;
	}
	
	public void setSliderMax(int nbrSeconds) {
		getSliderTime().setMaximum(nbrSeconds);
		getSliderTime().setValue(0);
	}
	
	public void setSliderValue(int time) {
		getSliderTime().setValue(time);
	}

}
