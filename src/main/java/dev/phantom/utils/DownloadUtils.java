package dev.phantom.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

import net.fabricmc.api.ModInitializer;

public class DownloadUtils {
    static boolean initiated = false;

    public static void init(String modfolder) {
        initiated = true;
        String modsFolder = modfolder;
        Path currentPath = Paths.get("");
        String containingFolderName = currentPath.toAbsolutePath().getFileName().toString();
        if (containingFolderName.equals("run")) {
            modsFolder = modfolder;
        } else if (containingFolderName.equals("minecraft")) {
            modsFolder = modfolder;
        }

        if (!new File(modsFolder).exists()) {
            new File(modsFolder).mkdir();
        }
        Path folderPath = Paths.get(modfolder);
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
        Path base = Paths.get("");
        Set<PosixFilePermission> pem = PosixFilePermissions.fromString("rwxr-xr-x");
        try {
            Files.setPosixFilePermissions(base, pem);
            //HaikuLogger.info("Permissions set successfully for folder: " + base);
        } catch (Exception e) {
            //HaikuLogger.info("Error while setting permissions for folder: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            Files.setPosixFilePermissions(folderPath, perms);
            //HaikuLogger.info("Permissions set successfully for folder: " + folderPath);
        } catch (Exception e) {
            //HaikuLogger.info("Error while setting permissions for folder: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void downloadFromFile(String modsFolder, String fileName, String fileUrl) {
        if (initiated == false) {
            init(modsFolder);
        }
        // this loads all the mods from the mods.txt file
        // might not even need this because if I was better I could just use the gradle build include to do this too and it would be better but that fine
        // first download the mods.txt file from the github if it does not exist
        if (!new File("mods.txt").exists()) {
            getMod("mods.txt", "https://raw.githubusercontent.com/Robotboy26/minecraftClient/master/cloudFiles//mods.txt", ".");
        }
        try (BufferedReader br = new BufferedReader(new FileReader("mods.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String url = line.split("|")[0];
                String group = line.split("|")[1];
                String[] parts = url.split("/");
                if (group == "performance") {
                    try {
                        getMod(parts[parts.length - 1], line, modsFolder);
                    } catch (Exception e) {
                        getMod(line, line, modsFolder);
                    }
                } else {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //HaikuLogger.logger.info("Loaded mods!");
        //HaikuLogger.logger.info("If this is your first time running haiku, please restart your game!");
    }

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
        //HaikuLogger.logger.info("Downloaded mod!" + modName);
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

    public static void getMod(String modName, String modUrl, String modsFolder) {
        if (!new File(modsFolder, modName).exists()) {
        } else {
            //HaikuLogger.logger.info("Already exists not downloading again! " + modName);
        }
    }

    public static void getShader() {
        if (new File("shaderpacks").exists()) {
            String shaderUrl = "https://www.mediafire.com/file/stwyz8u89eivvq6/kuda-shaders-v6.5.26.zip/file";
            String[] shaderUrlPart = shaderUrl.split("/");
            getMod(shaderUrlPart[shaderUrlPart.length - 2], shaderUrl, "shaderpacks");
        }
    }
}
