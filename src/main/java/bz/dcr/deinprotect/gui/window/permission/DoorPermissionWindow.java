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

public class DoorPermissionWindow extends PermissionWindow {

    private static final int SLOT_DOOR_OPEN = 0;
    private static final int SLOT_DOOR_CLOSE = 1;
    private static final int SLOT_MANAGE = 2;
    private static final int SLOT_BREAK = 3;


    public DoorPermissionWindow(Protection protection, UUID memberId) {
        super(
                9,
                DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_PERMISSIONS_TITLE, false),
                protection,
                memberId
        );
    }


    @Override
    public void show(Player player) {
        setItem(SLOT_DOOR_OPEN, buildPermissionSwitch(ProtectionPermission.DOOR_OPEN));
        setItem(SLOT_DOOR_CLOSE, buildPermissionSwitch(ProtectionPermission.DOOR_CLOSE));
        setItem(SLOT_MANAGE, buildPermissionSwitch(ProtectionPermission.MANAGE));
        setItem(SLOT_BREAK, buildPermissionSwitch(ProtectionPermission.BREAK));

        super.show(player);
    }


    @Override
    public void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case SLOT_DOOR_OPEN: {
                getMember().togglePermission(ProtectionPermission.DOOR_OPEN);
                break;
            }
            case SLOT_DOOR_CLOSE: {
                getMember().togglePermission(ProtectionPermission.DOOR_CLOSE);
                break;
            }
            case SLOT_MANAGE: {
                getMember().togglePermission(ProtectionPermission.MANAGE);
                break;
            }
            case SLOT_BREAK: {
                getMember().togglePermission(ProtectionPermission.BREAK);
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
