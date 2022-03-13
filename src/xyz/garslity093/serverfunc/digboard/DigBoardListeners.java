package xyz.garslity093.serverfunc.digboard;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scoreboard.Score;

public class DigBoardListeners implements org.bukkit.event.Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Score score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("dig").getScore(event.getPlayer());
        if (score.isScoreSet() == true) {
            score.setScore(score.getScore() + 1);
        } else {
            score.setScore(1);
        }
    }
}
