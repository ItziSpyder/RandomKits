package io.github.itzispyder.randomkits.commands;

import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import io.github.itzispyder.randomkits.data.Kit;
import io.github.itzispyder.randomkits.data.KitLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandRegistry(value = "kit", usage = "/kit <action> <name>", playersOnly = true, printStackTrace = true)
public class KitCommand implements CustomCommand {

    @Override
    public void dispatchCommand(CommandSender sender, Args args) {
        Player p = (Player) sender;
        int len = args.getSize();

        if (len == 0) {
            error(sender, "Incomplete command");
            return;
        }

        switch (args.get(0).toString()) {
            case "load" -> {
                if (len == 1) {
                    error(sender, "Please provide a kit name");
                    return;
                }

                String name = args.get(1).toString().replaceAll("[^0-9A-Za-z_-]", "");
                Kit kit = KitLoader.load(name);
                if (kit == null) {
                    error(sender, "Kit '%s' failed to load".formatted(name));
                    return;
                }

                p.getInventory().setContents(kit.getContents());
                info(sender, "Loaded kit &7%s".formatted(name));
            }
            case "loadall" -> {
                if (len == 1) {
                    error(sender, "Please provide a kit name");
                    return;
                }

                String name = args.get(1).toString().replaceAll("[^0-9A-Za-z_-]", "");
                Kit kit = KitLoader.load(name);
                if (kit == null) {
                    error(sender, "Kit '%s' failed to load".formatted(name));
                    return;
                }

                Bukkit.getOnlinePlayers().forEach(online -> online.getInventory().setContents(kit.getContents()));
                info(sender, "Loaded kit &7%s&r for all players".formatted(name));
            }
            case "loadrandom" -> {
                Kit kit = KitLoader.loadRandom(3);
                if (kit == null) {
                    error(sender, "Kit failed to load");
                    return;
                }

                p.getInventory().setContents(kit.getContents());
                info(sender, "Loaded random kit");
            }
            case "randomall" -> {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    Kit kit = KitLoader.loadRandom(3);
                    if (kit == null) {
                        error(sender, "Kit failed to load for &7%s".formatted(online.getName()));
                        return;
                    }

                    online.getInventory().setContents(kit.getContents());
                }
                info(sender, "Loaded random kit for all players");
            }
            case "save" -> {
                if (len == 1) {
                    error(sender, "Please provide a kit name");
                    return;
                }

                String name = args.get(1).toString().replaceAll("[^0-9A-Za-z_-]", "");
                Kit kit = new Kit(p.getInventory().getContents());
                KitLoader.save(name, kit);
                info(sender, "Saved inventory to kit &7%s".formatted(name));
            }
            case "delete" -> {
                if (len == 1) {
                    error(sender, "Please provide a kit name");
                    return;
                }

                String name = args.get(1).toString().replaceAll("[^0-9A-Za-z_-]", "");
                KitLoader.delete(name);
                info(sender, "Deleted kit &7%s".formatted(name));
            }
            case "list" -> {
                List<String> names = KitLoader.listNames();
                info(sender, "There are a total of &7%s&r kits: &7%s".formatted(names.size(), names));
            }
            default -> error(sender, "Unknown action");
        }
    }

    @Override
    public void dispatchCompletions(CompletionBuilder b) {
        b.then(b.arg("save")
                .then(b.arg(color("&8kitName"))));

        b.then(b.arg("loadrandom", "list", "randomall"));

        b.then(b.arg("load", "delete", "loadall")
                .then(b.arg(KitLoader.listNames())));
    }
}
