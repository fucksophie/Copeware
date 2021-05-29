package lv.yourfriend.copeware.modules.combat;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventPacket;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
public class Velocity extends Module {

	public Velocity() {
		super("Velocity", Keyboard.KEY_NONE, Category.COMBAT);
	}


	public void onEvent(Event e) {
		if (e instanceof EventPacket) {
			EventPacket event = (EventPacket) e;
			if (event.getPacket() instanceof S12PacketEntityVelocity) {
				e.setCancelled(true);
			} else if (event.getPacket() instanceof S27PacketExplosion) {
				e.setCancelled(true);
			}
		}
	}
}