package bz.dcr.deinprotect.listener;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.util.MultiPartUtil;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // Get placed block
        final Block block = event.getBlock();

        // Block is not protectable
        if (!DeinProtectPlugin.getPlugin().getProtectionManager().isProtectable(block)) {
            return;
        }

        // Get parts
        final List<Block> parts = MultiPartUtil.getParts(block);

        // No additional parts found
        if (parts.size() == 0) {
            return;
        }

        // Get protection
        final Protection protection = DeinProtectPlugin.getPlugin().getProtectionManager()
                .getProtection(new BlockLocation(parts.get(0)));

        // No protection found
        if (protection == null) {
            return;
        }

        // Add part to protection
        protection.addPart(new BlockLocation(block));

        // Save protection
        DeinProtectPlugin.getPlugin().getProtectionManager().saveProtection(protection);
    }

}
