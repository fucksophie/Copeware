package lv.yourfriend.copeware.modules.combat;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventPacket;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module {
	public ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet", "Minijump");
	
	public Criticals() {
		super("Criticals", Keyboard.KEY_NONE, Category.COMBAT);
		this.addSettings(mode);
	}

	public void onEvent(Event e) {
			this.setDisplayName(name+" (" +mode.getMode()+")");
			if(e instanceof EventPacket) {
				EventPacket event = (EventPacket)e;
				
				if(event.isIncoming() && event.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK) {
					if (mode.getMode().equals("Packet")) {
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625D, mc.thePlayer.posZ, false));
			             mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			             mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.001D, mc.thePlayer.posZ, false));
			             mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			       } else if (mode.getMode().equals("Minijump") && mc.thePlayer.onGround) {
			    	   mc.thePlayer.onGround = false;
			           mc.thePlayer.motionY = 0.1D;
			           mc.thePlayer.fallDistance = 0.1F;
			       }
				} 
				}
			}
		}