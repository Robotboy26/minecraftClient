/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.phantom.command;

import dev.phantom.command.commands.*;
import dev.phantom.utils.PhantomLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.brigadier.arguments.StringArgumentType;

public class CommandManager {
    public String prefix = "!";
    public List<Command> commands;
    boolean commandFound;

    public CommandManager() {
        commands = new ArrayList<>();

        /* Add commands here */
        commands.add(new ChatCmd());
        commands.add(new HelpCmd());
        commands.add(new TestCmd());
        // commands.add(new ModuleCommand());
        // commands.add(new ModulesCommand());
    }

    /**
     * Executes a command
     *
     * @param command command to execute
     */
    public void execute(String command) {
        String message = command;

        if (!message.startsWith(prefix)) return;

        message = message.substring(prefix.length());
        if (message.split(" ").length > 0) {
            commandFound = false;
            String commandName = message.split(" ")[0];
            for (Command c : commands) {
                if (c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
                    c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
                    commandFound = true;
                    break;
                }
            }
            if (!commandFound) PhantomLogger.error("Command not found, use " + prefix + "help");
        }
    }

    /**
     * Gets a command by name.
     *
     * @param name name of command
     */
    public Command getCommand(String name) {
        for (Command command : commands) {
            if (command.name.equalsIgnoreCase(name)) return command;
        }
        return null;
    }

    public static Object literal(String string) {
        return null;
    }

    public static Object argument(String string, StringArgumentType greedyString) {
        return null;
    }
}
