package lv.yourfriend.copeware.command.impl;

import java.io.File;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.command.Command;
import lv.yourfriend.copeware.modules.Module;

public class LoadConfig  extends Command {

	public LoadConfig() {
		super("loadconfig", "load config", "loadconfig <name>", "lc");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		if(args.length > 0) {
			String name = args[0];
			
			File tempConfig = new File("config_" + name + ".cope");
			
			if(!tempConfig.exists()) {
				Client.chat("Config " + name + " doesn't exist.");
			} else {
				Client.loadConfig(name, true);
				Client.chat("Attempting to load " + name + ".");
			}
		}
	}
}