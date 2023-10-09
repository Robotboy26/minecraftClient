package dev.vili.haiku.event.events.render;

public interface RenderListener extends AbstractListener {
	public abstract void OnRender(RenderEvent event);
}
