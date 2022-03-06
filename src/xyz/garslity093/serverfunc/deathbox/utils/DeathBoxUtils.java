package xyz.garslity093.serverfunc.deathbox.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.garslity093.serverfunc.DeathBoxLocation;
import xyz.garslity093.serverfunc.Func;
import xyz.garslity093.serverfunc.ServerUtils;
import xyz.garslity093.serverfunc.deathbox.DeathBoxInventoryHolder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class DeathBoxUtils {
    public static void addBox(DeathBoxLocation deathBoxLocation, Player player) {
        deathBoxLocation.getBlock().setType(Material.CHEST);
        Location signLoc = deathBoxLocation.getSignLocation();
        signLoc.getBlock().setType(Material.OAK_WALL_SIGN);
        Sign sign = (Sign) signLoc.getBlock().getState();
        sign.setLine(0, "§b[遗物箱]");
        sign.setLine(1, "§e主人:");
        sign.setLine(2, "§e" + player.getName());
        sign.update();
        WallSign wallSign = (WallSign) signLoc.getBlock().getBlockData();
        wallSign.setFacing(BlockFace.NORTH);
        Func.getBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".chest_loc", new Location(deathBoxLocation.getWorld(),
                deathBoxLocation.getBlockX(),
                deathBoxLocation.getBlockY(),
                deathBoxLocation.getBlockZ()));
        Func.getBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".sign_loc", signLoc);
        Func.getBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".owner", player.getUniqueId().toString());
        Func.getBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".owner_name", player.getName());
        Func.getBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".chest_id", UUID.randomUUID().toString());
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null) {
                itemStacks.add(itemStack);
            }
        }
        Func.getBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".items", itemStacks);
        Func.saveBoxRecord();
    }

    public static DeathBoxLocation getNewChestLoc(Player player) {
        DeathBoxLocation deathBoxLocation = new DeathBoxLocation(player.getWorld(),
                player.getLocation().getBlockX(), player.getLocation().getBlockY(),
                player.getLocation().getBlockZ());
        if ((deathBoxLocation.getBlock().getType() == Material.AIR ||
                deathBoxLocation.getBlock().getType() == Material.GRASS ||
                deathBoxLocation.getBlock().getType() == Material.TALL_GRASS ||
                deathBoxLocation.getBlock().getType() == Material.SEAGRASS ||
                deathBoxLocation.getBlock().getType() == Material.TALL_SEAGRASS ||
                deathBoxLocation.getBlock().getType() == Material.SNOW) && (deathBoxLocation.getSignLocation().getBlock().getType() == Material.AIR ||
                deathBoxLocation.getSignLocation().getBlock().getType() == Material.GRASS ||
                deathBoxLocation.getSignLocation().getBlock().getType() == Material.TALL_GRASS ||
                deathBoxLocation.getSignLocation().getBlock().getType() == Material.SEAGRASS ||
                deathBoxLocation.getSignLocation().getBlock().getType() == Material.TALL_SEAGRASS ||
                deathBoxLocation.getSignLocation().getBlock().getType() == Material.SNOW)) {
            if (isOnGround(deathBoxLocation)) {
                return deathBoxLocation;
            }
        } else {
            int y = 1;
            while (true) {
                if ((player.getLocation().getBlockY() + y < player.getWorld().getMaxHeight()) && player.getLocation().getBlockY() + y >= player.getWorld().getMinHeight()) {
                    deathBoxLocation = new DeathBoxLocation(player.getWorld(),
                            player.getLocation().getBlockX(),
                            player.getLocation().getBlockY() + y,
                            player.getLocation().getBlockZ());
                    if ((deathBoxLocation.getBlock().getType() == Material.AIR ||
                            deathBoxLocation.getBlock().getType() == Material.GRASS ||
                            deathBoxLocation.getBlock().getType() == Material.TALL_GRASS ||
                            deathBoxLocation.getBlock().getType() == Material.SEAGRASS ||
                            deathBoxLocation.getBlock().getType() == Material.TALL_SEAGRASS ||
                            deathBoxLocation.getBlock().getType() == Material.SNOW) && (deathBoxLocation.getSignLocation().getBlock().getType() == Material.AIR ||
                            deathBoxLocation.getSignLocation().getBlock().getType() == Material.GRASS ||
                            deathBoxLocation.getSignLocation().getBlock().getType() == Material.TALL_GRASS ||
                            deathBoxLocation.getSignLocation().getBlock().getType() == Material.SEAGRASS ||
                            deathBoxLocation.getSignLocation().getBlock().getType() == Material.TALL_SEAGRASS ||
                            deathBoxLocation.getSignLocation().getBlock().getType() == Material.SNOW)) {
                        if (isOnGround(deathBoxLocation)) {
                            return deathBoxLocation;
                        }
                    } else {
                        y++;
                    }
                } else {
                    break;
                }
            }
        }
        return new DeathBoxLocation(player.getWorld(), player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ());
    }

    public static boolean isAnyoneUsingDeathBox(String boxID) {
        for (UUID p : ServerUtils.getOnlinePlayers()) {
            if (Bukkit.getPlayer(p).getOpenInventory().getTopInventory().getHolder() instanceof DeathBoxInventoryHolder) {
                DeathBoxInventoryHolder holder = (DeathBoxInventoryHolder) Bukkit.getPlayer(p).getOpenInventory().getTopInventory().getHolder();
                if (Objects.equals(holder.getChestID(), boxID)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOnGround(DeathBoxLocation deathBoxLocation) {
        if (new Location(deathBoxLocation.getWorld(),
                deathBoxLocation.getBlockX(),
                deathBoxLocation.getBlockY() - 1,
                deathBoxLocation.getBlockZ()).getBlock().getType() != Material.AIR &&
                new Location(deathBoxLocation.getWorld(),
                        deathBoxLocation.getBlockX(),
                        deathBoxLocation.getBlockY() - 1,
                        deathBoxLocation.getBlockZ()).getBlock().getType() != Material.GRASS &&
                new Location(deathBoxLocation.getWorld(),
                        deathBoxLocation.getBlockX(),
                        deathBoxLocation.getBlockY() - 1,
                        deathBoxLocation.getBlockZ()).getBlock().getType() != Material.TALL_GRASS &&
                new Location(deathBoxLocation.getWorld(),
                        deathBoxLocation.getBlockX(),
                        deathBoxLocation.getBlockY() - 1,
                        deathBoxLocation.getBlockZ()).getBlock().getType() != Material.SEAGRASS &&
                new Location(deathBoxLocation.getWorld(),
                        deathBoxLocation.getBlockX(),
                        deathBoxLocation.getBlockY() - 1,
                        deathBoxLocation.getBlockZ()).getBlock().getType() != Material.TALL_SEAGRASS &&
                new Location(deathBoxLocation.getWorld(),
                        deathBoxLocation.getBlockX(),
                        deathBoxLocation.getBlockY() - 1,
                        deathBoxLocation.getBlockZ()).getBlock().getType() != Material.SNOW

        ) {
            return new Location(deathBoxLocation.getSignLocation().getWorld(),
                    deathBoxLocation.getSignLocation().getBlockX(),
                    deathBoxLocation.getSignLocation().getBlockY() - 1,
                    deathBoxLocation.getSignLocation().getBlockZ()).getBlock().getType() != Material.AIR &&
                    new Location(deathBoxLocation.getSignLocation().getWorld(),
                            deathBoxLocation.getSignLocation().getBlockX(),
                            deathBoxLocation.getSignLocation().getBlockY() - 1,
                            deathBoxLocation.getSignLocation().getBlockZ()).getBlock().getType() != Material.GRASS &&
                    new Location(deathBoxLocation.getSignLocation().getWorld(),
                            deathBoxLocation.getSignLocation().getBlockX(),
                            deathBoxLocation.getSignLocation().getBlockY() - 1,
                            deathBoxLocation.getSignLocation().getBlockZ()).getBlock().getType() != Material.TALL_GRASS &&
                    new Location(deathBoxLocation.getSignLocation().getWorld(),
                            deathBoxLocation.getSignLocation().getBlockX(),
                            deathBoxLocation.getSignLocation().getBlockY() - 1,
                            deathBoxLocation.getSignLocation().getBlockZ()).getBlock().getType() != Material.SEAGRASS &&
                    new Location(deathBoxLocation.getSignLocation().getWorld(),
                            deathBoxLocation.getSignLocation().getBlockX(),
                            deathBoxLocation.getSignLocation().getBlockY() - 1,
                            deathBoxLocation.getSignLocation().getBlockZ()).getBlock().getType() != Material.TALL_SEAGRASS &&
                    new Location(deathBoxLocation.getSignLocation().getWorld(),
                            deathBoxLocation.getSignLocation().getBlockX(),
                            deathBoxLocation.getSignLocation().getBlockY() - 1,
                            deathBoxLocation.getSignLocation().getBlockZ()).getBlock().getType() != Material.SNOW;
        }
        return false;
    }
}