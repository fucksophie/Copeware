package lv.yourfriend.copeware.modules.configs;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.*;

public class Mush extends Module {

	public Mush() {
		super("Mush", Keyboard.KEY_NONE, Category.CONFIGS);
	}

	public void onDisable() {
	}
	public void onEnable() {
		setupKillaura();
        toggle();
	}
	
	public void setupKillaura() {
		Module killaura = Client.getModuleByName("Killaura");
		((NumberSetting)killaura.getSetting("range")).setValue(5.9);
		((BooleanSetting)killaura.getSetting("noswing")).setEnabled(false);

		((BooleanSetting)killaura.getSetting("autoblock")).setEnabled(true);
		((BooleanSetting)killaura.getSetting("srotations")).setEnabled(true);
		((NumberSetting)killaura.getSetting("cps")).setValue(20);
	}
}