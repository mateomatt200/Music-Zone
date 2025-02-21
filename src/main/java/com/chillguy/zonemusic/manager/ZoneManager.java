package com.chillguy.zonemusic.manager;

import com.chillguy.zonemusic.build.Zone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoneManager {

    private JavaPlugin plugin;
    private List<Zone> zones;
    private Map<Player, Zone> playerZoneMap;

    public ZoneManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.zones = new ArrayList<>();
        this.playerZoneMap = new HashMap<>();
        loadZones();
    }

    public void loadZones() {
        File pluginFolder = plugin.getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }

        File zonesFile = new File(plugin.getDataFolder(), "zones.yml");
        if (!zonesFile.exists()) {
            try {
                if (zonesFile.createNewFile()) {
                    plugin.saveResource("zones.yml", false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(zonesFile);

        if (config.getConfigurationSection("zones-music") != null) {
            for (String zoneName : config.getConfigurationSection("zones-music").getKeys(false)) {
                String pos1Str = config.getString("zones-music." + zoneName + ".pos1");
                String pos2Str = config.getString("zones-music." + zoneName + ".pos2");
                String world_name = config.getString("zones-music." + zoneName + ".world");
                String music = config.getString("zones-music."+zoneName+".music");
                boolean disable_on_exit = config.getBoolean("zones-music."+zoneName+".disable_on_exit");
                if(world_name != null) {
                    World world = Bukkit.getWorld(world_name);

                    String[] pos1Coords = pos1Str.split(",");
                    String[] pos2Coords = pos2Str.split(",");

                    Location pos1 = new Location(world,
                            Double.parseDouble(pos1Coords[0]),
                            Double.parseDouble(pos1Coords[1]),
                            Double.parseDouble(pos1Coords[2]));
                    Location pos2 = new Location(world,
                            Double.parseDouble(pos2Coords[0]),
                            Double.parseDouble(pos2Coords[1]),
                            Double.parseDouble(pos2Coords[2]));

                    zones.add(new Zone(zoneName, world, pos1, pos2, music, disable_on_exit));
                }
            }
        }
    }

    public void reloadZones() {
        zones.clear();
        loadZones();
        for (Player player : Bukkit.getOnlinePlayers()) {
            checkPlayerInZone(player);
        }
    }

    public void addZone(Zone zone) {
        zones.add(zone);
    }



    public void saveZones() {
        File zonesFile = new File(plugin.getDataFolder(), "zones.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(zonesFile);

        config.set("zones-music", null);  // Clear existing data
        for (Zone zone : zones) {
            String basePath = "zones-music." + zone.getName();
            config.set(basePath + ".world", zone.getWorld().getName());
            config.set(basePath + ".pos1", zone.getPos1().getX() + "," + zone.getPos1().getY() + "," + zone.getPos1().getZ());
            config.set(basePath + ".pos2", zone.getPos2().getX() + "," + zone.getPos2().getY() + "," + zone.getPos2().getZ());
            config.set(basePath + ".music", zone.getMusic());
            config.set(basePath + ".disable_on_exit", zone.isDisable_on_exit());
        }

        try {
            config.save(zonesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkPlayerInZone(Player player) {
        Location playerLoc = player.getLocation();
        Zone currentZone = playerZoneMap.get(player);
        Zone newZone = null;

        for (Zone zone : zones) {
            if (zone.contains(playerLoc)) {
                newZone = zone;
                break;
            }
        }

        if (newZone != null && currentZone == null) {
            player.sendMessage("You have entered zone: " + newZone.getName());
            player.playSound(player.getLocation(), newZone.getMusic(), SoundCategory.PLAYERS, 1.0f, 1.0f);
            playerZoneMap.put(player, newZone);
        } else if (newZone == null && currentZone != null) {
            // Player exited a zone
            player.sendMessage("You have exited zone: " + currentZone.getName());
            if (currentZone.isDisable_on_exit()) {
                player.stopSound(currentZone.getMusic(), SoundCategory.PLAYERS);
            }
            playerZoneMap.remove(player);
        } else if (newZone != null && !newZone.equals(currentZone)) {

            player.sendMessage("You have exited zone: " + currentZone.getName());
            player.sendMessage("You have entered zone: " + newZone.getName());
            if (currentZone.isDisable_on_exit()) {
                player.stopSound(currentZone.getMusic(), SoundCategory.PLAYERS);
            }
            player.playSound(player.getLocation(), newZone.getMusic(), SoundCategory.PLAYERS, 1.0f, 1.0f);
            playerZoneMap.put(player, newZone);
        }
    }
}
