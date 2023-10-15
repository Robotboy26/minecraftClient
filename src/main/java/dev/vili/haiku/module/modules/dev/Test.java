package dev.vili.haiku.module.modules.dev;

import java.util.Collection;
import java.util.Collections;

import org.lwjgl.glfw.GLFW;

import java.util.List;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.utils.HaikuLogger;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;

public class Test extends Module {
    private ResourcePackProfile originalResourcePack;

    public Test() {
        super("Test", "Test", GLFW.GLFW_KEY_UNKNOWN, Module.Category.DEV);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        // Get the Minecraft client instance and the resource pack repository
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ResourcePackManager resourcePackManager = minecraft.getResourcePackManager();
        resourcePackManager.scanPacks();
        Collection<ResourcePackProfile> enabledProfiles = resourcePackManager.getEnabledProfiles();
        this.originalResourcePack = enabledProfiles.isEmpty() ? null : enabledProfiles.iterator().next();

        // Get the desired resource pack from the repository
        ResourcePackProfile resourcePack = resourcePackManager.getProfile("Xray");
        MinecraftClient.getInstance().worldRenderer.reload(null);
        HaikuLogger.logger.info("Xray: " + resourcePack);
        List<String> enabledProfileNames = Collections.singletonList("Xray");
        resourcePackManager.setEnabledProfiles(Collections.emptyList());
        resourcePackManager.setEnabledProfiles(enabledProfileNames);
        minecraft.reloadResources();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        // Get the Minecraft client instance and the resource pack repository
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ResourcePackManager resourcePackManager = minecraft.getResourcePackManager();

        // Set the original resource pack as active
        resourcePackManager.setEnabledProfiles(Collections.emptyList());

        // Reload the game's resources to apply the changes immediately
        minecraft.reloadResources();
    }
}
