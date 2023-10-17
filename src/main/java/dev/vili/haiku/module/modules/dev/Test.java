package dev.vili.haiku.module.modules.dev;

import org.lwjgl.glfw.GLFW;
import dev.vili.haiku.event.events.render.Render3DEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.utils.HaikuLogger;
public class Test extends Module {

    public Test() {
        super("Test", "Test", GLFW.GLFW_KEY_UNKNOWN, Module.Category.DEV);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        HaikuLogger.info("Test module is enabled!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @HaikuSubscribe
    public void render(Render3DEvent event) {
        HaikuLogger.info("Test module render world tick!");
    }
}
