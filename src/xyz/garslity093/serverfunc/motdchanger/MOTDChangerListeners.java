package xyz.garslity093.serverfunc.motdchanger;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import xyz.garslity093.serverfunc.Func;

public class MOTDChangerListeners implements Listener {
    @EventHandler
    public void onPlayerPing(ServerListPingEvent event) {
        event.setMotd(ChatColor.translateAlternateColorCodes('&', Func.getPlugin().getConfig().getString("motdChangerSettings.line1") + "\n" + Func.getPlugin().getConfig().getString("motdChangerSettings.line2")));
    }
}
