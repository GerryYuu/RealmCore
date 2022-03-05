package xyz.garslity093.serverfunc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.io.IOException;

public final class Func extends JavaPlugin {
    private static Plugin plugin;
    private static File boxFile;
    private static FileConfiguration boxConfig;

    public static Plugin getPlugin() {
        return plugin;
    }

    public static void initializeDigBoard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
        if (scoreboard.getObjective("dig") == null) {
            Objective objective = scoreboard.registerNewObjective("dig", "dummy", "挖掘榜");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
    }

    public static File getBoxFile() {
        return boxFile;
    }

    public static FileConfiguration getBoxConfig() {
        return boxConfig;
    }

    public static void reloadBoxConfig() {
        try {
            boxConfig.load(boxFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveBoxRecord() {
        try {
            boxConfig.save(boxFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        plugin = this;
        boxFile = new File(getDataFolder(), "box-record.yml");
        if (!boxFile.exists()) {
            saveResource("box-record.yml", false);
        }
        boxConfig = YamlConfiguration.loadConfiguration(boxFile);
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getServer().getPluginCommand("serverfunc").setExecutor(new Command());
        getServer().getPluginCommand("serverfunc").setTabCompleter(new Command());
        initializeDigBoard();
    }
}
