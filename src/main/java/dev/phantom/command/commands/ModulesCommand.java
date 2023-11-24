// package dev.vili.haiku.command.commands;

// import dev.vili.haiku.Haiku;
// import dev.vili.haiku.command.Command;
// import dev.vili.haiku.module.Module;
// import dev.vili.haiku.utils.HaikuLogger;
// import net.minecraft.client.MinecraftClient;

// import java.util.List;

// public class ModulesCommand extends Command {

//     public ModulesCommand() {
//         super("modules", "Lists all available modules.", "modules", "listmodules");
//     }

//     @Override
//     public void onCommand(String[] args, String command) {
//         List<Module> modules = Haiku.getInstance().getModuleManager().getModules();

//         if (!modules.isEmpty()) {
//             StringBuilder message = new StringBuilder("Modules: ");

//             for (Module module : modules) {
//                 message.append(module.getName()).append(", ");
//             }

//             message.setLength(message.length() - 2); // Remove the last comma and space
//             HaikuLogger.info(message.toString());
//         } else {
//             HaikuLogger.info("No modules found.");
//         }
//     }
// }