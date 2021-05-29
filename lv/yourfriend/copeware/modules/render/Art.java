package lv.yourfriend.copeware.modules.render;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventRenderGui;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.*;
import lv.yourfriend.copeware.ui.HUD;
import lv.yourfriend.copeware.util.MoveUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;

public class Art extends Module {
	int size = 0;
	
	Map<String, String> arts = new HashMap<String, String>();
	
	ModeSetting mode = new ModeSetting("Art", "dog", "dog", "sonic", "cat", "sus", "lollypop", "drink");
	
	public Art() {
		super("Art", Keyboard.KEY_NONE, Category.RENDER);
		
		arts.put("sonic", "          .,\r\n"
				+ ".      _,'f----.._\r\n"
				+ "|\\ ,-'\"/  |     ,'\r\n"
				+ "|,_  ,--.      /\r\n"
				+ "/,-. ,'`.     (_\r\n"
				+ "f  o|  o|__     \"`-.\r\n"
				+ ",-._.,--'_ `.   _.,-`\r\n"
				+ "`\"' ___.,'` j,-'\r\n"
				+ "  `-.__.,--'");
		
		arts.put("cat", "           __..--''``---....___   _..._    __\r\n"
				+ " /// //_.-'    .-/\";  `        ``<._  ``.''_ `. / // /\r\n"
				+ "///_.-' _..--.'_    \\                    `( ) ) // //\r\n"
				+ "/ (_..-' // ( < _     ;_..__               ; `' / ///\r\n"
				+ " / // // //  `-._,_)' // / ``--...____..-' /// / //");
		
		arts.put("dog", "            ,\r\n"
				+ "            |`-.__\r\n"
				+ "            / ' _/\r\n"
				+ "           ****` \r\n"
				+ "          /    }\r\n"
				+ "         /  \\ /\r\n"
				+ "     \\ /`   \\\\\\\r\n"
				+ "jgs   `\\    /_\\\\\r\n"
				+ "       `~~~~~``~`");
		
		
		arts.put("sus", "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣤⣤⣤⣤⣤⣶⣦⣤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀\r\n"
				+ "⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⡿⠛⠉⠙⠛⠛⠛⠛⠻⢿⣿⣷⣤⡀⠀⠀⠀⠀⠀\r\n"
				+ "⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⠋⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠈⢻⣿⣿⡄⠀⠀⠀⠀\r\n"
				+ "⠀⠀⠀⠀⠀⠀⠀⣸⣿⡏⠀⠀⠀⣠⣶⣾⣿⣿⣿⠿⠿⠿⢿⣿⣿⣿⣄⠀⠀⠀\r\n"
				+ "⠀⠀⠀⠀⠀⠀⠀⣿⣿⠁⠀⠀⢰⣿⣿⣯⠁⠀⠀⠀⠀⠀⠀⠀⠈⠙⢿⣷⡄⠀\r\n"
				+ "⠀⠀⣀⣤⣴⣶⣶⣿⡟⠀⠀⠀⢸⣿⣿⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣷⠀\r\n"
				+ "⠀⢰⣿⡟⠋⠉⣹⣿⡇⠀⠀⠀⠘⣿⣿⣿⣿⣷⣦⣤⣤⣤⣶⣶⣶⣶⣿⣿⣿⠀\r\n"
				+ "⠀⢸⣿⡇⠀⠀⣿⣿⡇⠀⠀⠀⠀⠹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠀\r\n"
				+ "⠀⣸⣿⡇⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠉⠻⠿⣿⣿⣿⣿⡿⠿⠿⠛⢻⣿⡇⠀⠀\r\n"
				+ "⠀⣿⣿⠁⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣧⠀⠀\r\n"
				+ "⠀⣿⣿⠀⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⠀⠀\r\n"
				+ "⠀⣿⣿⠀⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⠀⠀\r\n"
				+ "⠀⢿⣿⡆⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⡇⠀⠀\r\n"
				+ "⠀⠸⣿⣧⡀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⠃⠀⠀\r\n"
				+ "⠀⠀⠛⢿⣿⣿⣿⣿⣇⠀⠀⠀⠀⠀⣰⣿⣿⣷⣶⣶⣶⣶⠶⠀⢠⣿⣿⠀⠀⠀\r\n"
				+ "⠀⠀⠀⠀⠀⠀⠀⣿⣿⠀⠀⠀⠀⠀⣿⣿⡇⠀⣽⣿⡏⠁⠀⠀⢸⣿⡇⠀⠀⠀\r\n"
				+ "⠀⠀⠀⠀⠀⠀⠀⣿⣿⠀⠀⠀⠀⠀⣿⣿⡇⠀⢹⣿⡆⠀⠀⠀⣸⣿⠇⠀⠀⠀\r\n"
				+ "⠀⠀⠀⠀⠀⠀⠀⢿⣿⣦⣄⣀⣠⣴⣿⣿⠁⠀⠈⠻⣿⣿⣿⣿⡿⠏⠀⠀⠀⠀\r\n"
				+ "⠀⠀⠀⠀⠀⠀⠀⠈⠛⠻⠿⠿⠿⠿⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		
		
		arts.put("lollypop", "         _.-.\r\n"
				+ "       ,'/ //\\\r\n"
				+ "      /// // /)\r\n"
				+ "     /// // //|\r\n"
				+ "    /// // ///\r\n"
				+ "   /// // ///\r\n"
				+ "  (`: // ///\r\n"
				+ "   `;`: ///\r\n"
				+ "   / /:`:/\r\n"
				+ "  / /  `'\r\n"
				+ " / /\r\n"
				+ "(_/  hh");
		arts.put("drink", "()   ()      ()    /\r\n"
				+ "  ()      ()  ()  /\r\n"
				+ "   ______________/___\r\n"
				+ "   \\            /   /\r\n"
				+ "    \\^^^^^^^^^^/^^^/\r\n"
				+ "     \\     ___/   /\r\n"
				+ "      \\   (   )  /\r\n"
				+ "       \\  (___) /\r\n"
				+ "        \\ /    /\r\n"
				+ "         \\    /\r\n"
				+ "          \\  /\r\n"
				+ "           \\/\r\n"
				+ "           ||\r\n"
				+ "           ||\r\n"
				+ "           ||\r\n"
				+ "           ||\r\n"
				+ "           ||\r\n"
				+ "           /\\\r\n"
				+ "          /;;\\\r\n"
				+ "     ==============");
		this.addSettings(mode);
	}

	public void onEvent(Event event) {
		if(event instanceof EventRenderGui) {
			if(TargetHUD.isOn) return;
			this.setDisplayName(this.name + " (" + mode.getMode() + ")");
			String[] art = arts.get(mode.getMode()).split("\r\n");

			size = 0;
			int count = 20;
			for(String l: art) {if(mc.fontRendererObj.getStringWidth(l) > size)size=mc.fontRendererObj.getStringWidth(l);};			
			Gui.drawRect(size+32, (mc.fontRendererObj.FONT_HEIGHT*art.length)+80, 8, 40, 0x90000000);
			
			for(String text: art) {
				mc.fontRendererObj.drawString(text, 18, 10 * count-150, HUD.rainbow(150 * count));
				count++;
			}
		}
		
	}
}