package bz.dcr.deinprotect.listener;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.protection.ProtectionType;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    private static final InventoryAction[] putActions = {
            InventoryAction.PLACE_ALL,
            InventoryAction.PLACE_ONE,
            InventoryAction.PLACE_SOME,
            InventoryAction.SWAP_WITH_CURSOR
    };

    private static final InventoryAction[] takeActions = {
            InventoryAction.DROP_ALL_CURSOR,
            InventoryAction.DROP_ALL_SLOT,
            InventoryAction.DROP_ONE_CURSOR,
            InventoryAction.DROP_ONE_SLOT,
            InventoryAction.COLLECT_TO_CURSOR,
            InventoryAction.SWAP_WITH_CURSOR,
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.HOTBAR_MOVE_AND_READD,
            InventoryAction.PICKUP_ALL,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_ONE,
            InventoryAction.PICKUP_SOME,
            InventoryAction.MOVE_TO_OTHER_INVENTORY
    };


    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        // No inventory clicked
        if (event.getClickedInventory() == null) {
            return;
        }

        // Inventory has no InventoryHolder
        if (event.getClickedInventory().getHolder() == null) {
            return;
        }

        final Location location = event.getClickedInventory().getLocation();

        // Not a protectable inventory
        if (location == null) {
            return;
        }

        // Get protection
        final Protection protection = DeinProtectPlugin.getPlugin().getProtectionManager().getProtection(
                new BlockLocation(location)
        );

        // No protection existing
        if (protection == null) {
            return;
        }

        // Not a container protection
        if (protection.getType() != ProtectionType.CONTAINER) {
            return;
        }

        // Get player
        final Player player = (Player) event.getWhoClicked();

        // Check if player has permission
        if (!protection.hasPermission(player, ProtectionPermission.CONTAINER_PUT_ITEM) && isPutAction(event.getAction())) {
            event.setCancelled(true);
        } else if (!protection.hasPermission(player, ProtectionPermission.CONTAINER_TAKE_ITEM) && isTakeAction(event.getAction())) {
            event.setCancelled(true);
        }
    }


    private static boolean isPutAction(InventoryAction action) {
        for (InventoryAction a : putActions) {
            if (a == action) {
                return true;
            }
        }
        return false;
    }

    private static boolean isTakeAction(InventoryAction action) {
        for (InventoryAction a : takeActions) {
            if (a == action) {
                return true;
            }
        }
        return false;
    }

}
