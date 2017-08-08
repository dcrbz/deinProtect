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

public class InteractablePermissionWindow extends PermissionWindow {

    private static final int SLOT_INTERACT = 0;
    private static final int SLOT_MANAGE = 1;
    private static final int SLOT_BREAK = 2;


    public InteractablePermissionWindow(Protection protection, UUID memberId) {
        super(
                9,
                DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_PERMISSIONS_TITLE, false),
                protection,
                memberId
        );
    }


    @Override
    public void show(Player player) {
        setItem(SLOT_INTERACT, buildPermissionSwitch(ProtectionPermission.INTERACT));
        setItem(SLOT_MANAGE, buildPermissionSwitch(ProtectionPermission.MANAGE));
        setItem(SLOT_BREAK, buildPermissionSwitch(ProtectionPermission.BREAK));

        super.show(player);
    }


    @Override
    public void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case SLOT_INTERACT: {
                getMember().togglePermission(ProtectionPermission.INTERACT);
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
