package dev.nebula.module.modules.HUD;

import dev.nebula.eventbus.NebulaSubscribe;
import dev.nebula.module.Module;
import dev.nebula.utils.NebulaLogger;

public class Test extends Module {
    public Test() {
        super("Test", "For testing NebulaLogger.", ()->true, true);
    }

    public void onEnable() {
        NebulaLogger.info("Test");
    }

    @NebulaSubscribe
    public void onTick() {
        NebulaLogger.info("Test");
    }
    
}
