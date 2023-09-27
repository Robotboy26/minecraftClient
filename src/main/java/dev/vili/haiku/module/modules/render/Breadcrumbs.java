/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.event.events.render.Render3DEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.utils.misc.Pool;
import net.minecraft.world.dimension.DimensionType;

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

    @HaikuSubscribe
    private void onRender(Render3DEvent event) {
        int iLast = -1;

        for (Section section : sections) {
            if (iLast == -1) {
                iLast = event.renderer.lines.vec3(section.x1, section.y1, section.z1).color(colorRed.getValue(), colorBlue.getValue(), colorGreen.getValue()).next();
            }

            int i = event.renderer.lines.vec3(section.x2, section.y2, section.z2).color(colorRed.getValue(), colorBlue.getValue(), colorGreen.getValue()).next();
            event.renderer.lines.line(iLast, i);
            iLast = i;
        }
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

        public void render(Render3DEvent event) {
            event.renderer.line(x1, y1, z1, x2, y2, z2, colorRed.getValue(), colorBlue.getValue(), colorGreen.getValue());
        }
    }
}
