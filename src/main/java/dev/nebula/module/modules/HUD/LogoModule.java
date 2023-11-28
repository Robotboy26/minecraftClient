package dev.nebula.module.modules.HUD;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.hud.HUDComponent;

import dev.nebula.gui.settings.BooleanSetting;
import dev.nebula.gui.settings.ColorSetting;
import dev.nebula.gui.settings.IntegerSetting;
import dev.nebula.module.Module;

public class LogoModule extends Module {
	private static LogoModule instance;
	private static final IntegerSetting rotation=new IntegerSetting("Image Rotation","rotation","How to rotate the image.",()->true,0,3,0);
	private static final BooleanSetting parity=new BooleanSetting("Flip Image","parity","Whether to flip the image or not.",()->true,false);
	private static final ColorSetting color=new ColorSetting("Logo Color","color","The color to modulate the logo with.",()->true,true,true,new Color(255,255,255,128),true);
	
	public LogoModule() {
		super("Logo","Module that displays the PanelStudio icon on HUD.",()->true,true);
		this.addSettings(rotation, parity, color);
		instance = this;
	}

	public static IFixedComponent getComponent (IInterface inter) {
		int image=inter.loadImage("icon.png");
		return new HUDComponent(()->"Logo",new Point(300,300),"logo") {
			@Override
			public void render (Context context) {
				super.render(context);
				context.getInterface().drawImage(context.getRect(),rotation.getValue(),parity.getValue(),image,color.getValue());
			}
			
			@Override
			public Dimension getSize (IInterface inter) {
				return new Dimension(141,61);
			}
		};
	}
	
	public static IToggleable getToggle() {
		return instance.isEnabled();
	}
}
