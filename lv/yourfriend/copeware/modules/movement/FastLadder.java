package lv.yourfriend.copeware.modules.movement;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
public class FastLadder extends Module {

	public FastLadder() {
		super("FastLadder", Keyboard.KEY_NONE, Category.MOVEMENT);
	}

	public void onEvent(Event event) {
		if(event instanceof EventMotion) {
			if(!mc.thePlayer.onGround && mc.thePlayer.moveForward > 0 && mc.thePlayer.isOnLadder()) {
				mc.thePlayer.motionY += 0.2;
			}
 		}
	}
}