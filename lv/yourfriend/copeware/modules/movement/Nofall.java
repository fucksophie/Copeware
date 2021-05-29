package lv.yourfriend.copeware.modules.movement;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
public class Nofall extends Module {

	public Nofall() {
		super("Nofall", Keyboard.KEY_NONE, Category.MOVEMENT);
	}

	public void onEvent(Event event) {
		if(event instanceof EventUpdate) {
			if(event.isPre()) {
				if(mc.thePlayer.fallDistance > 2) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				}
			}
		}
	}
}