package xyz.garslity093.realmcore.functions.deathbox.commands;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.garslity093.realmcore.PluginUtils;
import xyz.garslity093.realmcore.functions.deathbox.utils.DeathBoxUtils;

import java.util.ArrayList;

public final class DeathBoxCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = ((Player) commandSender);
            ArrayList<Location> locations = DeathBoxUtils.getAllDeathBoxLocations(player);
            if (locations.size() > 0) {
                for (Location location : locations) {
                    String world;
                    switch (location.getWorld().getName()) {
                        case "world":
                            world = "主世界";
                        case "world_nether":
                            world = "地狱";
                        case "world_the_end":
                            world = "末地";
                        default:
                            world = location.getWorld().getName();
                    }
                    int locX = location.getBlockX();
                    int locY = location.getBlockY();
                    int locZ = location.getBlockZ();
                    player.sendMessage(PluginUtils.transColor("&a位置: &f" + locX + "," + locY + "," + locZ + "&7| &a世界: &f" + world));
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 0F);
            } else {
                player.sendMessage(PluginUtils.transColor("&c你没有存在的物品盒."));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 0f);
            }
        } else {
            commandSender.sendMessage("非玩家不可以使用这个指令.");
        }
        return true;
    }
}
