/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.nebula.command.commands;

import dev.nebula.Nebula;
import dev.nebula.command.Command;
import dev.nebula.utils.NebulaLogger;

import java.util.stream.Collectors;

public class HelpCmd extends Command {

    public HelpCmd() {
        super("Help", "Shows a list of commands", "help", "h");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length == 0) {
            NebulaLogger.info("Commands: " + Nebula.getInstance().getCommandManager().commands.stream()
                    .map(Command::getName).collect(Collectors.joining(", ")));
        } else {
            for (Command cmd : Nebula.getInstance().getCommandManager().commands) {
                if (cmd.getName().equalsIgnoreCase(args[0])) {
                    NebulaLogger.info(cmd.getSyntax());
                    return;
                }
            }
            NebulaLogger.error("Command not found.");
        }
    }
}
