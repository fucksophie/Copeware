package lv.yourfriend.copeware.modules.player;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.util.MovementUtils;
import lv.yourfriend.copeware.util.TickTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
public class KillSults extends Module {
	public KillSults() {
		super("KillSults", Keyboard.KEY_NONE, Category.PLAYER);
	}
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
	           for(Object objects: mc.theWorld.loadedEntityList) {
	                if(objects instanceof EntityPlayer) {
	                    EntityPlayer entity = (EntityPlayer)objects;
	                    if( mc.thePlayer != null && entity != mc.thePlayer && entity.getHealth() == 0 && mc.thePlayer.getDistanceToEntity(entity) < 4F) {
	                    		mc.theWorld.removeEntity(entity);
	                    		
	        					mc.thePlayer.sendChatMessage(getSult(entity.getName()));
	        			}
					}
				}
			}
        }
	
	public String getSult(String name) {
		String[] sults = {
			name + " got clapped by " + Client.name + "!!",
			"https://www.wikihow.com/Tie-a-Noose " + name,
			"How to cope and seethe: https://www.youtube.com/watch?v=4t5AKrZu_KE " + name,
			"OMG!! I just fricked ur mom " + name + "!!"
		};
		
		
		return sults[(int)(Math.random() * sults.length)] + " " + String.valueOf(Math.random()).substring(2);
		
	}
}