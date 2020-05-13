package bz.dcr.deinprotect.gui.window.permission.pub;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.entity.Player;

public class PublicInteractablePermissionWindow extends PublicPermissionWindow {

    private static final SlotPos SLOT_INTERACT = SlotPos.of(0, 0);
    private static final SlotPos SLOT_MANAGE = SlotPos.of(0, 1);
    private static final SlotPos SLOT_BREAK = SlotPos.of(0, 2);


    public PublicInteractablePermissionWindow(Protection protection) {
        super(protection);
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(SLOT_INTERACT, buildPermissionSwitch(ProtectionPermission.INTERACT));
        inventoryContents.set(SLOT_MANAGE, buildPermissionSwitch(ProtectionPermission.MANAGE));
        inventoryContents.set(SLOT_BREAK, buildPermissionSwitch(ProtectionPermission.BREAK));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        if (!isUpdateRequired()) {
            return;
        }

        init(player, inventoryContents);
        setUpdateRequired(false);
    }

    public static SmartInventory getInventory(Protection protection) {
        return SmartInventory.builder()
                .id("publicInteractablePermissionWindow")
                .provider(new PublicInteractablePermissionWindow(protection))
                .size(1, 9)
                .title(DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_PERMISSIONS_TITLE, false))
                .manager(DeinProtectPlugin.getPlugin().getInventoryManager())
                .build();
    }
}
