package lv.yourfriend.copeware.events.listeners;

import lv.yourfriend.copeware.events.Event;

public class EventKey extends Event<EventKey> {
	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int key;
	
	public EventKey(int key) {
		this.key = key;
	}
}
