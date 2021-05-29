package lv.yourfriend.copeware.command.impl;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.command.Command;
import lv.yourfriend.copeware.modules.Module;

public class Bind extends Command {
	public Bind() {
		super("Bind", "Bind a module by name.", "bind <name> <key> | clear");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length == 2) {
			String moduleName = args[0];
			String keyName = args[1].toUpperCase();
			if(Client.getModuleByName(moduleName) != null) {
				Module module = Client.getModuleByName(moduleName);
				module.keyCode.setKeyCode(Keyboard.getKeyIndex(keyName));

				Client.chat(String.format("Bound %s to %s", module.name, Keyboard.getKeyName(module.getKey())));
			} else {
				Client.chat("Module " + moduleName + " not found.");
			}
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("clear")) {
				for(Module module: Client.modules) {
					module.keyCode.setKeyCode(0);
				}
			}
			Client.chat("Cleared all binds. (set them to NONE)");
		}
	}
}
