package xyz.garslity093.serverfunc.deathbox;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class DeathBoxInventoryHolder implements InventoryHolder {
    protected String chestID;
    protected Inventory inventory;

    public DeathBoxInventoryHolder(String uuid) {
        this.chestID = uuid;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getChestID() {
        return chestID;
    }
}