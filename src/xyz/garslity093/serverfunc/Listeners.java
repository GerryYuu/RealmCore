package xyz.garslity093.serverfunc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;

public final class Listeners implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Score score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("dig").getScore(event.getPlayer());
        if (score.isScoreSet() == true) {
            score.setScore(score.getScore() + 1);
        } else {
            score.setScore(1);
        }
    }

    @EventHandler
    public void onBlockBreak1(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.OAK_WALL_SIGN) {
            for (String key : Func.getBoxConfig().getConfigurationSection("box").getKeys(false)) {
                Location location = Func.getBoxConfig().getLocation("box." + key + ".sign_loc");
                if (event.getBlock().getLocation().getBlockX() == location.getBlockX() &&
                        event.getBlock().getLocation().getBlockY() == location.getBlockY() &&
                        event.getBlock().getLocation().getBlockZ() == location.getBlockZ() &&
                        event.getBlock().getLocation().getWorld() == location.getWorld()) {
                    event.setCancelled(true);
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 0F);
                    event.getPlayer().sendTitle(" ", "§c你不可以破坏这个牌子.", 0, 70, 0);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak2(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.CHEST) {
            for (String key : Func.getBoxConfig().getConfigurationSection("box").getKeys(false)) {
                Location location = Func.getBoxConfig().getLocation("box." + key + ".chest_loc");
                if (event.getBlock().getLocation().getBlockX() == location.getBlockX() &&
                        event.getBlock().getLocation().getBlockY() == location.getBlockY() &&
                        event.getBlock().getLocation().getBlockZ() == location.getBlockZ() &&
                        event.getBlock().getLocation().getWorld() == location.getWorld()) {
                    if (!Utils.isAnyOneOpeningBox(Func.getBoxConfig().getString("box." + key + ".chest_id"))) {
                        new Location(event.getBlock().getWorld(), event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockY(), event.getBlock().getLocation().getBlockZ() - 1).getBlock().setType(Material.AIR);
                        ArrayList<ItemStack> itemStacks = (ArrayList<ItemStack>) Func.getBoxConfig().getList("box." + key + ".items");
                        for (ItemStack itemStack : itemStacks) {
                            event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), itemStack);
                        }
                        Func.getBoxConfig().set("box." + event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ(), null);
                        Func.saveBoxRecord();
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                    } else {
                        event.getPlayer().sendTitle(" ", "§c在有人使用这个箱子的时候不可以摧毁.", 0, 70, 0);
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 0f);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        if (!event.getKeepInventory()) {
            if (!player.getInventory().isEmpty()) {
                if (player.getLocation().getBlockY() >= player.getWorld().getMinHeight() && player.getLocation().getBlockY() < player.getWorld().getMaxHeight()) {
                    Utils.addBox(Utils.getNewChestLoc(player), Utils.getNewSignLoc(player), player);
                    event.getDrops().clear();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType().equals(Material.CHEST)) {
                for (String key : Func.getBoxConfig().getConfigurationSection("box").getKeys(false)) {
                    Location location = Func.getBoxConfig().getLocation("box." + key + ".chest_loc");
                    if (event.getClickedBlock().getLocation().getBlockX() == location.getBlockX() &&
                            event.getClickedBlock().getLocation().getBlockY() == location.getBlockY() &&
                            event.getClickedBlock().getLocation().getBlockZ() == location.getBlockZ() &&
                            event.getClickedBlock().getLocation().getWorld() == location.getWorld()) {
                        event.setCancelled(true);
                        Inventory inventory = Bukkit.createInventory(new BoxInventoryHolder(Func.getBoxConfig().getString("box." + key + ".chest_id")), 54);
                        if (!Utils.isAnyOneOpeningBox(Func.getBoxConfig().getString("box." + key + ".chest_id"))) {
                            ArrayList<ItemStack> itemStacks = (ArrayList<ItemStack>) Func.getBoxConfig().getList("box." + key + ".items");
                            for (ItemStack itemStack : itemStacks) {
                                inventory.addItem(itemStack);
                            }
                            event.getPlayer().openInventory(inventory);
                            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1F, 0F);
                        } else {
                            event.getPlayer().sendTitle(" ", "§c这个箱子已经有人在用了.", 0, 70, 0);
                            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 0f);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        ArrayList<Block> blocks = new ArrayList<>();
        for (Block block : event.blockList()) {
            for (String key : Func.getBoxConfig().getConfigurationSection("box").getKeys(false)) {
                Location locationChest = Func.getBoxConfig().getLocation("box." + key + ".chest_loc");
                Location locationSign = Func.getBoxConfig().getLocation("box." + key + ".sign_loc");
                if (block.getLocation().equals(locationChest) || block.getLocation().equals(locationSign)) {
                    blocks.add(block);
                }
            }
        }
        for (Block block : blocks) {
            event.blockList().remove(block);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        ArrayList<Block> blocks = new ArrayList<>();
        for (Block block : event.blockList()) {
            for (String key : Func.getBoxConfig().getConfigurationSection("box").getKeys(false)) {
                Location locationChest = Func.getBoxConfig().getLocation("box." + key + ".chest_loc");
                Location locationSign = Func.getBoxConfig().getLocation("box." + key + ".sign_loc");
                if (block.getLocation().equals(locationChest) || block.getLocation().equals(locationSign)) {
                    blocks.add(block);
                }
            }
        }
        for (Block block : blocks) {
            event.blockList().remove(block);
        }
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof BoxInventoryHolder) {
            BoxInventoryHolder holder = (BoxInventoryHolder) event.getInventory().getHolder();
            for (String key : Func.getBoxConfig().getConfigurationSection("box").getKeys(false)) {
                if (Func.getBoxConfig().get("box." + key + ".chest_id").equals(holder.getChestID())) {
                    ArrayList<ItemStack> itemStacks = new ArrayList<>();
                    for (ItemStack itemStack : event.getInventory().getContents()) {
                        if (itemStack != null) {
                            itemStacks.add(itemStack);
                        }
                    }
                    Func.getBoxConfig().set("box." + key + ".items", itemStacks);
                    if (event.getPlayer() instanceof Player) {
                        Player player = (Player) event.getPlayer();
                        if (itemStacks.isEmpty()) {
                            Func.getBoxConfig().getLocation("box." + key + ".sign_loc").getBlock().setType(Material.AIR);
                            Func.getBoxConfig().getLocation("box." + key + ".chest_loc").getBlock().setType(Material.AIR);
                            Func.getBoxConfig().set("box." + key, null);
                            Func.saveBoxRecord();
                            player.sendTitle(" ", "§6这个箱子已自动摧毁.", 0, 70, 0);
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1F, 0F);
                    }
                }
            }
        }
    }
}

