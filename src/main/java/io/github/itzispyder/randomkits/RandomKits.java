package io.github.itzispyder.randomkits;

import io.github.itzispyder.pdk.PDK;
import io.github.itzispyder.randomkits.commands.KitCommand;
import io.github.itzispyder.randomkits.events.PlayerEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class RandomKits extends JavaPlugin {

    public static final Logger logger = Bukkit.getLogger();

    @Override
    public void onEnable() {
        PDK.init(this);
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        new KitCommand().register();

        new PlayerEventListener().register();
    }

    @Override
    public void onDisable() {

    }
}
