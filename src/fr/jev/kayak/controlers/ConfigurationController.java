package fr.jev.kayak.controlers;

public class ConfigurationController {
	
	public static final String WINDOWS_OS = "windows";
	
	public static String file = null;
	
	public static boolean isWindows() {
		String os = System.getProperty("os.name");
		System.out.println("OS: "+os);
		if(os != null) {
			os = os.trim().toLowerCase();
			if(os.contains(ConfigurationController.WINDOWS_OS)) {
				System.out.println("Config for windows.");
				return true;
			}
		}
		return false;
	}

}
