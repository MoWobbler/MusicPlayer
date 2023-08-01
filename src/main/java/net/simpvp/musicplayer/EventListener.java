package net.simpvp.musicplayer;

import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.Map;

public class EventListener implements Listener {

    @EventHandler
    public void onSongEnd(SongEndEvent e){
        SongPlayer sp = e.getSongPlayer();
        PlaySong.songs.remove(sp);
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent e) {
        for (Map.Entry<SongPlayer, World> entry : PlaySong.songs.entrySet()) {
            if (!e.getPlayer().getWorld().getName().equals(entry.getValue().getName())) {
                entry.getKey().removePlayer(e.getPlayer());
            }
        }
    }
}
