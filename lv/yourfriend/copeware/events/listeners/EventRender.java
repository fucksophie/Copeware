package lv.yourfriend.copeware.events.listeners;

import lv.yourfriend.copeware.events.Event;

public class EventRender  extends Event<EventRender>{
	public float getPartialTicks() {
		return partialTicks;
	}

	public void setPartialTicks(int partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float partialTicks;
	
	public EventRender(float partialTicks) {
		this.partialTicks = partialTicks;
	}
}
