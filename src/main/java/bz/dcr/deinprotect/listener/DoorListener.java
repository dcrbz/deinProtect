package bz.dcr.deinprotect.listener;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.protection.ProtectionType;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Openable;

public class DoorListener implements Listener {

    private DeinProtectPlugin plugin;

    // Constructor
    public DoorListener(DeinProtectPlugin plugin){
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onDoorInteract(PlayerInteractEvent event){
        // Player did not click a block
        if (event.getClickedBlock() == null) {
            return;
        }

        // Get clicked block
        final Block block = event.getClickedBlock();

        // Clicked block is not protectable
        if (!plugin.getProtectionManager().isProtectable(event.getClickedBlock())) {
            return;
        }

        // Get protection
        final Protection protection = DeinProtectPlugin.getPlugin().getProtectionManager().getProtection(
                new BlockLocation(block)
        );

        // No protection present
        if (protection == null) {
            return;
        }

        // Protection is not a door
        if (protection.getType() != ProtectionType.DOOR) {
            return;
        }

        // Check door permissions
        if (block.getState().getData() instanceof Openable) {
            final Openable door = (Openable) block.getState().getData();
            boolean hasOpenPermission;

            if (door.isOpen()) {
                hasOpenPermission = protection.hasPermission(event.getPlayer(), ProtectionPermission.DOOR_CLOSE);
            } else {
                hasOpenPermission = protection.hasPermission(event.getPlayer(), ProtectionPermission.DOOR_OPEN);
            }

            event.setCancelled(!hasOpenPermission);
        }
    }


}
