/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import org.lwjgl.glfw.GLFW;

public class BoatFly extends Module
{
    public final BooleanSetting changeForwardSpeed = new BooleanSetting("Change Forward Speed-BoatFlyHack", "Allows \\u00a7eForward Speed\\u00a7r to be changed, disables smooth acceleration.", true);
    public final NumberSetting speed = new NumberSetting("Speed-BoatFlyHack", "How fast to fly on boat.", .33, 0, 10, 0.01);
    public final NumberSetting upSpeed = new NumberSetting("upSpeed-BoatFlyHack", "How fast to upwards fly on boat.", .1, 0, 10, 0.01);
	
    public BoatFly()
	{
		super("BoatFly", "Allows you to fly just on a boat.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
		this.addSettings(changeForwardSpeed, speed, upSpeed);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
	}
	
	@Override
	public void onDisable()
	{
		if (mc.world == null || mc.player == null) return;
        mc.player.getAbilities().flying = false;
        mc.player.getAbilities().allowFlying = false;
        mc.player.getAbilities().setFlySpeed(0.05f);

        super.onDisable();
	}
	
	@HaikuSubscribe
    public void onTick(TickEvent event) {
        // check if riding
		if(!mc.player.hasVehicle())
        return;
    
        Entity vehicle = mc.player.getVehicle();
        Vec3d velocity = vehicle.getVelocity();
        
        // default motion
        double motionX = velocity.x;
        double motionY = 0;
        double motionZ = velocity.z;
        
        // up/down
        if(mc.options.jumpKey.isPressed())
            motionY = upSpeed.getValue();
        else if(mc.options.sprintKey.isPressed())
            motionY = velocity.y;
        
        // forward
        if(mc.options.forwardKey.isPressed() && changeForwardSpeed.isEnabled())
        {
            double boatFlySpeed = (int) speed.getValue();
            float yawRad = vehicle.getYaw() * MathHelper.RADIANS_PER_DEGREE;

            motionX = MathHelper.sin(-yawRad) * boatFlySpeed;
            motionZ = MathHelper.cos(yawRad) * boatFlySpeed;
        }
    
        // apply motion
        vehicle.setVelocity(motionX, motionY, motionZ);
    }
}
