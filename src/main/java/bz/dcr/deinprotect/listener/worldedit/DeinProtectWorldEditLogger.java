package bz.dcr.deinprotect.listener.worldedit;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.protection.entity.Protection;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.logging.AbstractLoggingExtent;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class DeinProtectWorldEditLogger extends AbstractLoggingExtent {

    private DeinProtectPlugin plugin;

    private World world;


    DeinProtectWorldEditLogger(Extent extent, World world, DeinProtectPlugin plugin) {
        super(extent);

        this.world = world;
        this.plugin = plugin;
    }


    @Override
    protected void onBlockChange(Vector position, BaseBlock newBlock) {
        // Get BlockLocation
        final BlockLocation location = new BlockLocation(
                world.getUID(),
                position.getBlockX(),
                position.getBlockY(),
                position.getBlockZ()
        );

        // Block is not protectable
        if (!plugin.getProtectionManager().isProtectable(location.getBlock())) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // Get protection
            final Protection protection = plugin.getProtectionManager().getProtection(location);

            // Block is not protected
            if (protection == null) {
                return;
            }

            // Delete protection
            plugin.getProtectionManager().deleteProtection(protection);

            // Log event
            plugin.getLogger().warning("Protection at " + location.toString() + " has been deleted (Reason: WorldEdit)");
        });
    }

}
