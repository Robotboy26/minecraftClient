package dev.vili.haiku.module.modules.render.xray.config;

import java.util.List;
import java.util.stream.Stream;

import dev.vili.haiku.module.modules.render.xray.color.EntityTypeIcon;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public class SyncedEntityTypeList extends SyncedRegistryList<EntityType<?>> {

    private SyncedEntityTypeList(SyncedRegistryList<EntityType<?>> other) {
        super(other);
    }

    public SyncedEntityTypeList() {
        super(Registries.ENTITY_TYPE);
    }

    public SyncedEntityTypeList(EntityType<?>... objects) {
        super(objects, Registries.ENTITY_TYPE);
    }

    public SyncedEntityTypeList(List<EntityType<?>> objects) {
        super(objects, Registries.ENTITY_TYPE);
    }

    public Stream<ItemStack> getIcons() {
        return getObjects().stream().map(EntityTypeIcon::getIcon);
    }

    @Override
    public SyncedEntityTypeList clone() {
        return new SyncedEntityTypeList(this);
    }
}