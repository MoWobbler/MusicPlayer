package net.simpvp.musicplayer;

import org.bukkit.plugin.java.JavaPlugin;

public final class MusicPlayer extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getCommand("musicplayer").setExecutor(new MusicPlayerCommand(this));
        getServer().getPluginManager().registerEvents(new EventListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
