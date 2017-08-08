package bz.dcr.deinprotect.gui.window.permission;

import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionMember;
import bz.dcr.deinprotect.protection.entity.ProtectionPermission;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.upperlevel.spigot.gui.CustomGui;

import java.util.UUID;

public abstract class PermissionWindow extends CustomGui {

    private Protection protection;
    private UUID memberId;
    private ProtectionMember member;


    public PermissionWindow(int size, String title, Protection protection, UUID memberId) {
        super(size, title);

        this.setProtection(protection);
        this.setMemberId(memberId);
    }


    protected ItemStack buildPermissionSwitch(ProtectionPermission permission) {
        final ProtectionMember member = protection.getMember(memberId);

        if (member == null) {
            return null;
        }

        ItemStack item = (member.hasPermission(permission)) ? buildOnItem() : buildOffItem();
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(permission.toString());

        item.setItemMeta(itemMeta);

        return item;
    }

    private ItemStack buildOnItem() {
        return new ItemStack(Material.CONCRETE,1, (short) 5);
    }

    private ItemStack buildOffItem() {
        return new ItemStack(Material.CONCRETE,1, (short) 14);
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
