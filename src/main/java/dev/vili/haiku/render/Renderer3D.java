/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.render;

import dev.vili.haiku.utils.world.Dir;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class Renderer3D {
    public final Mesh lines = new ShaderMesh(Shaders.POS_COLOR, DrawMode.Lines, Mesh.Attrib.Vec3, Mesh.Attrib.Color);
    public final Mesh triangles = new ShaderMesh(Shaders.POS_COLOR, DrawMode.Triangles, Mesh.Attrib.Vec3, Mesh.Attrib.Color);

    public void begin() {
        lines.begin();
        triangles.begin();
    }

    public void end() {
        lines.end();
        triangles.end();
    }

    public void render(MatrixStack matrices) {
        lines.render(matrices);
        triangles.render(matrices);
    }

    // Lines

    public void line(double x1, double y1, double z1, double x2, double y2, double z2, double r1, double g1, double b1, double r2, double g2, double b2) {
        lines.line(
            lines.vec3(x1, y1, z1).color(r1, g1, b1).next(),
            lines.vec3(x2, y2, z2).color(r2, g2, b2).next()
        );
    }

    public void line(double x1, double y1, double z1, double x2, double y2, double z2, double r1, double g1, double b1) {
        line(x1, y1, z1, x2, y2, z2, r1, g1, b1, r1, g1, b1);
    }

    @SuppressWarnings("Duplicates")
    public void boxLines(double x1, double y1, double z1, double x2, double y2, double z2, double r, double g, double b, int excludeDir) {
        int blb = lines.vec3(x1, y1, z1).color(r, g, b).next();
        int blf = lines.vec3(x1, y1, z2).color(r, g, b).next();
        int brb = lines.vec3(x2, y1, z1).color(r, g, b).next();
        int brf = lines.vec3(x2, y1, z2).color(r, g, b).next();
        int tlb = lines.vec3(x1, y2, z1).color(r, g, b).next();
        int tlf = lines.vec3(x1, y2, z2).color(r, g, b).next();
        int trb = lines.vec3(x2, y2, z1).color(r, g, b).next();
        int trf = lines.vec3(x2, y2, z2).color(r, g, b).next();

        if (excludeDir == 0) {
            // Bottom to top
            lines.line(blb, tlb);
            lines.line(blf, tlf);
            lines.line(brb, trb);
            lines.line(brf, trf);

            // Bottom loop
            lines.line(blb, blf);
            lines.line(brb, brf);
            lines.line(blb, brb);
            lines.line(blf, brf);

            // Top loop
            lines.line(tlb, tlf);
            lines.line(trb, trf);
            lines.line(tlb, trb);
            lines.line(tlf, trf);
        }
        else {
            // Bottom to top
            if (Dir.isNot(excludeDir, Dir.WEST) && Dir.isNot(excludeDir, Dir.NORTH)) lines.line(blb, tlb);
            if (Dir.isNot(excludeDir, Dir.WEST) && Dir.isNot(excludeDir, Dir.SOUTH)) lines.line(blf, tlf);
            if (Dir.isNot(excludeDir, Dir.EAST) && Dir.isNot(excludeDir, Dir.NORTH)) lines.line(brb, trb);
            if (Dir.isNot(excludeDir, Dir.EAST) && Dir.isNot(excludeDir, Dir.SOUTH)) lines.line(brf, trf);

            // Bottom loop
            if (Dir.isNot(excludeDir, Dir.WEST) && Dir.isNot(excludeDir, Dir.DOWN)) lines.line(blb, blf);
            if (Dir.isNot(excludeDir, Dir.EAST) && Dir.isNot(excludeDir, Dir.DOWN)) lines.line(brb, brf);
            if (Dir.isNot(excludeDir, Dir.NORTH) && Dir.isNot(excludeDir, Dir.DOWN)) lines.line(blb, brb);
            if (Dir.isNot(excludeDir, Dir.SOUTH) && Dir.isNot(excludeDir, Dir.DOWN)) lines.line(blf, brf);

            // Top loop
            if (Dir.isNot(excludeDir, Dir.WEST) && Dir.isNot(excludeDir, Dir.UP)) lines.line(tlb, tlf);
            if (Dir.isNot(excludeDir, Dir.EAST) && Dir.isNot(excludeDir, Dir.UP)) lines.line(trb, trf);
            if (Dir.isNot(excludeDir, Dir.NORTH) && Dir.isNot(excludeDir, Dir.UP)) lines.line(tlb, trb);
            if (Dir.isNot(excludeDir, Dir.SOUTH) && Dir.isNot(excludeDir, Dir.UP)) lines.line(tlf, trf);
        }

        lines.growIfNeeded();
    }

    public void blockLines(int x, int y, int z, double r, double g, double b, int excludeDir) {
        boxLines(x, y, z, x + 1, y + 1, z + 1, r, g, b, excludeDir);
    }

    // Quads

    public void quad(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, double r1, double g1, double b1, double r2, double g2, double b2, double r3, double g3, double b3, double r4, double g4, double b4) {
        triangles.quad(
            triangles.vec3(x1, y1, z1).color(r1, g1, b1).next(),
            triangles.vec3(x2, y2, z2).color(r2, g2, b2).next(),
            triangles.vec3(x3, y3, z3).color(r3, g3, b3).next(),
            triangles.vec3(x4, y4, z4).color(r4, g4, b4).next()
        );
    }

    public void quad(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, double r, double g, double b) {
        quad(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, r, g, b, r, g, b, r, g, b, r, g, b);
    }

    public void quadVertical(double x1, double y1, double z1, double x2, double y2, double z2, double r, double  g, double b) {
        quad(x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, r, g, b);
    }

    public void quadHorizontal(double x1, double y, double z1, double x2, double z2, double r, double  g, double b) {
        quad(x1, y, z1, x1, y, z2, x2, y, z2, x2, y, z1, r, g, b);
    }

    public void gradientQuadVertical(double x1, double y1, double z1, double x2, double y2, double z2, double tr, double tg, double tb, double br, double bg, double bb) {
        quad(x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, tr, tg, tb, tr, tg, tb, br, bg, bb, br, bg, bb);
    }

    // Sides

    @SuppressWarnings("Duplicates")
    public void side(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, double sr, double sg, double sb, double lr, double lg, double lb, ShapeMode mode) {
        if (mode.lines()) {
            int i1 = lines.vec3(x1, y1, z1).color(lr, lg, lb).next();
            int i2 = lines.vec3(x2, y2, z2).color(lr, lg, lb).next();
            int i3 = lines.vec3(x3, y3, z3).color(lr, lg, lb).next();
            int i4 = lines.vec3(x4, y4, z4).color(lr, lg, lb).next();

            lines.line(i1, i2);
            lines.line(i2, i3);
            lines.line(i3, i4);
            lines.line(i4, i1);
        }

        if (mode.sides()) {
            quad(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, sr, sg, sb);
        }
    }

    public void sideVertical(double x1, double y1, double z1, double x2, double y2, double z2, double sr, double sg, double sb, double lr, double lg, double lb, ShapeMode mode) {
        side(x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, sr, sg, sb, lr, lg, lb, mode);
    }

    public void sideHorizontal(double x1, double y1, double z1, double x2, double y2, double z2, double sr, double sg, double sb, double lr, double lg, double lb, ShapeMode mode) {
        side(x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, sr, sg, sb, lr, lg, lb, mode);
    }

    // Boxes

    @SuppressWarnings("Duplicates")
    public void boxSides(double x1, double y1, double z1, double x2, double y2, double z2, double r, double g, double b, int excludeDir) {
        int blb = triangles.vec3(x1, y1, z1).color(r, g, b).next();
        int blf = triangles.vec3(x1, y1, z2).color(r, g, b).next();
        int brb = triangles.vec3(x2, y1, z1).color(r, g, b).next();
        int brf = triangles.vec3(x2, y1, z2).color(r, g, b).next();
        int tlb = triangles.vec3(x1, y2, z1).color(r, g, b).next();
        int tlf = triangles.vec3(x1, y2, z2).color(r, g, b).next();
        int trb = triangles.vec3(x2, y2, z1).color(r, g, b).next();
        int trf = triangles.vec3(x2, y2, z2).color(r, g, b).next();

        if (excludeDir == 0) {
            // Bottom to top
            triangles.quad(blb, blf, tlf, tlb);
            triangles.quad(brb, trb, trf, brf);
            triangles.quad(blb, tlb, trb, brb);
            triangles.quad(blf, brf, trf, tlf);

            // Bottom
            triangles.quad(blb, brb, brf, blf);

            // Top
            triangles.quad(tlb, tlf, trf, trb);
        }
        else {
            // Bottom to top
            if (Dir.isNot(excludeDir, Dir.WEST)) triangles.quad(blb, blf, tlf, tlb);
            if (Dir.isNot(excludeDir, Dir.EAST)) triangles.quad(brb, trb, trf, brf);
            if (Dir.isNot(excludeDir, Dir.NORTH)) triangles.quad(blb, tlb, trb, brb);
            if (Dir.isNot(excludeDir, Dir.SOUTH)) triangles.quad(blf, brf, trf, tlf);

            // Bottom
            if (Dir.isNot(excludeDir, Dir.DOWN)) triangles.quad(blb, brb, brf, blf);

            // Top
            if (Dir.isNot(excludeDir, Dir.UP)) triangles.quad(tlb, tlf, trf, trb);
        }

        triangles.growIfNeeded();
    }

    public void blockSides(int x, int y, int z, double r, double g, double b, int excludeDir) {
        boxSides(x, y, z, x + 1, y + 1, z + 1, r, g, b, excludeDir);
    }

    public void box(double x1, double y1, double z1, double x2, double y2, double z2, double sr, double sg, double sb, double lr, double lg, double lb, ShapeMode mode, int excludeDir) {
        if (mode.lines()) boxLines(x1, y1, z1, x2, y2, z2, lr, lg, lb, excludeDir);
        if (mode.sides()) boxSides(x1, y1, z1, x2, y2, z2, sr, sg, sb, excludeDir);
    }

    public void box(BlockPos pos, double sr, double sg, double sb, double lr, double lg, double lb, ShapeMode mode, int excludeDir) {
        if (mode.lines()) boxLines(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, lr, lg, lb, excludeDir);
        if (mode.sides()) boxSides(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, sr, sg, sb, excludeDir);
    }

    public void box(Box box, double sr, double sg, double sb, double lr, double lg, double lb, ShapeMode mode, int excludeDir) {
        if (mode.lines()) boxLines(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, lr, lg, lb, excludeDir);
        if (mode.sides()) boxSides(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, sr, sg, sb, excludeDir);
    }
}
