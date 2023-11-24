/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.phantom.command.commands;

import dev.phantom.Phantom;
import dev.phantom.command.Command;
import dev.phantom.utils.PhantomLogger;

import java.util.stream.Collectors;

public class HelpCmd extends Command {

    public HelpCmd() {
        super("Help", "Shows a list of commands", "help", "h");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length == 0) {
            PhantomLogger.info("Commands: " + Phantom.getInstance().getCommandManager().commands.stream()
                    .map(Command::getName).collect(Collectors.joining(", ")));
        } else {
            for (Command cmd : Phantom.getInstance().getCommandManager().commands) {
                if (cmd.getName().equalsIgnoreCase(args[0])) {
                    PhantomLogger.info(cmd.getSyntax());
                    return;
                }
            }
            PhantomLogger.error("Command not found.");
        }
    }
}
