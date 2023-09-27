package dev.vili.haiku.module.modules.movement;

import net.minecraft.util.math.Vec3d;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.ModeSetting;

import org.lwjgl.glfw.GLFW;

public class Speed extends Module
{
    public final ModeSetting mode = new ModeSetting("Mode-SpeedHack", "um.", "Vanilla", "Vanilla", "EndCrystal.me", "NoCheat");
    public final BooleanSetting noCheat = new BooleanSetting("noCheat-SpeedHack", "Allows \\u00a7eForward Speed\\u00a7r to be changed, disables smooth acceleration.", true);
    public final NumberSetting speed = new NumberSetting("Speed-SpeedHack", "um.", 1, 0.01, 100, 1);
    public final NumberSetting noCheatSpeed = new NumberSetting("NoCheatSpeed-SpeedHack", "um.", .489, 0.01, 1, 0.01);
    public final NumberSetting timer1num = new NumberSetting("Timer1-SpeedHack", "um.", 13.5, 0.01, 40, 0.01);
    public final NumberSetting timer2num = new NumberSetting("Timer2-SpeedHack", "um.", 3.5, 0.01, 40, 0.01);


    double timer = 5;
    double timer2 = 5;

    public Speed()
	{
		super("Speed", "Allows you to become sonic.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
		this.addSettings(noCheat, speed, noCheatSpeed, timer1num, timer2num);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
	}
	
	@Override
	public void onDisable()
	{
        super.onDisable();
	}
	
	@HaikuSubscribe
    public void onTick(TickEvent event) throws InterruptedException {
        // return if sneaking or not walking
		if(mc.player.isSneaking()
        || mc.player.forwardSpeed == 0 && mc.player.sidewaysSpeed == 0)
        return;
    
    // activate sprint if walking forward
    if(mc.player.forwardSpeed > 0 && !mc.player.horizontalCollision)
        mc.player.setSprinting(true);
    
    // activate mini jump if on ground
    if(!mc.player.isOnGround())
        return;
    
    Vec3d v = mc.player.getVelocity();
    mc.player.setVelocity(v.x * 1.8, v.y + 0.1, v.z * 1.8);
    
    v = mc.player.getVelocity();
    double currentSpeed = Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.z, 2));
    
    // limit speed to highest value that works on NoCheat+ version
    // 3.13.0-BETA-sMD5NET-b878
    // UPDATE: Patched in NoCheat+ version 3.13.2-SNAPSHOT-sMD5NET-b888
    float maxSpeed = 0;
    if(timer2 > 0) {
        timer2--;
        if(noCheat.isEnabled() && timer > 0) {
                maxSpeed = (float) noCheatSpeed.getValue();
                timer--;
        } else if(timer > 0) {
                maxSpeed = speed.floatValue() * 100;
        }
    } else {
        maxSpeed = (float) currentSpeed;
    }
    
    if(timer < 0)
    {
        timer = timer1num.getValue();
        timer2 = timer2num.getValue();
        maxSpeed = (float) currentSpeed;
    } else {
        timer--;
    }

    if(currentSpeed > maxSpeed)
        mc.player.setVelocity(v.x / currentSpeed * maxSpeed, v.y,
            v.z / currentSpeed * maxSpeed);
    }
}