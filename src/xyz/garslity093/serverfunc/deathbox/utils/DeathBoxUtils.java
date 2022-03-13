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
        Func.getDeathBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".chest_loc", new Location(deathBoxLocation.getWorld(),
                deathBoxLocation.getBlockX(),
                deathBoxLocation.getBlockY(),
                deathBoxLocation.getBlockZ()));
        Func.getDeathBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".sign_loc", signLoc);
        Func.getDeathBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".owner", player.getUniqueId().toString());
        Func.getDeathBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".owner_name", player.getName());
        Func.getDeathBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".chest_id", UUID.randomUUID().toString());
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null) {
                itemStacks.add(itemStack);
            }
        }
        Func.getDeathBoxConfig().set("box." + deathBoxLocation.getBlockX() + "," + deathBoxLocation.getBlockY() + "," + deathBoxLocation.getBlockZ() + ".items", itemStacks);
        Func.saveBoxRecord();
    }

    public static DeathBoxLocation getNewChestLoc(Location playerDeathLocation) {
        Location signLocation = new Location(playerDeathLocation.getWorld(), playerDeathLocation.getBlockX(), playerDeathLocation.getBlockY(), playerDeathLocation.getBlockZ() - 1);
        Location deathBoxLocation;
        if (isCanReplaceBlock(playerDeathLocation)) {
            deathBoxLocation = new Location(playerDeathLocation.getWorld(), playerDeathLocation.getBlockX(), playerDeathLocation.getBlockY(), playerDeathLocation.getBlockZ());
        } else {
            int y = 1;
            while (true) {
                if (!(playerDeathLocation.getBlockY() + y == playerDeathLocation.getWorld().getMaxHeight())) {
                    if (new Location(playerDeathLocation.getWorld(), playerDeathLocation.getBlockX(), playerDeathLocation.getBlockY() + y, playerDeathLocation.getBlockZ()).getBlock().getType() == Material.AIR &&
                            new Location(playerDeathLocation.getWorld(), playerDeathLocation.getBlockX(), playerDeathLocation.getBlockY() + y, playerDeathLocation.getBlockZ() - 1).getBlock().getType() == Material.AIR) {
                        break;
                    } else {
                        y++;
                    }
                } else {
                    break;
                }
            }
            deathBoxLocation = new Location(playerDeathLocation.getWorld(), playerDeathLocation.getBlockX(), playerDeathLocation.getBlockY() + y, playerDeathLocation.getBlockZ());
        }
        return new DeathBoxLocation(deathBoxLocation);
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

    /*public static boolean isOnGround(DeathBoxLocation deathBoxLocation) {
        Material blockUnderBox = new Location(deathBoxLocation.getWorld(),
                deathBoxLocation.getBlockX(),
                deathBoxLocation.getBlockY() - 1,
                deathBoxLocation.getBlockZ()).getBlock().getType();
        Material blockUnderSign = new Location(deathBoxLocation.getWorld(),
                deathBoxLocation.getBlockX(),
                deathBoxLocation.getBlockY() - 1,
                deathBoxLocation.getBlockZ() - 1).getBlock().getType();
        if (Func.getDeathBoxReplaceBlocks().size() > 0)  {
            if (Func.getDeathBoxReplaceBlocks().contains(blockUnderBox)) {
                if (Func.getDeathBoxReplaceBlocks().contains(blockUnderSign)) {
                    return true;
                }
            }
        }
        return blockUnderBox != Material.AIR && blockUnderSign != Material.AIR;
    }*/

    public static boolean isCanReplaceBlock(Location deathBoxLocation) {
        Material deathBoxMat = deathBoxLocation.getBlock().getType();
        Material signMat = new DeathBoxLocation(deathBoxLocation).getSignLocation().getBlock().getType();
        ArrayList<Material> materials = Func.getDeathBoxReplaceBlocks();
        return materials.contains(deathBoxMat) && materials.contains(signMat);
    }
}