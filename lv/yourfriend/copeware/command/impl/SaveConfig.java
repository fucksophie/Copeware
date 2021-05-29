package lv.yourfriend.copeware.command.impl;

import java.io.File;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.command.Command;

public class SaveConfig   extends Command {

	public SaveConfig() {
		super("saveconfig", "save config", "saveconfig <name>", "sc");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		if(args.length > 0) {
			String name = args[0];
			
			File tempConfig = new File("config_" + name + ".cope");
			
			if(tempConfig.exists()) {
				Client.chat("Config " + name + " already exists.");
			} else {
				Client.saveConfig(name, true);
				Client.chat("Attempting to save " + name + ".");
			}
		}
	}
}