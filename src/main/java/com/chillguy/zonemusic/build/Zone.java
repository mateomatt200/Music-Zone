package com.chillguy.zonemusic.build;

import org.bukkit.Location;
import org.bukkit.World;

public class Zone {

    private String name;
    private World world;
    private Location pos1;
    private Location pos2;
    private String music;
    private boolean disable_on_exit;


    public Zone(String name, World world, Location pos1, Location pos2, String music, boolean disable_on_exit) {
        this.name = name;
        this.world = world;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.music = music;
        this.disable_on_exit = disable_on_exit;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public String getMusic() {
        return music;
    }

    public boolean isDisable_on_exit() {
        return disable_on_exit;
    }

    public boolean contains(Location loc) {
        if (!loc.getWorld().equals(world)) return false;

        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        return loc.getX() >= minX && loc.getX() <= maxX &&
                loc.getY() >= minY && loc.getY() <= maxY &&
                loc.getZ() >= minZ && loc.getZ() <= maxZ;
    }
}
