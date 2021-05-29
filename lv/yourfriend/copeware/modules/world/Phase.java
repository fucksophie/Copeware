package lv.yourfriend.copeware.modules.world;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.ModeSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.util.MovementUtils;
import lv.yourfriend.copeware.util.TickTimer;
import net.minecraft.util.ChatComponentText;

import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
/*
 * Code credits:
 * 
 * AAC4/Redesky Phase - https://forums.ccbluex.net/topic/1584/liquidbounce-redesky-sex-script
 * 						AutoReplyBot on CCBlueX forums (Skidma)
 * 
 * Matrix Phase (Mineplex) - https://github.com/CCBlueX/LiquidBounce/blob/legacy/shared/main/java/net/ccbluex/liquidbounce/features/module/modules/exploit/Phase.java
 * 				  CCBlueX and Contributors
 */

public class Phase extends Module {
	public ModeSetting mode = new ModeSetting("Mode", "AAC4", "AAC4", "Matrix");
	public Phase() {
		super("Phase", Keyboard.KEY_NONE, Category.WORLD);
		this.addSettings(mode);
	}

    private boolean matrixClip;
    private final TickTimer matrixTickTimer = new TickTimer();
    
	public void onEnable() {
		if(mode.getMode() == "AAC4") {
			if (mc.thePlayer.isCollidedHorizontally) {
            	mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 0.00000001, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            	mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 0.000001, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
			} else {
            	Client.chat("Stand near blocks.");
        	}
		}
	}
	
	public void onEvent(Event e) {
		this.setDisplayName(name+" (" +mode.getMode()+")");
		if(e instanceof EventMotion) {
			EventMotion event = (EventMotion)e;
			if (mc.thePlayer.isCollidedHorizontally)
                matrixClip = true;
            if (!matrixClip)
                return;

            matrixTickTimer.update();

            if (matrixTickTimer.hasTimePassed(3)) {
                matrixTickTimer.reset();
                matrixClip = false;
            } else if (matrixTickTimer.hasTimePassed(1)) {
                final double offset = matrixTickTimer.hasTimePassed(2) ? 1.6D : 0.06D;
                final double direction = MovementUtils.getDirection();
                event.setX(event.getX() + (-Math.sin(direction) * offset));
                event.setY(event.getY());
                event.setZ(event.getZ() + (Math.cos(direction) * offset));
            }
        }
	}
}