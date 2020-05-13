package bz.dcr.deinprotect.gui.window.permission.player;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ContainerPermissionWindow extends PermissionWindow {

    private static final SlotPos SLOT_OPEN = SlotPos.of(0, 0);
    private static final SlotPos SLOT_PUT_ITEM = SlotPos.of(0, 1);
    private static final SlotPos SLOT_TAKE_ITEM = SlotPos.of(0, 2);
    private static final SlotPos SLOT_MANAGE = SlotPos.of(0, 3);
    private static final SlotPos SLOT_BREAK = SlotPos.of(0, 4);

    public ContainerPermissionWindow(Protection protection, UUID memberId) {
        super(protection, memberId);
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(SLOT_OPEN, buildPermissionSwitch(ProtectionPermission.CONTAINER_OPEN));
        inventoryContents.set(SLOT_PUT_ITEM, buildPermissionSwitch(ProtectionPermission.CONTAINER_PUT_ITEM));
        inventoryContents.set(SLOT_TAKE_ITEM, buildPermissionSwitch(ProtectionPermission.CONTAINER_TAKE_ITEM));
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

    public static SmartInventory getInventory(Protection protection, UUID memberId) {
        return SmartInventory.builder()
                .id("containerPermissionWindow")
                .provider(new ContainerPermissionWindow(protection, memberId))
                .size(1, 9)
                .title(DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_PERMISSIONS_TITLE, false))
                .manager(DeinProtectPlugin.getPlugin().getInventoryManager())
                .build();
    }
}
