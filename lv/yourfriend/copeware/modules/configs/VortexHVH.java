package lv.yourfriend.copeware.modules.configs;

import org.lwjgl.input.Keyboard;

import com.google.gson.Gson;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventPacket;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.modules.Module.Category;
import lv.yourfriend.copeware.settings.BooleanSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.util.TimeHelper;
import lv.yourfriend.copeware.util.Timer;
import net.minecraft.network.play.server.S02PacketChat;

public class VortexHVH extends Module {
	Gson gson = new Gson();
	Timer timer = new Timer();
	int time = 0;
	boolean textAlternate = false;
	public VortexHVH() {
		super("VortexHVH", Keyboard.KEY_NONE, Category.CONFIGS);
	}

	public void onDisable() {

	}
	public void onEnable() {
		time = 1;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(time != 0) {
				if(timer.hasTimeElapsed(time, true)) {
					mc.thePlayer.sendChatMessage("/api minify BegTime lukeacat");
					time = 0;
				}
			}
		}
		
		if(e instanceof EventPacket) {
			EventPacket event = (EventPacket)e;
			if(event.getPacket() instanceof S02PacketChat) {
				S02PacketChat packet = (S02PacketChat)event.getPacket();
				String text = packet.func_148915_c().getUnformattedText();
				
				if(text.startsWith("API")) {
					if(text.contains("BegTime")) {
						event.setCancelled(true);
						BegTime api = gson.fromJson(text.substring(4), BegTime.class);
						
						if(api.BegTime <= 0) {
							textAlternate = !textAlternate;
							if(textAlternate) {
								mc.thePlayer.sendChatMessage("> please beg ["+mc.thePlayer.getName()+" with "+Client.name+"]");
							} else {
								mc.thePlayer.sendChatMessage("please beg");
							}
							time = 60650;
						} else {
							time = Math.max(api.BegTime/2, 650);
						}
					}
				} else if(text.contains("was slain by " + mc.thePlayer.getName())) {
					mc.thePlayer.sendChatMessage("> gg ["+mc.thePlayer.getName()+" with "+Client.name+"]");
				}
			}
		}
	}
	
	
	public class BegTime {
		String playerName;
		int BegTime;
	}
}