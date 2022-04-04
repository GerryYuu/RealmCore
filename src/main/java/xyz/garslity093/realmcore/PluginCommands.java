package xyz.garslity093.realmcore;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

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
                if (strings.length >= 2) {
                    if (strings[1].equalsIgnoreCase("getUnixTimestamp")) {
                        commandSender.sendMessage(String.valueOf(System.currentTimeMillis() / 1000));
                    } else {
                        commandSender.sendMessage("未知参数.");
                    }
                } else {
                    commandSender.sendMessage("参数不足.");
                }
            } else {
                commandSender.sendMessage("未知参数.");
            }
        } else {
            commandSender.sendMessage("参数不足.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        ArrayList<String> result = new ArrayList<>();
        if (strings.length >= 1) {
            if (strings[0].equalsIgnoreCase("debug")) {
                result.add("getUnixTimestamp");
            } else {
                result.add("reload");
                result.add("debug");
            }
        }
        return result;
    }
}
