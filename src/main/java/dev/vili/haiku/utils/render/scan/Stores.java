package dev.vili.haiku.utils.render.scan;



public class Stores {
    public static final SettingsStore SETTINGS = SettingsStore.getInstance();
    public static final BlockStore BLOCKS = BlockStore.getInstance();

    public static void reload() {
        SETTINGS.read();
        BLOCKS.read();
    }

    public static void write() {
        SETTINGS.write();
        BLOCKS.write();
    }
}

class SettingsStore {
    private static final SettingsStore INSTANCE = new SettingsStore();

    public static SettingsStore getInstance() {
        return INSTANCE;
    }

    public void read() {
        // implementation
    }

    public void write() {
        // implementation
    }
}

class BlockStore {
    private static final BlockStore INSTANCE = new BlockStore();

    public static BlockStore getInstance() {
        return INSTANCE;
    }

    public void read() {
        // implementation
    }

    public void write() {
        // implementation
    }
}