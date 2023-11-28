package dev.phantom.module.modules.HUD;

import dev.phantom.eventbus.PhantomSubscribe;
import dev.phantom.module.Module;
import dev.phantom.utils.PhantomLogger;

public class Test extends Module {
    public Test() {
        super("Test", "For testing PhantomLogger.", ()->true, true);
    }

    public void onEnable() {
        PhantomLogger.info("Test");
    }

    @PhantomSubscribe
    public void onTick() {
        PhantomLogger.info("Test");
    }
    
}
