package com.nnarain.fps;

import java.io.File;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-20
 */
public class Setup {
	
	private boolean setupRequired;
	
	private File configFile;
	
	private static final String CONFIG = "controller_map.config";
	
	/**
	 * 
	 */
	public Setup() {
		
		configFile = new File(CONFIG);
		
		setupRequired = !configFile.exists();
		
	}

	/**
	 * @return if setup is required
	 */
	public boolean isSetupRequired() {
		return setupRequired;
	}
	
}
