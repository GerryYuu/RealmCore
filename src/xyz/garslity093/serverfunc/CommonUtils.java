package xyz.garslity093.serverfunc;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public final class CommonUtils {
    public static ArrayList<Material> legalMaterialsFilter(List<String> materials) {
        ArrayList<String> mats = (ArrayList<String>) materials;
        ArrayList<Material> result = new ArrayList<>();
        for (String s : materials) {
            if (Material.getMaterial(s, true) != null) {
                result.add(Material.getMaterial(s, true));
            } else {
                Func.getPlugin().getLogger().info(s + " in config is not a legal material!");
            }
        }
        return result;
    }
}
