package io.github.itzispyder.randomkits.events;

import io.github.itzispyder.pdk.events.CustomListener;
import io.github.itzispyder.pdk.utils.SchedulerUtils;
import io.github.itzispyder.randomkits.data.Kit;
import io.github.itzispyder.randomkits.data.KitLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerEventListener implements CustomListener {

    @EventHandler
    public void onGameJoin(PlayerJoinEvent e) {
        if (!getConfig().getBoolean("plugin.triggers.game-join"))
            return;

        Player p = e.getPlayer();
        Kit kit = KitLoader.loadRandom(3);

        if (kit != null)
            p.getInventory().setContents(kit.getContents());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (!getConfig().getBoolean("plugin.triggers.player-death"))
            return;

        Player p = e.getPlayer();
        Kit kit = KitLoader.loadRandom(3);

        if (kit != null)
            SchedulerUtils.later(20, () -> p.getInventory().setContents(kit.getContents()));
    }
}
