package bz.dcr.deinprotect.listener;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Get block
        final Block block = event.getBlock();

        // Block is not protectable
        if (!DeinProtectPlugin.getPlugin().getProtectionManager().isProtectable(block)) {
            return;
        }

        // Create BlockLocation
        final BlockLocation blockLocation = new BlockLocation(block);

        // Get player
        final Player player = event.getPlayer();

        // Get protection
        final Protection protection = DeinProtectPlugin.getPlugin().getProtectionManager()
                .getProtection(blockLocation);

        // Block is not protected
        if (protection == null) {
            return;
        }

        // Player is not allowed to break block
        if (!protection.hasPermission(player, ProtectionPermission.BREAK)) {
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(DeinProtectPlugin.getPlugin(), () -> {
            // Remove protection from block
            DeinProtectPlugin.getPlugin().getProtectionManager()
                    .deleteProtectionPart(blockLocation);

            // Send message
            player.sendMessage(DeinProtectPlugin.getPlugin().getLangManager()
                    .getMessage(LangKey.PROTECTION_DELETED, true)
            );
        });
    }

}
