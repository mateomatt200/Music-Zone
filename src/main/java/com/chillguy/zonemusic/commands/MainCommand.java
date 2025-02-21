package com.chillguy.zonemusic.commands;

import com.chillguy.zonemusic.build.Zone;
import com.chillguy.zonemusic.build.ZoneBuilder;
import com.chillguy.zonemusic.manager.ZoneManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MainCommand implements CommandExecutor {

    private ZoneManager zoneManager;
    private Map<Player, ZoneBuilder> zoneBuilders;

    public MainCommand(ZoneManager zoneManager) {
        this.zoneManager = zoneManager;
        this.zoneBuilders = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§cUsage: /zone <create|setpos1|setpos2|save>");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "create":
                if (args.length < 2) {
                    player.sendMessage("Usage: /zone create <name>");
                    return true;
                }
                String name = args[1];
                zoneBuilders.put(player, new ZoneBuilder(name, player.getWorld()));
                player.sendMessage("§9Zone " + name + " created. Use /zone setpos1 and /zone setpos2 to set the positions.");
                break;
            case "setpos1":
                if (!zoneBuilders.containsKey(player)) {
                    player.sendMessage("§cYou must create a zone first using /zone create <name>.");
                    return true;
                }
                ZoneBuilder builder = zoneBuilders.get(player);
                builder.setPos1(player.getLocation());
                player.sendMessage("§9Position 1 set.");
                break;
            case "setpos2":
                if (!zoneBuilders.containsKey(player)) {
                    player.sendMessage("§cYou must create a zone first using /zone create <name>.");
                    return true;
                }
                builder = zoneBuilders.get(player);
                builder.setPos2(player.getLocation());
                player.sendMessage("§9Position 2 set.");
                break;
            case "save":
                if (!zoneBuilders.containsKey(player)) {
                    player.sendMessage("§cYou must create a zone first using /zone create <name>.");
                    return true;
                }
                builder = zoneBuilders.get(player);
                Zone zone = builder.build();
                zoneManager.addZone(zone);
                zoneManager.saveZones();
                zoneBuilders.remove(player);
                player.sendMessage("§9Zone " + zone.getName() + " saved.");
                break;
            case "reload":
                zoneManager.reloadZones();
                player.sendMessage("§aPlugin Reload");
            default:
                player.sendMessage("Unknown subcommand. Usage: /zone <create|setpos1|setpos2|save>");
                break;
        }

        return true;
    }
}
