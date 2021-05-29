package lv.yourfriend.copeware.modules.render;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.ui.ClickGuiScreen;
import me.tireman.hexa.alts.GuiAltManager;
public class Fullbright extends Module {

	public Fullbright() {
		super("Fullbright", Keyboard.KEY_INSERT, Category.RENDER);
	}

	public void onEnable() {
		mc.gameSettings.gammaSetting = 100;
	}
	
	public void onDisable() {
		mc.gameSettings.gammaSetting = 1;
	}
}