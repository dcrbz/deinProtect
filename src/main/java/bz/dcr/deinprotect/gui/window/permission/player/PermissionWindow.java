package bz.dcr.deinprotect.gui.window.permission.player;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionMember;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public abstract class PermissionWindow implements InventoryProvider {

    private boolean updateRequired;

    private Protection protection;
    private UUID memberId;
    private ProtectionMember member;

    public PermissionWindow(Protection protection, UUID memberId) {
        this.setProtection(protection);
        this.setMemberId(memberId);
    }

    protected ClickableItem buildPermissionSwitch(ProtectionPermission permission) {
        final ProtectionMember member = protection.getMember(memberId);

        if (member == null) {
            return null;
        }

        final boolean hasPermission = member.hasPermission(permission);

        ItemStack item = hasPermission ? buildOnItem() : buildOffItem();
        ItemMeta itemMeta = item.getItemMeta();

        // Set display name
        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage((hasPermission ? LangKey.GUI_PERMISSIONS_ON : LangKey.GUI_PERMISSIONS_OFF), false) +
                        DeinProtectPlugin.getPlugin().getLangManager()
                                .getMessage(permission.getLangKey(), false)
        );

        item.setItemMeta(itemMeta);

        return ClickableItem.of(item, (event) -> {
            event.setCancelled(true);

            getMember().togglePermission(permission);

            // Update member
            getProtection().putMember(getMember());

            // Save protection
            Bukkit.getScheduler().runTaskAsynchronously(DeinProtectPlugin.getPlugin(), () -> {
                DeinProtectPlugin.getPlugin().getProtectionManager().saveProtection(getProtection());
            });

            // Schedule update
            setUpdateRequired(true);
        });
    }

    protected boolean isUpdateRequired() {
        return updateRequired;
    }

    protected void setUpdateRequired(boolean updateRequired) {
        this.updateRequired = updateRequired;
    }


    private ItemStack buildOnItem() {
        return new ItemStack(Material.LIME_CONCRETE);
    }

    private ItemStack buildOffItem() {
        return new ItemStack(Material.RED_CONCRETE);
    }


    protected Protection getProtection() {
        return protection;
    }

    protected void setProtection(Protection protection) {
        this.protection = protection;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
        this.member = protection.getMember(memberId);
    }

    public ProtectionMember getMember() {
        return member;
    }

}
