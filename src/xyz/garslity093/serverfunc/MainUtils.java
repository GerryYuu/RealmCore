package xyz.garslity093.serverfunc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MainUtils {
    public static ArrayList<Material> legalMaterialsFilter(List<String> materials) {
        ArrayList<String> mats = (ArrayList<String>) materials;
        ArrayList<Material> result = new ArrayList<>();
        for (String s : materials) {
            if (Material.getMaterial(s, true) != null) {
                result.add(Material.getMaterial(s, false));
            } else {
                Func.getPlugin().getLogger().info(s + " in config is not a legal material!");
            }
        }
        return result;
    }

    public static ArrayList<UUID> getOnlinePlayers() {
        ArrayList<UUID> uuids = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            uuids.add(player.getUniqueId());
        }
        return uuids;
    }
}
