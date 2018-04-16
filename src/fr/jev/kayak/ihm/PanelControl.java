package fr.jev.kayak.ihm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelControl extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6576443325666724652L;

	private JButton buttonPlay;

	public PanelControl() {
		this.setLayout(new BorderLayout());
		this.add(getButtonPlay(), BorderLayout.CENTER);
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

}
