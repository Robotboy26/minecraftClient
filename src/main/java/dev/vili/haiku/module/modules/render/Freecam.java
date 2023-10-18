/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.event.events.entity.player.EventClientMove;
import dev.vili.haiku.event.events.meteor.MouseButtonEvent;
import dev.vili.haiku.event.events.meteor.MouseScrollEvent;
import dev.vili.haiku.event.game.OpenScreenEvent;

import org.joml.Vector3d;
import org.lwjgl.glfw.GLFW;

import com.ibm.icu.impl.units.UnitsData.Categories;

import dev.vili.haiku.event.events.EventOpenScreen;
import dev.vili.haiku.event.events.KeyEvent;
import dev.vili.haiku.event.events.PacketEvent;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.Setting;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.utils.world.PlayerCopyEntity;
import dev.vili.haiku.utils.HaikuLogger;
import dev.vili.haiku.utils.Utils;
import dev.vili.haiku.utils.player.Rotations;
import dev.vili.haiku.utils.misc.input.Input;
import dev.vili.haiku.utils.misc.input.KeyAction;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class Freecam extends Module {
	public final NumberSetting speed = new NumberSetting("speed", "Your speed while in freecam.", 1.0, 0.0, 20.0, 0.01);
	public final NumberSetting speedScrollSensitivity = new NumberSetting("speed-scroll-sensitivity", "Allows you to change speed value using scroll wheel. 0 to disable.", 0.0, 0.0, 2.0, 0.01);
	public final BooleanSetting toggleOnDamage = new BooleanSetting("toggle-on-damage", "Disables freecam when you take damage.", false);
	public final BooleanSetting toggleOnDeath = new BooleanSetting("toggle-on-death", "Disables freecam when you die.", false);
	public final BooleanSetting toggleOnLog = new BooleanSetting("toggle-on-log", "Disables freecam when you disconnect from a server.", true);
	public final BooleanSetting reloadChunks = new BooleanSetting("reload-chunks", "Disables cave culling.", true);
	public final BooleanSetting renderHands = new BooleanSetting("show-hands", "Whether or not to render your hands in freecam.", true);
	public final BooleanSetting rotate = new BooleanSetting("rotate", "Rotates to the block or entity you are looking at.", false);
	public final BooleanSetting staticView = new BooleanSetting("static", "Disables settings that move the view.", false);

	public final Vector3d pos = new Vector3d();
    public final Vector3d prevPos = new Vector3d();

    private Perspective perspective;
    private double speedValue;

    public float yaw, pitch;
    public float prevYaw, prevPitch;

    private double fovScale;
    private boolean bobView;

    private boolean forward, backward, right, left, up, down;

    public Freecam() {
        super("Freecam", "See things anywhere", GLFW.GLFW_KEY_G, Category.RENDER);
        super.addSettings(speed, speedScrollSensitivity, toggleOnDamage, toggleOnDeath, toggleOnLog, reloadChunks, renderHands, rotate, staticView);
    }

    @Override
    public void onEnable() {
        fovScale = mc.options.getFovEffectScale().getValue();
        bobView = mc.options.getBobView().getValue();
        if (staticView.isEnabled()) {
            mc.options.getFovEffectScale().setValue((double)0);
            mc.options.getBobView().setValue(false);
        }
        yaw = mc.player.getYaw();
        pitch = mc.player.getPitch();

        perspective = mc.options.getPerspective();
        speedValue = speed.getValue();

        Utils.set(pos, mc.gameRenderer.getCamera().getPos());
        Utils.set(prevPos, mc.gameRenderer.getCamera().getPos());

        prevYaw = yaw;
        prevPitch = pitch;

        forward = false;
        backward = false;
        right = false;
        left = false;
        up = false;
        down = false;

        unpress();
        if (reloadChunks.isEnabled()) mc.worldRenderer.reload();
		super.onEnable();
    }

    @Override
    public void onDisable() {
        if (reloadChunks.isEnabled()) mc.worldRenderer.reload();
        mc.options.setPerspective(perspective);
        if (staticView.isEnabled()) {
            mc.options.getFovEffectScale().setValue((double)fovScale);
            mc.options.getBobView().setValue(bobView);
        }
		super.onDisable();
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        unpress();

        prevPos.set(pos);
        prevYaw = yaw;
        prevPitch = pitch;
    }

    private void unpress() {
        mc.options.forwardKey.setPressed(false);
        mc.options.backKey.setPressed(false);
        mc.options.rightKey.setPressed(false);
        mc.options.leftKey.setPressed(false);
        mc.options.jumpKey.setPressed(false);
        mc.options.sneakKey.setPressed(false);
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
        if (mc.cameraEntity.isInsideWall()) mc.getCameraEntity().noClip = true;
        if (!perspective.isFirstPerson()) mc.options.setPerspective(Perspective.FIRST_PERSON);

        Vec3d forward = Vec3d.fromPolar(0, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90);
        double velX = 0;
        double velY = 0;
        double velZ = 0;

        if (rotate.isEnabled()) {
            BlockPos crossHairPos;
            Vec3d crossHairPosition;

            if (mc.crosshairTarget instanceof EntityHitResult) {
                crossHairPos = ((EntityHitResult) mc.crosshairTarget).getEntity().getBlockPos();
                Rotations.rotate(Rotations.getYaw(crossHairPos), Rotations.getPitch(crossHairPos), 0, null);
            } else {
                crossHairPosition = mc.crosshairTarget.getPos();
                crossHairPos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();

                if (!mc.world.getBlockState(crossHairPos).isAir()) {
                    Rotations.rotate(Rotations.getYaw(crossHairPosition), Rotations.getPitch(crossHairPosition), 0, null);
                }
            }
        }

        double s = 0.5;
        if (mc.options.sprintKey.isPressed()) s = 1;

        boolean a = false;
        if (this.forward) {
            velX += forward.x * s * speedValue;
            velZ += forward.z * s * speedValue;
            a = true;
        }
        if (this.backward) {
            velX -= forward.x * s * speedValue;
            velZ -= forward.z * s * speedValue;
            a = true;
        }

        boolean b = false;
        if (this.right) {
            velX += right.x * s * speedValue;
            velZ += right.z * s * speedValue;
            b = true;
        }
        if (this.left) {
            velX -= right.x * s * speedValue;
            velZ -= right.z * s * speedValue;
            b = true;
        }

        if (a && b) {
            double diagonal = 1 / Math.sqrt(2);
            velX *= diagonal;
            velZ *= diagonal;
        }

        if (this.up) {
            velY += s * speedValue;
        }
        if (this.down) {
            velY -= s * speedValue;
        }

        prevPos.set(pos);
        pos.set(pos.x + velX, pos.y + velY, pos.z + velZ);
    }

    @HaikuSubscribe
    public void onKey(KeyEvent event) {
	if (GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_F3) == GLFW.GLFW_PRESS) return;
	// if (checkGuiMove()) return;

	boolean cancel = true;

        if (mc.options.forwardKey.matchesKey(event.key, 0)) {
            forward = event.action != KeyAction.Release;
            mc.options.forwardKey.setPressed(false);
        }
        else if (mc.options.backKey.matchesKey(event.key, 0)) {
            backward = event.action != KeyAction.Release;
            mc.options.backKey.setPressed(false);
        }
        else if (mc.options.rightKey.matchesKey(event.key, 0)) {
            right = event.action != KeyAction.Release;
            mc.options.rightKey.setPressed(false);
        }
        else if (mc.options.leftKey.matchesKey(event.key, 0)) {
            left = event.action != KeyAction.Release;
            mc.options.leftKey.setPressed(false);
        }
        else if (mc.options.jumpKey.matchesKey(event.key, 0)) {
            up = event.action != KeyAction.Release;
            mc.options.jumpKey.setPressed(false);
        }
        else if (mc.options.sneakKey.matchesKey(event.key, 0)) {
            down = event.action != KeyAction.Release;
            mc.options.sneakKey.setPressed(false);
        }
        else {
            cancel = false;
        }

        if (cancel) event.cancel();
    }

    @HaikuSubscribe
    private void onMouseButton(MouseButtonEvent event) {
        // if (checkGuiMove()) return;

        boolean cancel = true;

        if (mc.options.forwardKey.matchesMouse(event.button)) {
            forward = event.action != KeyAction.Release;
            mc.options.forwardKey.setPressed(false);
        }
        else if (mc.options.backKey.matchesMouse(event.button)) {
            backward = event.action != KeyAction.Release;
            mc.options.backKey.setPressed(false);
        }
        else if (mc.options.rightKey.matchesMouse(event.button)) {
            right = event.action != KeyAction.Release;
            mc.options.rightKey.setPressed(false);
        }
        else if (mc.options.leftKey.matchesMouse(event.button)) {
            left = event.action != KeyAction.Release;
            mc.options.leftKey.setPressed(false);
        }
        else if (mc.options.jumpKey.matchesMouse(event.button)) {
            up = event.action != KeyAction.Release;
            mc.options.jumpKey.setPressed(false);
        }
        else if (mc.options.sneakKey.matchesMouse(event.button)) {
            down = event.action != KeyAction.Release;
            mc.options.sneakKey.setPressed(false);
        }
        else {
            cancel = false;
        }

        if (cancel) event.cancel();
    }

    // @EventHandler(priority = EventPriority.LOW)
    // private void onMouseScroll(MouseScrollEvent event) {
    //     if (speedScrollSensitivity.getValue() > 0) {
    //         speedValue += event.value * 0.25 * (speedScrollSensitivity.getValue() * speedValue);
    //         if (speedValue < 0.1) speedValue = 0.1;

    //         event.cancel();
    //     }
    // }

    // @EventHandler
    // private void onChunkOcclusion(ChunkOcclusionEvent event) {
    //     event.cancel();
    // }

    // @HaikuSubscribe
    // private void onDamage(DamageEvent event) {
    //     if (event.entity.getUuid() == null) return;
    //     if (!event.entity.getUuid().equals(mc.player.getUuid())) return;

    //     if (toggleOnDamage.isEnabled()) {
    //         toggle();
    //         info("Toggled off because you took damage.");
    //     }
    // }

    // @EventHandler
    // private void onGameLeft(GameLeftEvent event) {
    //     if (!toggleOnLog.isEnabled()) return;

    //     toggle();
    // }

    @HaikuSubscribe
    private void onPacketReceive(PacketEvent event)  {
        if (event.packet instanceof DeathMessageS2CPacket packet) {
            Entity entity = mc.world.getEntityById(packet.getEntityId());
            if (entity == mc.player && toggleOnDeath.isEnabled()) {
                toggle();
                HaikuLogger.info("Toggled off because you died.");
            }
        }
    }

    // private boolean checkGuiMove() {
    //     // TODO: This is very bad but you all can cope :cope:
    //     GUIMove guiMove = Modules.isEnabled().get(GUIMove.class);
    //     if (mc.currentScreen != null && !guiMove.isActive()) return true;
    //     return (mc.currentScreen != null && guiMove.isActive() && guiMove.skip());
    // }

    public void changeLookDirection(double deltaX, double deltaY) {
        prevYaw = yaw;
        prevPitch = pitch;

        yaw += deltaX;
        pitch += deltaY;

        pitch = MathHelper.clamp(pitch, -90, 90);
    }

    public boolean renderHands() {
        return !isEnabled() || renderHands.isEnabled();
    }

    public double getX(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.x, pos.x);
    }
    public double getY(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.y, pos.y);
    }
    public double getZ(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.z, pos.z);
    }

    public double getYaw(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevYaw, yaw);
    }
    public double getPitch(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPitch, pitch);
    }
}
