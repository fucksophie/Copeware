package lv.yourfriend.copeware.modules.render;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
public class Nametags extends Module {

	public Nametags() {
		super("Nametags", Keyboard.KEY_NONE, Category.RENDER);
	}
}