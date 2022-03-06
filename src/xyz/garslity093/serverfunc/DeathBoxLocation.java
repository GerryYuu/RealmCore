package xyz.garslity093.serverfunc;

import org.bukkit.Location;
import org.bukkit.World;

public final class DeathBoxLocation extends Location {
    public DeathBoxLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public Location getSignLocation() {
        return new Location(getWorld(), getX(), getY(), getZ() - 1);
    }
}
