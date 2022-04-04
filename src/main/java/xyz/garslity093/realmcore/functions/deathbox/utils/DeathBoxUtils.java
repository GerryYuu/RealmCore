package xyz.garslity093.realmcore.functions.deathbox.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.garslity093.realmcore.PluginCore;
import xyz.garslity093.realmcore.PluginUtils;
import xyz.garslity093.realmcore.functions.deathbox.DeathBoxInventoryHolder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public final class DeathBoxUtils {
    public static void addBox(Location boxLocation, Player player) {
        getSignLocByBoxLoc(boxLocation).getBlock().setType(Material.CHEST);
        Location signLoc = getSignLocByBoxLoc(boxLocation);
        signLoc.getBlock().setType(Material.OAK_WALL_SIGN);
        Sign sign = (Sign) signLoc.getBlock().getState();
        sign.setLine(0, "§b[遗物箱]");
        sign.setLine(1, "§e主人:");
        sign.setLine(2, "§e" + player.getName());
        sign.update();
        WallSign wallSign = (WallSign) signLoc.getBlock().getBlockData();
        wallSign.setFacing(BlockFace.NORTH);
        PluginCore.getDeathBoxConfig().set("box." + getSignLocByBoxLoc(boxLocation).getBlockX() + "," + getSignLocByBoxLoc(boxLocation).getBlockY() + "," + getSignLocByBoxLoc(boxLocation).getBlockZ() + ".chest_loc", new Location(getSignLocByBoxLoc(boxLocation).getWorld(),
                getSignLocByBoxLoc(boxLocation).getBlockX(),
                getSignLocByBoxLoc(boxLocation).getBlockY(),
                getSignLocByBoxLoc(boxLocation).getBlockZ()));
        PluginCore.getDeathBoxConfig().set("box." + getSignLocByBoxLoc(boxLocation).getBlockX() + "," + getSignLocByBoxLoc(boxLocation).getBlockY() + "," + getSignLocByBoxLoc(boxLocation).getBlockZ() + ".sign_loc", signLoc);
        PluginCore.getDeathBoxConfig().set("box." + getSignLocByBoxLoc(boxLocation).getBlockX() + "," + getSignLocByBoxLoc(boxLocation).getBlockY() + "," + getSignLocByBoxLoc(boxLocation).getBlockZ() + ".owner", player.getUniqueId().toString());
        PluginCore.getDeathBoxConfig().set("box." + getSignLocByBoxLoc(boxLocation).getBlockX() + "," + getSignLocByBoxLoc(boxLocation).getBlockY() + "," + getSignLocByBoxLoc(boxLocation).getBlockZ() + ".owner_name", player.getName());
        PluginCore.getDeathBoxConfig().set("box." + getSignLocByBoxLoc(boxLocation).getBlockX() + "," + getSignLocByBoxLoc(boxLocation).getBlockY() + "," + getSignLocByBoxLoc(boxLocation).getBlockZ() + ".chest_id", UUID.randomUUID().toString());
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null) {
                itemStacks.add(itemStack);
            }
        }
        PluginCore.getDeathBoxConfig().set("box." + getSignLocByBoxLoc(boxLocation).getBlockX() + "," + getSignLocByBoxLoc(boxLocation).getBlockY() + "," + getSignLocByBoxLoc(boxLocation).getBlockZ() + ".items", itemStacks);
        PluginCore.saveBoxRecord();
    }

    public static Location getNewBoxLoc(Location playerDeathLoc) {
        World world = playerDeathLoc.getWorld();
        int x = playerDeathLoc.getBlockX();
        int y = playerDeathLoc.getBlockY();
        int z = playerDeathLoc.getBlockZ();
        int worldMaxHeight = world.getMaxHeight();
        int worldMinHeight = world.getMinHeight();
        if (isOnGround(playerDeathLoc)) {
            if (isCanReplace(playerDeathLoc)) {
                return playerDeathLoc;
            }
        }
        Location location = new Location(world, x, y, z);
        for (int i = worldMaxHeight - 1; i >= worldMinHeight; i--) {
            location = new Location(world, x, i, z);
            if (isOnGround(location)) {
                if (isCanReplace(location)) {
                    return location;
                }
            }
        }
        return playerDeathLoc;
    }

    public static boolean isBoxAlreadyOpen(String boxID) {
        for (UUID p : PluginUtils.getOnlinePlayers()) {
            if (Bukkit.getPlayer(p).getOpenInventory().getTopInventory().getHolder() instanceof DeathBoxInventoryHolder) {
                DeathBoxInventoryHolder holder = (DeathBoxInventoryHolder) Bukkit.getPlayer(p).getOpenInventory().getTopInventory().getHolder();
                if (Objects.equals(holder.getChestID(), boxID)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOnGround(Location location) {
        Material blockTypeUnderBox = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()).getBlock().getType();
        return blockTypeUnderBox != Material.AIR;
    }

    public static boolean isCanReplace(Location boxLocation) {
        Material deathBoxMat = boxLocation.getBlock().getType();
        Material signMat = getSignLocByBoxLoc(boxLocation).getBlock().getType();
        ArrayList<Material> materials = PluginCore.getDeathBoxReplaceBlocks();
        return materials.contains(deathBoxMat) && materials.contains(signMat);
    }

    public static ArrayList<Location> getAllBoxLocations(Player player) {
        ArrayList<Location> locations = new ArrayList<>();
        ConfigurationSection configurationSection = PluginCore.getDeathBoxConfig().getConfigurationSection("box");
        for (String key : configurationSection.getKeys(false)) {
            if (PluginCore.getDeathBoxConfig().getString("box." + key + ".owner").equals(player.getUniqueId().toString())) {
                locations.add(PluginCore.getDeathBoxConfig().getLocation("box." + key + ".chest_loc"));
            }
        }
        return locations;
    }

    public static Location getSignLocByBoxLoc(Location boxLoc) {
        return new Location(boxLoc.getWorld(),
                boxLoc.getBlockX(),
                boxLoc.getBlockY(),
                boxLoc.getBlockZ() - 1);
    }
}