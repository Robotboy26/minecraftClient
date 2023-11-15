package dev.vili.haiku.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.ServiceLoader;

import net.fabricmc.api.ModInitializer;

public class DownloadUtils {
    public static void downloadMod(String url, String modsFolder, String modName) throws IOException {
        String destPath = String.format("%s/%s", modsFolder, modName);
        File dest = new File(destPath);
        URL modUrl = new URL(url);
        URLConnection conn = modUrl.openConnection();
        try (InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(dest)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
                }   
            }
        HaikuLogger.logger.info("Downloaded mod!" + modName);
    }
    
    public void loadMod() {
        Path modsFolder;
        if (System.getProperty("user.dir").endsWith("mods")) {
            modsFolder = Path.of("");
        } else {
            modsFolder = Path.of("mods");
        }
        Path modFilePath = modsFolder.resolve("mod.jar");

        if (modFilePath.toFile().exists()) {
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[] { modFilePath.toUri().toURL() }, getClass().getClassLoader());

                ServiceLoader<ModInitializer> initializerLoader = ServiceLoader.load(ModInitializer.class, classLoader);
                Iterator<ModInitializer> iterator = initializerLoader.iterator();
                if (iterator.hasNext()) {
                    ModInitializer modInitializer = iterator.next();
                    modInitializer.onInitialize();
                } else {
                    throw new RuntimeException("Failed to find mod initializer");
                }

                classLoader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
