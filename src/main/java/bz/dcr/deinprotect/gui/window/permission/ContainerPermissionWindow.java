package bz.dcr.deinprotect.gui.window.permission;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.upperlevel.spigot.gui.GuiManager;

import java.util.UUID;

public class ContainerPermissionWindow extends PermissionWindow {

    private static final int SLOT_OPEN = 1;
    private static final int SLOT_PUT_ITEM = 3;
    private static final int SLOT_TAKE_ITEM = 5;
    private static final int SLOT_MANAGE = 7;


    public ContainerPermissionWindow(Protection protection, UUID memberId) {
        super(
                9,
                DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_PERMISSIONS_TITLE, false),
                protection,
                memberId
        );
    }


    @Override
    public void show(Player player) {
        setItem(SLOT_OPEN, buildPermissionSwitch(ProtectionPermission.CONTAINER_OPEN));
        setItem(SLOT_PUT_ITEM, buildPermissionSwitch(ProtectionPermission.CONTAINER_PUT_ITEM));
        setItem(SLOT_TAKE_ITEM, buildPermissionSwitch(ProtectionPermission.CONTAINER_TAKE_ITEM));
        setItem(SLOT_MANAGE, buildPermissionSwitch(ProtectionPermission.MANAGE));

        super.show(player);
    }


    @Override
    public void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case SLOT_OPEN: {
                getMember().togglePermission(ProtectionPermission.CONTAINER_OPEN);
                break;
            }
            case SLOT_PUT_ITEM: {
                getMember().togglePermission(ProtectionPermission.CONTAINER_PUT_ITEM);
                break;
            }
            case SLOT_TAKE_ITEM: {
                getMember().togglePermission(ProtectionPermission.CONTAINER_TAKE_ITEM);
                break;
            }
            case SLOT_MANAGE: {
                getMember().togglePermission(ProtectionPermission.MANAGE);
                break;
            }
            default: return;
        }

        // Update member
        getProtection().putMember(getMember());

        // Save protection
        Bukkit.getScheduler().runTaskAsynchronously(DeinProtectPlugin.getPlugin(), () -> {
            DeinProtectPlugin.getPlugin().getProtectionManager().saveProtection(getProtection());
        });

        // Update GUI
        GuiManager.reprint(player);
    }

}
