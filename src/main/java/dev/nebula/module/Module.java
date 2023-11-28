package dev.nebula.module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IModule;
import com.lukflug.panelstudio.setting.ISetting;

import dev.nebula.Nebula;
import dev.nebula.gui.settings.Setting;
import dev.nebula.utils.NebulaLogger;
import net.minecraft.util.Formatting;

public class Module implements IModule {
	public final String displayName, description;
	public final IBoolean visible;
	public final List<Setting<?>> settings = new ArrayList<Setting<?>>();
	public final boolean toggleable;
	private boolean enabled = false;
	
	public Module (String displayName, String description, IBoolean visible, boolean toggleable) {
		this.displayName = displayName;
		this.description = description;
		this.visible = visible;
		this.toggleable = toggleable;
	}

	/**
     * Called when the module is enabled.
     */
    public void onEnable() {
        Nebula.getInstance().getEventBus().register(this);
        //Nebula.getInstance().getConfigManager().save();

        NebulaLogger.info(Formatting.GREEN + "Enabled " + this.displayName + "!");
    }

    /**
     * Called when the module is disabled.
     */
    public void onDisable() {
        Nebula.getInstance().getEventBus().unregister(this);
        //Nebula.getInstance().getConfigManager().save();

        NebulaLogger.info(Formatting.RED + "Disabled " + this.displayName + "!");
    }

	public void addSettings(Object... settings) {
		for (Object setting : settings) {
			this.settings.add((Setting<?>) setting);
		}
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public IBoolean isVisible() {
		return visible;
	}

	@Override
	public IToggleable isEnabled() {
		if (!toggleable) return null;
		return new IToggleable() {
			@Override
			public boolean isOn() {
				return enabled;
			}

			@Override
			public void toggle() {
				enabled =! enabled;
			}
		};
	}

	@Override
	public Stream<ISetting<?>> getSettings() {
		return settings.stream().filter(setting->setting instanceof ISetting).sorted((a,b)->a.displayName.compareTo(b.displayName)).map(setting->(ISetting<?>)setting);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			onEnable();
		} else {
			onDisable();
		}
	}
}
