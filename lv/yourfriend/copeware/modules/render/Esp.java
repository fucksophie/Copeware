package lv.yourfriend.copeware.modules.render;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.BooleanSetting;

public class Esp extends Module {
	BooleanSetting players = new BooleanSetting("Players", true);
	BooleanSetting animals = new BooleanSetting("Animals", false);
	BooleanSetting monsters = new BooleanSetting("Monsters", false);
	
	public Esp() {
		super("Esp", Keyboard.KEY_NONE, Category.RENDER);
		this.addSettings(players, animals, monsters);
	}
}