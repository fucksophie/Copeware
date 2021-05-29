package lv.yourfriend.copeware.modules.movement;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.ModeSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.util.MoveUtils;
import lv.yourfriend.copeware.util.MovementUtils;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
public class Fly extends Module {
	public NumberSetting speed = new NumberSetting("Speed", 1, 1, 30, 0.5);
	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "NCP", "TakaFly", "OldNCP");
	boolean takaFlyWasFlown = false;
	boolean speedWasOn = false;
	 private double startY = 0.0;
	public Fly() {
		super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
		this.addSettings(speed, mode);
	}
	
	public void onEnable() {
		if(mc.thePlayer != null)startY = mc.thePlayer.posY;
        		
		if(Client.getModuleByName("Speed").isEnabled()) {
			speedWasOn = true;
			Client.getModuleByName("Speed").toggled = false;
			Client.getModuleByName("Speed").onDisable();
		}
		
		if(mode.getMode().equalsIgnoreCase("vanilla")) {
			mc.thePlayer.jump();
		}
		if(mode.getMode().equals("ncp")) {
            if (!mc.thePlayer.onGround)
                return;

            for(int i = 0; i < 64; i++) {
            	mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.049, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            }

            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY+0.1, mc.thePlayer.posZ, false));

            mc.thePlayer.motionX *= 0.1;
            		mc.thePlayer.motionZ *= 0.1;
            				mc.thePlayer.swingItem();
		}
		if(mode.getMode().equals("oldncp")) {
            if (!mc.thePlayer.onGround)
                return;

            for(int i = 0; i < 3; i++) {
            	mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.01, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            }

            mc.thePlayer.jump();
            mc.thePlayer.swingItem();
		}
	}
	
	public void onDisable() {
		mc.thePlayer.capabilities.isFlying = false;
		if(mode.getMode().equalsIgnoreCase("takafly")) {
			mc.gameSettings.keyBindForward.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
		}
		
		if(speedWasOn) {
			speedWasOn = false;
			Client.getModuleByName("Speed").toggled = true;
			Client.getModuleByName("Speed").onEnable();
		}
		
        mc.thePlayer.motionX = 0.0;
        		mc.thePlayer.motionY = 0.0;
        				mc.thePlayer.motionZ = 0.0;
        		        mc.timer.timerSpeed = 1f;
        		                mc.thePlayer.speedInAir = 0.02f;
	}
	
	public void onEvent(Event event) {
		this.setDisplayName(name+" (" +mode.getMode()+")");
		if(Client.getModuleByName("Speed").isEnabled()) {
			speedWasOn = true;
			Client.getModuleByName("Speed").toggled = false;
			Client.getModuleByName("Speed").onDisable();
		}
		
		if(event instanceof EventMotion) {
			
			if(mode.getMode().equalsIgnoreCase("vanilla")) {
					if(event.isPre()) {
					mc.thePlayer.capabilities.setFlySpeed((float) speed.getValue() / 10);
					mc.thePlayer.capabilities.isFlying = true;
				}
			} else if(mode.getMode().equalsIgnoreCase("ncp")) {
                mc.thePlayer.motionY = 0;
                if (mc.gameSettings.keyBindSneak.getIsKeyPressed()) mc.thePlayer.motionY = -0.5;
                MovementUtils.strafe();
			} else if(mode.getMode().equalsIgnoreCase("takafly")) {
				if (mc.thePlayer.hurtTime > 0) {
					mc.gameSettings.keyBindForward.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
					MoveUtils.setSpeed(9f);
					mc.thePlayer.jump();
					//mc.timer.timerSpeed = 0.42f;
					takaFlyWasFlown = true;
				}
				
				if(mc.thePlayer.onGround && takaFlyWasFlown) {
					takaFlyWasFlown = false;
					//mc.timer.timerSpeed = 1f;
					this.toggle();
				};
		} else if(mode.getMode().equalsIgnoreCase("oldncp")) {
            if (startY > mc.thePlayer.posY)
            	mc.thePlayer.motionY = -0.0001;
            if (mc.gameSettings.keyBindSneak.getIsKeyPressed())
            	mc.thePlayer.motionY = -0.2;
            if (mc.gameSettings.keyBindJump.getIsKeyPressed() && mc.thePlayer.posY < startY - 0.1)
            	mc.thePlayer.motionY = 0.2;
            MovementUtils.strafe();
		}
		}
	}
}