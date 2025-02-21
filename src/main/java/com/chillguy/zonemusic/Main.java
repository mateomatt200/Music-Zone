package com.chillguy.zonemusic;

import com.chillguy.zonemusic.commands.MainCommand;
import com.chillguy.zonemusic.manager.ZoneManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin implements Listener {

    ZoneManager zoneManager;

    @Override
    public void onEnable() {


        this.zoneManager = new ZoneManager(this);

        Objects.requireNonNull(getCommand("zone")).setExecutor(new MainCommand(zoneManager));
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onJoinZone(PlayerMoveEvent event){
        zoneManager.checkPlayerInZone(event.getPlayer());
    }


    /*
    @EventHandler
    public void onshift(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        player.sendMessage("Reproduciendo musica");
        player.playSound(player.getLocation(), "francisco_sounds:claw_pull", SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    @EventHandler
    public void onjoin(BlockBreakEvent event){
        Player player = event.getPlayer();
        player.stopSound("francisco_sounds:claw_pull", SoundCategory.PLAYERS);
    }

     */
}
