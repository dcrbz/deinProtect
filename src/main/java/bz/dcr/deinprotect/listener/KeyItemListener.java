package bz.dcr.deinprotect.listener;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.gui.window.MainMenuWindow;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class KeyItemListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // Not right-clicking block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();

        // Block can not be protected
        if (!DeinProtectPlugin.getPlugin().getProtectionManager().isProtectable(block)) {
            return;
        }

        // Player is not using key item
        if (!DeinProtectPlugin.getPlugin().getKeyItemProvider().isKeyItem(event.getItem())) {
            return;
        }

        // Cancel event
        event.setCancelled(true);

        // Get protection
        Protection protection = DeinProtectPlugin.getPlugin().getProtectionManager().getProtection(
                new BlockLocation(block)
        );

        // Block is not protected
        if (protection == null) {
            // Check if player has access to block
            final boolean hasAccess = DeinProtectPlugin.getPlugin().getBlocksHub()
                    .getApi().hasAccess(
                            player.getUniqueId(),block.getWorld().getUID(), block.getX(), block.getY(), block.getZ());

            // Player does not have access to block
            if (!hasAccess) {
                return;
            }

            // Create new protection
            DeinProtectPlugin.getPlugin().getProtectionManager().createProtection(
                    player,
                    new BlockLocation(block)
            );

            // Send message
            player.sendMessage(
                    DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.PROTECTION_CREATED, true)
            );
        } else if (protection.hasPermission(player, ProtectionPermission.MANAGE)) {
            // Open management GUI
            MainMenuWindow.getInventory(protection).open(player);
        }
    }

}
