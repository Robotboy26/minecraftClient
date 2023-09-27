package dev.vili.haiku.command.commands;

import dev.vili.haiku.Haiku;
import dev.vili.haiku.command.Command;
import dev.vili.haiku.utils.HaikuLogger;

public class ClosestPlayer extends Command {

    public ClosestPlayer() {
        super("ClosestPlayer", "returns the name of the closest player (will return none if no player within  blocks)", "ClosestPlayer", "CP?");
    }

    String closestPlayer = "";

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length == 0) {
            closestPlayer = mc.world.getClosestPlayer(mc.player, 1000).getName().getString();
            HaikuLogger.info(closestPlayer);
        } else {
            for (Command cmd : Haiku.getInstance().getCommandManager().commands) {
                if (cmd.getName().equalsIgnoreCase(args[0])) {
                    HaikuLogger.info(cmd.getSyntax());
                    return;
                }
            }
            HaikuLogger.error("Command not found.");
        }
    }
}
