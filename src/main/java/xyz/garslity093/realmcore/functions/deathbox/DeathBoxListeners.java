package xyz.garslity093.realmcore.functions.deathbox;

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
import xyz.garslity093.realmcore.PluginCore;
import xyz.garslity093.realmcore.functions.deathbox.utils.DeathBoxUtils;

import java.util.ArrayList;

public final class DeathBoxListeners implements Listener {
    @EventHandler
    public void onBlockBreak1(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.OAK_WALL_SIGN) {
            for (String key : PluginCore.getDeathBoxConfig().getConfigurationSection("box").getKeys(false)) {
                Location location = PluginCore.getDeathBoxConfig().getLocation("box." + key + ".sign_loc");
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
            for (String key : PluginCore.getDeathBoxConfig().getConfigurationSection("box").getKeys(false)) {
                Location location = PluginCore.getDeathBoxConfig().getLocation("box." + key + ".chest_loc");
                if (event.getBlock().getLocation().getBlockX() == location.getBlockX() &&
                        event.getBlock().getLocation().getBlockY() == location.getBlockY() &&
                        event.getBlock().getLocation().getBlockZ() == location.getBlockZ() &&
                        event.getBlock().getLocation().getWorld() == location.getWorld()) {
                    if (!DeathBoxUtils.isAnyoneUsingDeathBox(PluginCore.getDeathBoxConfig().getString("box." + key + ".chest_id"))) {
                        new Location(event.getBlock().getWorld(), event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockY(), event.getBlock().getLocation().getBlockZ() - 1).getBlock().setType(Material.AIR);
                        ArrayList<ItemStack> itemStacks = (ArrayList<ItemStack>) PluginCore.getDeathBoxConfig().getList("box." + key + ".items");
                        for (ItemStack itemStack : itemStacks) {
                            event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), itemStack);
                        }
                        PluginCore.getDeathBoxConfig().set("box." + event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ(), null);
                        PluginCore.saveBoxRecord();
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
                if (player.getLocation().getBlockY() >= player.getWorld().getMinHeight() &&
                        player.getLocation().getBlockY() < player.getWorld().getMaxHeight()) {
                    DeathBoxUtils.addBox(DeathBoxUtils.getNewChestLoc(player.getLocation()), player);
                    event.getDrops().clear();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType().equals(Material.CHEST)) {
                for (String key : PluginCore.getDeathBoxConfig().getConfigurationSection("box").getKeys(false)) {
                    Location location = PluginCore.getDeathBoxConfig().getLocation("box." + key + ".chest_loc");
                    if (event.getClickedBlock().getLocation().getBlockX() == location.getBlockX() &&
                            event.getClickedBlock().getLocation().getBlockY() == location.getBlockY() &&
                            event.getClickedBlock().getLocation().getBlockZ() == location.getBlockZ() &&
                            event.getClickedBlock().getLocation().getWorld() == location.getWorld()) {
                        event.setCancelled(true);
                        Inventory inventory = Bukkit.createInventory(new DeathBoxInventoryHolder(PluginCore.getDeathBoxConfig().getString("box." + key + ".chest_id")), 54);
                        if (!DeathBoxUtils.isAnyoneUsingDeathBox(PluginCore.getDeathBoxConfig().getString("box." + key + ".chest_id"))) {
                            ArrayList<ItemStack> itemStacks = (ArrayList<ItemStack>) PluginCore.getDeathBoxConfig().getList("box." + key + ".items");
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
            for (String key : PluginCore.getDeathBoxConfig().getConfigurationSection("box").getKeys(false)) {
                Location locationChest = PluginCore.getDeathBoxConfig().getLocation("box." + key + ".chest_loc");
                Location locationSign = PluginCore.getDeathBoxConfig().getLocation("box." + key + ".sign_loc");
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
            for (String key : PluginCore.getDeathBoxConfig().getConfigurationSection("box").getKeys(false)) {
                Location locationChest = PluginCore.getDeathBoxConfig().getLocation("box." + key + ".chest_loc");
                Location locationSign = PluginCore.getDeathBoxConfig().getLocation("box." + key + ".sign_loc");
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
        if (event.getInventory().getHolder() instanceof DeathBoxInventoryHolder) {
            DeathBoxInventoryHolder holder = (DeathBoxInventoryHolder) event.getInventory().getHolder();
            for (String key : PluginCore.getDeathBoxConfig().getConfigurationSection("box").getKeys(false)) {
                if (PluginCore.getDeathBoxConfig().get("box." + key + ".chest_id").equals(holder.getChestID())) {
                    ArrayList<ItemStack> itemStacks = new ArrayList<>();
                    for (ItemStack itemStack : event.getInventory().getContents()) {
                        if (itemStack != null) {
                            itemStacks.add(itemStack);
                        }
                    }
                    PluginCore.getDeathBoxConfig().set("box." + key + ".items", itemStacks);
                    if (event.getPlayer() instanceof Player) {
                        Player player = (Player) event.getPlayer();
                        if (itemStacks.isEmpty()) {
                            PluginCore.getDeathBoxConfig().getLocation("box." + key + ".sign_loc").getBlock().setType(Material.AIR);
                            PluginCore.getDeathBoxConfig().getLocation("box." + key + ".chest_loc").getBlock().setType(Material.AIR);
                            PluginCore.getDeathBoxConfig().set("box." + key, null);
                            PluginCore.saveBoxRecord();
                            player.sendTitle(" ", "§6这个箱子已自动摧毁.", 0, 70, 0);
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1F, 0F);
                    }
                }
            }
        }
    }
}
