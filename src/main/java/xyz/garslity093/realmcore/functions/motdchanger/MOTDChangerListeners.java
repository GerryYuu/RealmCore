package xyz.garslity093.realmcore.functions.motdchanger;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import xyz.garslity093.realmcore.PluginCore;

public final class MOTDChangerListeners implements Listener {
    @EventHandler
    public void onPlayerPing(ServerListPingEvent event) {
        event.setMotd(ChatColor.translateAlternateColorCodes('&', PluginCore.getPlugin().getConfig().getString("motdChangerSettings.line1") + "\n" + PluginCore.getPlugin().getConfig().getString("motdChangerSettings.line2")));
    }
}
