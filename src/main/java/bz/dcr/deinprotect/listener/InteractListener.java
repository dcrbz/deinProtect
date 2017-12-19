package bz.dcr.deinprotect.listener;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.protection.ProtectionType;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Openable;
import org.primesoft.blockshub.api.Vector;

public class InteractListener implements Listener {

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

        final Protection protection = DeinProtectPlugin.getPlugin().getProtectionManager().getProtection(
                new BlockLocation(block)
        );

        // Block is not protected
        if (protection == null) {
            final boolean hasAccess = DeinProtectPlugin.getPlugin().getBlocksHub()
                    .getApi().hasAccess(player.getUniqueId(), block.getWorld().getUID(),
                            new Vector(block.getX(), block.getY(), block.getZ()));

            event.setCancelled(!hasAccess);
            return;
        }

        // Check protection permissions
        if (protection.getType() == ProtectionType.CONTAINER) {
            event.setCancelled(
                    !protection.hasPermission(player, ProtectionPermission.CONTAINER_OPEN)
            );
        } else if (protection.getType() == ProtectionType.DOOR) {
            // Check door permissions
            if (block.getState().getData() instanceof Openable) {
                final Openable door = (Openable) block.getState().getData();
                boolean hasOpenPermission;

                if (door.isOpen()) {
                    hasOpenPermission = protection.hasPermission(player, ProtectionPermission.DOOR_CLOSE);
                } else {
                    hasOpenPermission = protection.hasPermission(player, ProtectionPermission.DOOR_OPEN);
                }

                event.setCancelled(!hasOpenPermission);
            }
        } else {
            // Player is not allowed to interact
            event.setCancelled(!protection.hasPermission(player, ProtectionPermission.INTERACT));
        }
    }

}
