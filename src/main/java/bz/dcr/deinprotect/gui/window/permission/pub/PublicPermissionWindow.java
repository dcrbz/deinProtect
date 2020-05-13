package bz.dcr.deinprotect.gui.window.permission.pub;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class PublicPermissionWindow implements InventoryProvider {

    private boolean updateRequired;

    private Protection protection;

    public PublicPermissionWindow(Protection protection) {
        this.setProtection(protection);
    }


    protected ClickableItem buildPermissionSwitch(ProtectionPermission permission) {
        final boolean hasPermission = protection.hasPublicPermission(permission);

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

            getProtection().togglePublicPermission(permission);

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

}
