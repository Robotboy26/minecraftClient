/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.event.events.RenderEvent;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.utils.HaikuLogger;
import dev.vili.haiku.utils.misc.Pool;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.dimension.DimensionType;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Queue;

public class Breadcrumbs extends Module
{
    public final NumberSetting colorRed = new NumberSetting("ColorRed-Breadcrumbs", "The colorRed of the Breadcrumbs trail.", 25, 0, 255, 1);
    public final NumberSetting colorBlue = new NumberSetting("ColordBlue-Breadcrumbs", "The colorBlue of the Breadcrumbs trail.", 25, 0, 255, 1);
    public final NumberSetting colorGreen = new NumberSetting("ColorGreen-Breadcrumbs", "The colorGreen of the Breadcrumbs trail.", 25, 0, 255, 1);
    public final NumberSetting maxSections = new NumberSetting("MaxSections-Breadcrumbs", "The maximum number of sections.", 1000, 1, 5000, 1);
    public final NumberSetting sectionLength = new NumberSetting("SectionLength-Breadcrumbs", "The section length in blocks.", 0.5, 0, 1, 0.01);

    private final Pool<Section> sectionPool = new Pool<>(Section::new);
    private final Queue<Section> sections = new ArrayDeque<>();

    private Section section;

    private DimensionType lastDimension;

    public Breadcrumbs() {
        super("Breadcrumbs", "Displays a trail behind where you have walked.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);
        super.addSettings(colorRed, colorBlue, colorGreen, maxSections, sectionLength);
    }

    @Override
    public void onEnable() {
        section = sectionPool.get();
        section.set1();

        lastDimension = mc.world.getDimension();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        for (Section section : sections) sectionPool.free(section);
        sections.clear();
        super.onDisable();
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
        if (lastDimension != mc.world.getDimension()) {
            for (Section sec : sections) sectionPool.free(sec);
            sections.clear();
        }

        if (isFarEnough(section.x1, section.y1, section.z1)) {
            section.set2();

            if (sections.size() >= (int) maxSections.getValue()) {
                Section section = sections.poll();
                if (section != null) sectionPool.free(section);
            }

            sections.add(section);
            section = sectionPool.get();
            section.set1();
        }

        lastDimension = mc.world.getDimension();
    }

    // @HaikuSubscribe
    // public void onRender(RenderInGameHudEvent event) {
    //     HaikuLogger.info("RenderInGameHudEvent called");
    //     MatrixStack matrixStack = RenderSystem.getModelViewStack();
    //     matrixStack.push();
    //     // Add things to the matrix stack here
    //     matrixStack.pop();
    // }

    @HaikuSubscribe
    public void OnRender(RenderEvent event) {
        HaikuLogger.info("Render event called");
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        // Add things to the matrix stack here
        matrixStack.pop();
    }

    private boolean isFarEnough(double x, double y, double z) {
        return Math.abs(mc.player.getX() - x) >= sectionLength.getValue() || Math.abs(mc.player.getY() - y) >= sectionLength.getValue() || Math.abs(mc.player.getZ() - z) >= sectionLength.getValue();
    }

    private class Section {
        public float x1, y1, z1;
        public float x2, y2, z2;

        public void set1() {
            x1 = (float) mc.player.getX();
            y1 = (float) mc.player.getY();
            z1 = (float) mc.player.getZ();
        }

        public void set2() {
            x2 = (float) mc.player.getX();
            y2 = (float) mc.player.getY();
            z2 = (float) mc.player.getZ();
        }
    }
}
