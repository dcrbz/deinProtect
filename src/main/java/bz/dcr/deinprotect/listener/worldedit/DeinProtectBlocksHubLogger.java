package bz.dcr.deinprotect.listener.worldedit;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.protection.entity.Protection;
import org.bukkit.Bukkit;
import org.primesoft.blockshub.api.BlockData;
import org.primesoft.blockshub.api.IBlockLogger;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.IWorld;

public class DeinProtectBlocksHubLogger implements IBlockLogger {

    private static final String LOGGER_NAME = "deinprotect_block_logger";

    private DeinProtectPlugin plugin;


    public DeinProtectBlocksHubLogger(DeinProtectPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void logBlock(org.primesoft.blockshub.api.Vector location, IPlayer player, IWorld world, BlockData oldBlock, BlockData newBlock) {
        // Get BlockLocation
        final BlockLocation blockLocation = new BlockLocation(
                world.getUuid(),
                (int) location.getX(),
                (int) location.getY(),
                (int) location.getZ()
        );

        // Block is not protectable
        if (!plugin.getProtectionManager().isProtectable(blockLocation.getBlock())) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // Get protection
            final Protection protection = plugin.getProtectionManager().getProtection(blockLocation);

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

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean reloadConfiguration() {
        return true;
    }

    @Override
    public String getName() {
        return LOGGER_NAME;
    }

}
