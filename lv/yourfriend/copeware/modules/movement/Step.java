package lv.yourfriend.copeware.modules.movement;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
public class Step extends Module {

	public Step() {
		super("Step", Keyboard.KEY_NONE, Category.MOVEMENT);
	}

	public void onEvent(Event event) {
		if(event instanceof EventMotion) {
			if(mc.thePlayer.isCollidedHorizontally) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.52, mc.thePlayer.posZ);
			}
 		}
	}
}