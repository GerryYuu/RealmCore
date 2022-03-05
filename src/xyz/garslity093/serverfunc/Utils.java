package xyz.garslity093.serverfunc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public final class Utils {
    public static ArrayList<UUID> getOnlinePlayers() {
        ArrayList<UUID> uuids = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            uuids.add(player.getUniqueId());
        }
        return uuids;
    }

    public static void addBox(Location chestLoc, Location signLoc, Player player) {
        chestLoc.getBlock().setType(Material.CHEST);
        signLoc.getBlock().setType(Material.OAK_WALL_SIGN);
        Sign sign = (Sign) signLoc.getBlock().getState();
        sign.setLine(0, "§b[遗物箱]");
        sign.setLine(1, "§e主人:");
        sign.setLine(2, "§e" + player.getName());
        sign.update();
        WallSign wallSign = (WallSign) signLoc.getBlock().getBlockData();
        wallSign.setFacing(BlockFace.NORTH);
        Func.getBoxConfig().set("box." + chestLoc.getBlockX() + "," + chestLoc.getBlockY() + "," + chestLoc.getBlockZ() + ".chest_loc", chestLoc);
        Func.getBoxConfig().set("box." + chestLoc.getBlockX() + "," + chestLoc.getBlockY() + "," + chestLoc.getBlockZ() + ".sign_loc", signLoc);
        Func.getBoxConfig().set("box." + chestLoc.getBlockX() + "," + chestLoc.getBlockY() + "," + chestLoc.getBlockZ() + ".owner", player.getUniqueId().toString());
        Func.getBoxConfig().set("box." + chestLoc.getBlockX() + "," + chestLoc.getBlockY() + "," + chestLoc.getBlockZ() + ".owner_name", player.getName());
        Func.getBoxConfig().set("box." + chestLoc.getBlockX() + "," + chestLoc.getBlockY() + "," + chestLoc.getBlockZ() + ".chest_id", UUID.randomUUID().toString());
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null) {
                itemStacks.add(itemStack);
            }
        }
        Func.getBoxConfig().set("box." + chestLoc.getBlockX() + "," + chestLoc.getBlockY() + "," + chestLoc.getBlockZ() + ".items", itemStacks);
        Func.saveBoxRecord();
    }

    public static Location getNewChestLoc(Player player) {
        Location chestLoc;
        if (player.getLocation().getBlock().getType() == Material.AIR &&
                new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ() - 1).getBlock().getType() == Material.AIR) {
            chestLoc = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
        } else {
            int y = 1;
            while (true) {
                if (!(player.getLocation().getBlockY() + y == player.getWorld().getMaxHeight())) {
                    if (new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + y, player.getLocation().getBlockZ()).getBlock().getType() == Material.AIR &&
                            new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + y, player.getLocation().getBlockZ() - 1).getBlock().getType() == Material.AIR) {
                        break;
                    } else {
                        y++;
                    }
                } else {
                    break;
                }
            }
            chestLoc = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + y, player.getLocation().getBlockZ());
        }
        return chestLoc;
    }

    public static Location getNewSignLoc(Player player) {
        Location signLoc;
        if (player.getLocation().getBlock().getType() == Material.AIR &&
                new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ() - 1).getBlock().getType() == Material.AIR) {
            signLoc = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ() - 1);
        } else {
            int y = 1;
            while (true) {
                if (!(player.getLocation().getBlockY() + y == player.getWorld().getMaxHeight())) {
                    if (new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + y, player.getLocation().getBlockZ()).getBlock().getType() == Material.AIR &&
                            new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + y, player.getLocation().getBlockZ() - 1).getBlock().getType() == Material.AIR) {
                        break;
                    } else {
                        y++;
                    }
                } else {
                    break;
                }
            }
            signLoc = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + y, player.getLocation().getBlockZ() - 1);
        }
        return signLoc;
    }

    public static boolean isAnyOneOpeningBox(String boxId) {
        boolean b = false;
        for (UUID p : Utils.getOnlinePlayers()) {
            if (Bukkit.getPlayer(p).getOpenInventory().getTopInventory().getHolder() instanceof BoxInventoryHolder) {
                BoxInventoryHolder holder = (BoxInventoryHolder) Bukkit.getPlayer(p).getOpenInventory().getTopInventory().getHolder();
                if (holder.getChestID() == boxId) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }
}