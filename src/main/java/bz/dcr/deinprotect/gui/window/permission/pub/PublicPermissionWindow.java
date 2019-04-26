package bz.dcr.deinprotect.gui.window.permission.pub;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.upperlevel.spigot.gui.CustomGui;

public abstract class PublicPermissionWindow extends CustomGui {

    private Protection protection;


    public PublicPermissionWindow(int size, String title, Protection protection) {
        super(size, title);

        this.setProtection(protection);
    }


    protected ItemStack buildPermissionSwitch(ProtectionPermission permission) {
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

        return item;
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
