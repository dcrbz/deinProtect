package bz.dcr.deinprotect.block;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.Serializable;
import java.util.UUID;

public class BlockLocation implements Serializable {

    private UUID worldId;
    private int posX;
    private int posY;
    private int posZ;


    // <editor-folding> Constructors
    protected BlockLocation() {
    }

    public BlockLocation(UUID worldId, int posX, int posY, int posZ) {
        this.worldId = worldId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public BlockLocation(Block block) {
        this(
                block.getWorld().getUID(),
                block.getX(),
                block.getY(),
                block.getZ()
        );
    }

    public BlockLocation(Location location) {
        this(
                location.getWorld().getUID(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }
    // </editor-folding>


    public World getWorld() {
        return Bukkit.getWorld(getWorldId());
    }

    public Block getBlock() {
        final World world = getWorld();

        if (world == null) {
            return null;
        }

        return world.getBlockAt(getPosX(), getPosY(), getPosZ());
    }

    public Location getBukkitLocation() {
        return new Location(
                getWorld(),
                getPosX(),
                getPosY(),
                getPosZ()
        );
    }


    public UUID getWorldId() {
        return worldId;
    }

    public void setWorldId(UUID worldId) {
        this.worldId = worldId;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public void setPosZ(int posZ) {
        this.posZ = posZ;
    }


    @Override
    public String toString() {
        return "{\"worldId\": \"" + worldId.toString() + "\", \"posX\": " + posX + ", \"posY\": " + posY + ", \"posZ\": " + posZ + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockLocation)) {
            return false;
        }

        final BlockLocation location = (BlockLocation) obj;

        return (
                location.getWorldId().equals(getWorldId()) &&
                        location.getPosX() == getPosX() &&
                        location.getPosY() == getPosY() &&
                        location.getPosZ() == getPosZ()
        );
    }

}
