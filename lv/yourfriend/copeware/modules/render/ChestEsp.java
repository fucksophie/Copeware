package lv.yourfriend.copeware.modules.render;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.BooleanSetting;

public class ChestEsp extends Module {
	public ChestEsp() {
		super("ChestEsp", Keyboard.KEY_NONE, Category.RENDER);
	}
}