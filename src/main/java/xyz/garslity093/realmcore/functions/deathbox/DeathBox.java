package xyz.garslity093.realmcore.functions.deathbox;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.garslity093.realmcore.PluginUtils;
import xyz.garslity093.realmcore.functions.deathbox.utils.DeathBoxUtils;

import java.util.List;
import java.util.UUID;

public class DeathBox {
    Material boxMaterial;
    Location boxLocation;
    Location signLocation;
    String ownerId;
    String ownerName;
    List<ItemStack> items;
    String boxId;
    Long expireTime;

    public DeathBox(Player owner, Location boxLocation, List<ItemStack> items) {
        this.ownerId = owner.getUniqueId().toString();
        this.boxLocation = boxLocation;
        this.items = items;
        ownerName = owner.getName().toLowerCase();
        boxId = String.valueOf(UUID.randomUUID());
        boxMaterial = Material.CHEST;
        signLocation = DeathBoxUtils.getSignLocByBoxLoc(boxLocation);
        expireTime = PluginUtils.getUnixTimestamp() + 3600 * 24 * 10;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public Location getBoxLocation() {
        return boxLocation;
    }

    public void setBoxLocation(Location boxLocation) {
        this.boxLocation = boxLocation;
    }

    public Location getSignLocation() {
        return signLocation;
    }

    public void setSignLocation(Location signLocation) {
        this.signLocation = signLocation;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Material getBoxMaterial() {
        return boxMaterial;
    }

    public void setBoxMaterial(Material boxMaterial) {
        this.boxMaterial = boxMaterial;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
