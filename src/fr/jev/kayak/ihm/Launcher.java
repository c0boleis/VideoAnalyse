package fr.jev.kayak.ihm;

import fr.jev.kayak.controlers.ConfigurationController;

public class Launcher {

	public static void main(String[] args) {
		if(args.length!=1) {
			System.err.println("il ny a pas de video en argument.");
			System.exit(1);
		}
		ConfigurationController.file = args[0];
		MainFrame.get().setVisible(true);
	}

}
