package xyz.garslity093.realmcore;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class PluginCommands implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (strings.length >= 1) {
            if (strings[0].equalsIgnoreCase("reload")) {
                commandSender.sendMessage("重载成功.");
                PluginCore.getPlugin().reloadConfig();
                PluginCore.reloadBoxConfig();
                PluginCore.loadDeathBoxMaterials();
            } else if (strings[0].equalsIgnoreCase("debug")) {
                Player player = (Player) commandSender;
                commandSender.sendMessage(String.valueOf(player.getWorld().getMaxHeight()));
                Material blockTypeUnderBox = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()).getBlock().getType();
                player.sendMessage(String.valueOf(blockTypeUnderBox != Material.AIR));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        ArrayList<String> result = new ArrayList<>();
        if (strings.length == 1) {
            result.add("reload");
            result.add("debug");
        }
        return result;
    }
}
