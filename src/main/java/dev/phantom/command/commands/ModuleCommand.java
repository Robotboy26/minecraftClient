// package dev.phantom.command.commands;

// import dev.phantom.Phantom;
// import dev.phantom.command.Command;
// import dev.phantom.module.Module;
// import dev.phantom.utils.PhantomLogger;
// import net.minecraft.client.MinecraftClient;

// import java.util.Arrays;
// import java.util.List;

// public class ModuleCommand extends Command {
    
//     public ModuleCommand() {
//         super("module", "Enables or disables a module.", "module <moduleName> [enable/disable]", "mod");
//     }
    
//     @Override
//     public void onCommand(String[] args, String command) {
//         if (args.length >= 1) {
//             String moduleName = args[0];
//             boolean enable = true;
    
//             if (args.length >= 2) {
//                 String action = args[1].toLowerCase();
    
//                 if (action.equals("enable") || action.equals("true") || action.equals("on") || action.equals("toggle")) {
//                     enable = true;
//                 } else if (action.equals("disable") || action.equals("false") || action.equals("off") || action.equals("untoggle")) {
//                     enable = false;
//                 }
//             }
    
//             Module module = Phantom.getInstance().getModuleManager().getModule(moduleName);
    
//             if (module != null) {
//                 module.toggleFromCommand(enable);
//                 PhantomLogger.info("Module " + moduleName + " is now " + (enable ? "enabled" : "disabled") + ".");
//             } else {
//                 PhantomLogger.info("Module " + moduleName + " not found.");
//             }
//         }
//     }
// }