package xyz.garslity093.serverfunc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public final class ServerUtils {
    public static ArrayList<UUID> getOnlinePlayers() {
        ArrayList<UUID> uuids = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            uuids.add(player.getUniqueId());
        }
        return uuids;
    }
}