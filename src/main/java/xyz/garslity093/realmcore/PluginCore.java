package xyz.garslity093.realmcore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import xyz.garslity093.realmcore.functions.deathbox.DeathBoxListeners;
import xyz.garslity093.realmcore.functions.deathbox.commands.DeathBoxCommand;
import xyz.garslity093.realmcore.functions.digboard.DigBoardListeners;
import xyz.garslity093.realmcore.functions.motdchanger.MOTDChangerListeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class PluginCore extends JavaPlugin {
    private static Plugin plugin;
    private static File boxFile;
    private static FileConfiguration boxConfig;

    private static ArrayList<Material> deathBoxReplaceBlocks;

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

    public static FileConfiguration getDeathBoxConfig() {
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

    public static ArrayList<Material> getDeathBoxReplaceBlocks() {
        return deathBoxReplaceBlocks;
    }

    public static void loadDeathBoxMaterials() {
        deathBoxReplaceBlocks = PluginUtils.legalMaterialsFilter(PluginCore.getPlugin().getConfig().getStringList("deathBoxSettings.replaceBlocks"));
    }

    @Override
    public void onEnable() {
        plugin = this;
        boxFile = new File(getDataFolder(), "box-record.yml");
        if (!boxFile.exists()) {
            saveResource("box-record.yml", false);
        }
        boxConfig = YamlConfiguration.loadConfiguration(boxFile);
        getServer().getPluginManager().registerEvents(new PluginListeners(), this);
        getServer().getPluginManager().registerEvents(new DigBoardListeners(), this);
        getServer().getPluginManager().registerEvents(new DeathBoxListeners(), this);
        getServer().getPluginManager().registerEvents(new MOTDChangerListeners(), this);
        getServer().getPluginCommand("serverfunc").setExecutor(new PluginCommands());
        getServer().getPluginCommand("serverfunc").setTabCompleter(new PluginCommands());
        getServer().getPluginCommand("deathbox").setExecutor(new DeathBoxCommand());
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        initializeDigBoard();
        loadDeathBoxMaterials();
    }
}
