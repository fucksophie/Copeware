package lv.yourfriend.copeware.events.listeners;

import lv.yourfriend.copeware.events.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event<EventPacket>{
	Packet packet;

	public EventPacket(Packet packet) {
		super();
		this.packet = packet;
	}
	
	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

}