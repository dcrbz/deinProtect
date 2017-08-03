package bz.dcr.deinprotect.listener;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        // Event was cancelled
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        // Not right-clicking block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();

        // Block can not be protected
        if (!DeinProtectPlugin.getPlugin().getProtectionManager().isProtectable(block)) {
            return;
        }

        // Using key item
        if (DeinProtectPlugin.getPlugin().getKeyItemProvider().isKeyItem(event.getItem())) {
            event.setCancelled(true);

            // Get protection of clicked block
            Protection protection = DeinProtectPlugin.getPlugin().getProtectionManager()
                    .getProtection(new BlockLocation(event.getClickedBlock()));

            if (protection == null) {
                // Create new protection
                protection = DeinProtectPlugin.getPlugin().getProtectionManager().createProtection(
                        player,
                        new BlockLocation(block)
                );

                // Send message
                player.sendMessage(
                        DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.PROTECTION_CREATED, true)
                );
            } else {
                player.sendMessage("§cDieser Block ist bereits gesichert!");
            }

            return;
        }

        final Protection protection = DeinProtectPlugin.getPlugin().getProtectionManager().getProtection(
                new BlockLocation(block)
        );

        // Block is not protected
        if (protection == null) {
            return;
        }

        // Player has no permission
        if (!protection.hasPermission(player, ProtectionPermission.INTERACT)) {
            event.setCancelled(true);
        }
    }

}
