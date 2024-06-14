package io.github.itzispyder.randomkits.data;

import io.github.itzispyder.pdk.utils.FileValidationUtils;
import io.github.itzispyder.pdk.utils.misc.Randomizer;
import io.github.itzispyder.randomkits.RandomKits;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class KitLoader {

    public static Kit load(String name) {
        try {
            File file = makeValidName(name);
            if (!file.exists())
                throw new IllegalArgumentException();

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            BukkitObjectInputStream bois = new BukkitObjectInputStream(ois);

            Kit kit = (Kit) bois.readObject();
            bois.close();
            ois.close();
            bis.close();
            fis.close();
            return kit;
        }
        catch (Exception ex) {
            RandomKits.logger.warning("failed to load kit '%s'".formatted(name));
            return null;
        }
    }

    public static Kit loadRandom() {
        try {
            File file = new File("plugins/RandomKits/kits/");
            if (!file.exists() || !file.isDirectory())
                return null;

            File[] subFiles = file.listFiles();
            if (subFiles == null || subFiles.length == 0)
                return null;

            Randomizer r = new Randomizer();
            File kitFile = r.getRandomElement(subFiles);
            String name = kitFile.getName().replaceAll("(.*/)|(\\..+)", "");
            return load(name);
        }
        catch (Exception ex) {
            RandomKits.logger.warning("failed to load random kit");
            return null;
        }
    }

    public static Kit loadRandom(int attempts) {
        Kit kit;
        for (int i = 0; i < attempts; i++)
            if ((kit = loadRandom()) != null)
                return kit;
        return null;
    }

    public static void save(String name, Kit kit) {
        try {
            File file = makeValidName(name);
            FileValidationUtils.validate(file);

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            BukkitObjectOutputStream boos = new BukkitObjectOutputStream(oos);

            boos.writeObject(kit);
            boos.flush();
            boos.close();
            oos.close();
            bos.close();
            fos.close();
        }
        catch (Exception ex) {
            RandomKits.logger.warning("kit '%s' failed to save".formatted(name));
        }
    }

    public static void delete(String name) {
        if (!makeValidName(name).delete())
            RandomKits.logger.warning("failed to delete kit '%s'".formatted(name));
    }

    private static File makeValidName(String name) {
        name = name.replaceAll("[^0-9A-Za-z_-]", "");
        return new File("plugins/RandomKits/kits/%s.kit".formatted(name));
    }

    public static List<String> listNames() {
        try {
            File file = new File("plugins/RandomKits/kits/");
            if (!file.exists() || !file.isDirectory())
                return new ArrayList<>();

            File[] subFiles = file.listFiles();
            if (subFiles == null || subFiles.length == 0)
                return new ArrayList<>();

            return new ArrayList<>(Arrays.stream(subFiles).filter(Objects::nonNull).map(f -> {
                return f.getName().replaceAll("(.*/)|(\\..+)", "");
            }).toList());
        }
        catch (Exception ex) {
            return new ArrayList<>();
        }
    }
}
