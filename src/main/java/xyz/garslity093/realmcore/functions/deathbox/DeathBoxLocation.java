package xyz.garslity093.realmcore.functions.deathbox;

import org.bukkit.Location;
import org.bukkit.World;

public final class DeathBoxLocation extends Location {
    public DeathBoxLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public DeathBoxLocation(Location location) {
        super(location.getWorld(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ());
    }

    public Location getSignLocation() {
        return new Location(getWorld(), getX(), getY(), getZ() - 1);
    }
}