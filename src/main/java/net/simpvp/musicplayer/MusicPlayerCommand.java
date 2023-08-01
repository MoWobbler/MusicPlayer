package net.simpvp.musicplayer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MusicPlayerCommand implements CommandExecutor {

    PlaySong playSong;

    public MusicPlayerCommand(JavaPlugin plugin) {
        this.playSong = new PlaySong(plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            /* If non-op */
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("stop")) {
                playSong.stopSongs();
                sender.sendMessage(ChatColor.GREEN + "Stopping songs");
            } else if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(ChatColor.GREEN + "Listing songs");
                playSong.listSongs(sender);
            } else {
                sender.sendMessage((ChatColor.RED + "Correct usage: /musicplayer list or /musicplayer stop"));
            }
            return true;
        }

        if (args.length == 2 || args.length == 3) {
            if (!args[0].equalsIgnoreCase("play")) {
                sender.sendMessage((ChatColor.RED + "Correct usage: /musicplayer play <songname> [distance]"));
                return true;
            }

            int parsedInteger = 100;
            if (args.length == 3) {
                try {
                    parsedInteger = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    parsedInteger = -1;
                }
            }

            if (parsedInteger <= 0) {
                sender.sendMessage((ChatColor.RED + "Invalid integer for distance argument: " + args[2]));
                return true;
            }

            Location location = null;
            if (sender instanceof Player) {
                Player player = (Player) sender;

                location = player.getLocation();
            } else if (sender instanceof BlockCommandSender) {
                BlockCommandSender blockCommandSender = (BlockCommandSender) sender;
                Block block = blockCommandSender.getBlock();
                location = block.getLocation();
            }

            if (location == null) {
                sender.sendMessage(ChatColor.RED + "The command can not be run from console.");
                return true;
            }

            if (playSong.getSongFile(args[1]) != null) {
                playSong.startSong(args[1], location, parsedInteger);
            } else {
                sender.sendMessage(ChatColor.RED + "Could not find a song with the name: " + args[1]);
            }
        }
        return true;
    }
}
