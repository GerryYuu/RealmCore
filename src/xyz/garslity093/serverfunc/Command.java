package xyz.garslity093.serverfunc;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public final class Command implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (strings.length >= 1) {
            if (strings[0].equalsIgnoreCase("reload")) {
                commandSender.sendMessage("重载成功.");
                Func.getPlugin().reloadConfig();
                Func.reloadBoxConfig();
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        ArrayList<String> result = new ArrayList<>();
        if (strings.length == 1) {
            result.add("reload");
        }
        return result;
    }
}