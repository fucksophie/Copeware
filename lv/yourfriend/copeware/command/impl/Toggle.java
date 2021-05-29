package lv.yourfriend.copeware.command.impl;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.command.Command;
import lv.yourfriend.copeware.modules.Module;

public class Toggle extends Command {

	public Toggle() {
		super("Toggle", "Toggles a module by name.", "toggle <name>", "t");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		if(args.length > 0) {
			String moduleName = args[0];
			
			if(Client.getModuleByName(moduleName) != null) {
				Module module = Client.getModuleByName(moduleName);
				module.toggle();
				Client.chat((module.isEnabled() ? "Enabled" : "Disabled") + " " + module.name + "!");
			} else {
				Client.chat("Module " + moduleName + " not found.");
			}
		}
	}
}
