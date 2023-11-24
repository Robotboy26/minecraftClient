package dev.phantom.command.commands;

import dev.phantom.command.Command;
import dev.phantom.utils.PhantomLogger;

public class TestCmd extends Command {

    public TestCmd() {
        super("Test", "prints somthing", "test", "t");
    }

    @Override
    public void onCommand(String[] args, String command) {
        PhantomLogger.info("This is a test");
    }
}
