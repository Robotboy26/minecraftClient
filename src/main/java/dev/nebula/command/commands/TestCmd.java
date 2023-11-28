package dev.nebula.command.commands;

import dev.nebula.command.Command;
import dev.nebula.utils.NebulaLogger;

public class TestCmd extends Command {

    public TestCmd() {
        super("Test", "prints somthing", "test", "t");
    }

    @Override
    public void onCommand(String[] args, String command) {
        NebulaLogger.info("This is a test");
    }
}
