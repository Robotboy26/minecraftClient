package dev.vili.haiku.module.modules.dev;

import org.lwjgl.glfw.GLFW;
import dev.vili.haiku.event.events.render.Render3DEvent;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.utils.HaikuLogger;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventBus;
import java.util.Collections;

public class Test extends Module {
    private final EventBus eventBus;

    public Test() {
        super("Test", "Test", GLFW.GLFW_KEY_UNKNOWN, Module.Category.DEV);
        this.eventBus = new EventBus();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        HaikuLogger.info("Test module is enabled!");
        // eventBus.subscribe(Render3DEvent.class, this::onRender);
        eventBus.subscribe(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        eventBus.unsubscribe(this);
    }

    @EventHandler
    public void onRender(Render3DEvent event) {
        HaikuLogger.info("Test module render tick!");
        // Render a block outline at 0 100 0
        // Orbit.getRenderer().drawOutline(0, 100, 0, 1.0, 1.0, 1.0, 1.0, 2.0);
    }

    @EventHandler
    public void onTick() {
        HaikuLogger.info("Test module tick!");
    }
}
