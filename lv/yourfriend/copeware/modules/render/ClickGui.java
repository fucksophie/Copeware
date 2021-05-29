package lv.yourfriend.copeware.modules.render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.modules.Module.Category;
import lv.yourfriend.copeware.settings.ModeSetting;
import lv.yourfriend.copeware.ui.ClickGuiScreen;
public class ClickGui extends Module {
	public ModeSetting color = new ModeSetting("Color", "CYAN", "CYAN", "RED", "YELLOW", "BLUE", "PINK");
	Color[] colors = {Color.cyan, Color.red, Color.yellow, Color.blue, Color.pink};
	
	public ClickGui() {
		super("ClickGui", Keyboard.KEY_RSHIFT, Category.RENDER);
		this.addSettings(color);
	}

	public void onEnable() {		
		toggle();
		
		Client.color = colors[color.modes.indexOf(color.getMode())];
		
		mc.displayGuiScreen(new ClickGuiScreen());
	}
	
	public void onDisable() {
		Client.color = colors[color.modes.indexOf(color.getMode())];
	}
}
