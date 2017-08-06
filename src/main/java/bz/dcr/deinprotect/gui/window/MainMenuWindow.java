package bz.dcr.deinprotect.gui.window;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionMember;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.upperlevel.spigot.gui.CustomGui;
import xyz.upperlevel.spigot.gui.Gui;
import xyz.upperlevel.spigot.gui.GuiManager;
import xyz.upperlevel.spigot.gui.impl.anvil.AnvilInputGui;

import java.util.UUID;

public class MainMenuWindow extends CustomGui {

    private static final int SLOT_REMOVE_MEMBER = 2;
    private static final int SLOT_MEMBERS = 4;
    private static final int SLOT_ADD_MEMBER = 6;
    private static final int SLOT_DELETE = 26;

    private Protection protection;


    public MainMenuWindow(Protection protection) {
        super(
                27,
                DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_MAIN_MENU_TITLE, false)
        );

        this.protection = protection;

        setItem(SLOT_REMOVE_MEMBER, buildRemoveMemberItem());
        setItem(SLOT_MEMBERS, buildMembersItem());
        setItem(SLOT_ADD_MEMBER, buildAddMemberItem());
        setItem(SLOT_DELETE, buildDeleteItem());
    }


    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case SLOT_REMOVE_MEMBER: {
                final Gui gui = new AnvilInputGui()
                        .message(DeinProtectPlugin.getPlugin().getLangManager().getMessage(
                                LangKey.GUI_REMOVE_MEMBER_MESSAGE, false)
                        )
                        .listener((player1, s) -> {
                            final UUID playerId = DeinProtectPlugin.getPlugin().getDcCore().getIdentificationProvider()
                                    .getUUID(s);

                            // Player not found
                            if (playerId == null) {
                                player1.sendMessage(
                                        DeinProtectPlugin.getPlugin().getLangManager()
                                                .getMessage(LangKey.ERR_PLAYER_NOT_EXISTING, true)
                                );
                                return "";
                            }

                            // Player is not a member
                            if (!protection.hasMember(playerId)) {
                                player1.sendMessage(
                                        DeinProtectPlugin.getPlugin().getLangManager()
                                                .getMessage(LangKey.ERR_PLAYER_IS_NOT_MEMBER, true)
                                );
                                return "";
                            }

                            protection.removeMember(playerId);
                            DeinProtectPlugin.getPlugin().getProtectionManager().saveProtection(protection);

                            return "";
                        });
                GuiManager.open(player, gui);
                break;
            }
            case SLOT_MEMBERS: {
                GuiManager.change(player, new MembersWindow(protection));
                break;
            }
            case SLOT_ADD_MEMBER: {
                final Gui gui = new AnvilInputGui()
                        .message(DeinProtectPlugin.getPlugin().getLangManager().getMessage(
                                LangKey.GUI_ADD_MEMBER_MESSAGE, false)
                        )
                        .listener((player1, s) -> {
                            final UUID playerId = DeinProtectPlugin.getPlugin().getDcCore().getIdentificationProvider()
                                    .getUUID(s);

                            // Player not found
                            if (playerId == null) {
                                player1.sendMessage(
                                        DeinProtectPlugin.getPlugin().getLangManager()
                                                .getMessage(LangKey.ERR_PLAYER_NOT_EXISTING, true)
                                );
                                return "";
                            }

                            // Player is a member
                            if (protection.hasMember(playerId)) {
                                player1.sendMessage(
                                        DeinProtectPlugin.getPlugin().getLangManager()
                                                .getMessage(LangKey.ERR_PLAYER_IS_MEMBER, true)
                                );
                                return "";
                            }

                            protection.addMember(playerId, new ProtectionMember(playerId));
                            DeinProtectPlugin.getPlugin().getProtectionManager().saveProtection(protection);

                            return "";
                        });
                GuiManager.open(player, gui);
                break;
            }
            case SLOT_DELETE: {
                DeinProtectPlugin.getPlugin().getProtectionManager().deleteProtection(protection);
                GuiManager.close(player);
                player.sendMessage(
                        DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.PROTECTION_DELETED, true)
                );
                break;
            }
        }
    }


    private ItemStack buildRemoveMemberItem() {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_MAIN_MENU_REMOVE_MEMBER, false)
        );

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack buildMembersItem() {
        ItemStack itemStack = new ItemStack(Material.BOOKSHELF);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_MAIN_MENU_MEMBERS, false)
        );

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack buildAddMemberItem() {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS, 1, (short) 13);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_MAIN_MENU_ADD_MEMBER, false)
        );

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack buildDeleteItem() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_MAIN_MENU_DELETE_PROTECTION, false)
        );

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
