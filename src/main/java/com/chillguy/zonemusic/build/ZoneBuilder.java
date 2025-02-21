package com.chillguy.zonemusic.build;

import org.bukkit.Location;
import org.bukkit.World;

public class ZoneBuilder {
    private String name;
    private World world;
    private Location pos1;
    private Location pos2;

    public ZoneBuilder(String name, World world) {
        this.name = name;
        this.world = world;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Zone build() {
        return new Zone(name, world, pos1, pos2, "francisco_sounds:claw_pull", true);
    }
}
