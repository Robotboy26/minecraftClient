package dev.vili.haiku.event.events.entity;

import dev.vili.haiku.event.Event;

public class EventEntityControl extends Event {
	
	private Boolean canBeControlled;

	public Boolean canBeControlled() {
		return canBeControlled;
	}

	public void setControllable(Boolean canBeControlled) {
		this.canBeControlled = canBeControlled;
	}
}
