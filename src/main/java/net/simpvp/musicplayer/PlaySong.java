package net.simpvp.musicplayer;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaySong implements Listener {

    private final JavaPlugin plugin;
    public static Map<SongPlayer, World> songs;

    public PlaySong(JavaPlugin plugin) {
        this.plugin = plugin;
        songs = new HashMap<>();
    }

    public void listSongs(CommandSender sender) {
        File songsFolder = new File(plugin.getDataFolder(), "songs");
        if (!songsFolder.exists() || !songsFolder.isDirectory()) {
            if (songsFolder.mkdir()) {
                plugin.getLogger().info("Songs folder created successfully!");
            } else {
                plugin.getLogger().warning("Failed to create songs folder!");
                return;
            }
        }
        File[] filesInSongsFolder = songsFolder.listFiles();
        if (filesInSongsFolder == null) {
            plugin.getLogger().warning("Could not find the songs folder");
            return;
        }
        if (filesInSongsFolder.length == 0) {
            plugin.getLogger().warning("There are no songs in the songs folder");
            return;
        }
        for (File file : filesInSongsFolder) {
            sender.sendMessage(file.toString());
        }
    }

    public File getSongFile(String fileName) {
        File songsFolder = new File(plugin.getDataFolder(), "songs");
        File[] filesInSongsFolder = songsFolder.listFiles();
        if (filesInSongsFolder == null) {
            plugin.getLogger().warning("Could not find the songs folder");
            return null;
        }
        for (File file : filesInSongsFolder) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }

    public ArrayList<Player> getNearbyPlayers(Location location, int distance) {
        ArrayList<Player> nearbyPlayers = new ArrayList<>();
        for (Player player: Bukkit.getServer().getOnlinePlayers()) {
            if (!(player.getWorld().equals(location.getWorld()))) {
                continue;
            }
            if (player.getLocation().distance(location) < distance && !player.isDead()) {
                nearbyPlayers.add(player);
            }
        }
        return nearbyPlayers;
    }

    public void startSong(String fileName, Location location, int distance) {
        Song song = NBSDecoder.parse(new File(String.valueOf(getSongFile(fileName))));
        RadioSongPlayer rsp = new RadioSongPlayer(song);
        for (Player p : getNearbyPlayers(location, distance)) {
            rsp.addPlayer(p);
        }
        rsp.setCategory(SoundCategory.RECORDS);
        rsp.setPlaying(true);
        songs.put(rsp, location.getWorld());
    }

    public void stopSongs() {
        for (Map.Entry<SongPlayer, World> entry : songs.entrySet()) {
            entry.getKey().destroy();
        }
        songs.clear();
    }
}
