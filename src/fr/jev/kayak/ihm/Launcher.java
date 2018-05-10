package fr.jev.kayak.ihm;

import fr.jev.kayak.controlers.ConfigurationController;

public class Launcher {

	public static void main(String[] args) {
		String[] nameFormats =
	            System.getProperty("gstreamer.GstNative.nameFormats", "%s-1.0").split("\\|");
		System.out.println("nameFormats");
		for(String st : nameFormats) {
			System.out.println(st);
		}
		if(args.length!=1) {
			System.err.println("il ny a pas de video en argument.");
			System.exit(1);
		}
		ConfigurationController.file = args[0];
		MainFrame.get().setVisible(true);
	}

}
