package bz.dcr.deinprotect.gui.window;

import bz.dcr.dccore.commons.prompt.AbstractPrompt;
import bz.dcr.dccore.commons.prompt.StringPrompt;
import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.gui.window.permission.pub.PublicContainerPermissionWindow;
import bz.dcr.deinprotect.gui.window.permission.pub.PublicDoorPermissionWindow;
import bz.dcr.deinprotect.gui.window.permission.pub.PublicInteractablePermissionWindow;
import bz.dcr.deinprotect.gui.window.permission.pub.PublicPermissionWindow;
import bz.dcr.deinprotect.protection.ProtectionType;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionMember;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.upperlevel.spigot.gui.CustomGui;
import xyz.upperlevel.spigot.gui.Gui;
import xyz.upperlevel.spigot.gui.GuiManager;

import java.util.UUID;

public class MainMenuWindow extends CustomGui {

    private static final int SLOT_REMOVE_MEMBER = 2;
    private static final int SLOT_MEMBERS = 4;
    private static final int SLOT_ADD_MEMBER = 6;
    private static final int SLOT_PUBLIC_PERMS = 13;
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
        setItem(SLOT_PUBLIC_PERMS, buildPublicPermsItem());
        setItem(SLOT_DELETE, buildDeleteItem());
    }


    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case SLOT_REMOVE_MEMBER: {
                final StringPrompt prompt = new StringPrompt(player.getUniqueId(), 3, 16);
                prompt.setAction(new AbstractPrompt.PromptCallback<StringPrompt>() {
                    @Override
                    public void onSuccess(StringPrompt prompt) {
                        final UUID playerId = DeinProtectPlugin.getPlugin().getDcCore().getIdentificationProvider()
                                .getUUID(prompt.getInput());

                        // Player not found
                        if (playerId == null) {
                            player.sendMessage(
                                    DeinProtectPlugin.getPlugin().getLangManager()
                                            .getMessage(LangKey.ERR_PLAYER_NOT_EXISTING, true)
                            );
                            return;
                        }

                        // Player is not a member
                        if (!protection.hasMember(playerId)) {
                            player.sendMessage(
                                    DeinProtectPlugin.getPlugin().getLangManager()
                                            .getMessage(LangKey.ERR_PLAYER_IS_NOT_MEMBER, true)
                            );
                            return;
                        }

                        // Remove member from protection
                        protection.removeMember(playerId);

                        // Save protection
                        saveProtection();

                        // Send message
                        player.sendMessage(
                                DeinProtectPlugin.getPlugin().getLangManager()
                                        .getMessage(LangKey.MEMBER_REMOVED, true)
                        );
                    }

                    @Override
                    public void onFailure(StringPrompt abstractPrompt, String s) {
                        // Send error message
                        player.sendMessage(DeinProtectPlugin.getPlugin().getLangManager()
                                .getMessage(LangKey.ERR_INVALID_NAME, true));
                    }
                });

                // Register prompt
                DeinProtectPlugin.getPlugin().getDcCore().getPromptManager().registerPrompt(prompt);

                // Send prompt message
                player.sendMessage(DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.PROMPT_REMOVE_MEMBER, true));

                break;
            }
            case SLOT_MEMBERS: {
                Gui membersGui = new MembersWindow(protection, player);
                break;
            }
            case SLOT_ADD_MEMBER: {
                final StringPrompt prompt = new StringPrompt(player.getUniqueId(), 3, 16);
                prompt.setAction(new AbstractPrompt.PromptCallback<StringPrompt>() {
                    @Override
                    public void onSuccess(StringPrompt prompt) {
                        final UUID playerId = DeinProtectPlugin.getPlugin().getDcCore().getIdentificationProvider()
                                .getUUID(prompt.getInput());

                        // Player not found
                        if (playerId == null) {
                            player.sendMessage(
                                    DeinProtectPlugin.getPlugin().getLangManager()
                                            .getMessage(LangKey.ERR_PLAYER_NOT_EXISTING, true)
                            );
                            return;
                        }

                        // Player is not a member
                        if (!protection.hasMember(playerId)) {
                            player.sendMessage(
                                    DeinProtectPlugin.getPlugin().getLangManager()
                                            .getMessage(LangKey.ERR_PLAYER_IS_NOT_MEMBER, true)
                            );
                            return;
                        }

                        // Remove member from protection
                        protection.putMember(new ProtectionMember(playerId));

                        // Save protection
                        saveProtection();

                        // Send message
                        player.sendMessage(
                                DeinProtectPlugin.getPlugin().getLangManager()
                                        .getMessage(LangKey.MEMBER_ADDED, true)
                        );
                    }

                    @Override
                    public void onFailure(StringPrompt abstractPrompt, String s) {
                        // Send error message
                        player.sendMessage(DeinProtectPlugin.getPlugin().getLangManager()
                                .getMessage(LangKey.ERR_INVALID_NAME, true));
                    }
                });

                // Register prompt
                DeinProtectPlugin.getPlugin().getDcCore().getPromptManager().registerPrompt(prompt);

                // Send prompt message
                player.sendMessage(DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.PROMPT_ADD_MEMBER, true));

                break;
            }
            case SLOT_PUBLIC_PERMS: {
                PublicPermissionWindow permissionWindow;

                // Create Permission GUI
                if (protection.getType() == ProtectionType.CONTAINER) {
                    permissionWindow = new PublicContainerPermissionWindow(protection);
                } else if (protection.getType() == ProtectionType.DOOR) {
                    permissionWindow = new PublicDoorPermissionWindow(protection);
                } else {
                    permissionWindow = new PublicInteractablePermissionWindow(protection);
                }

                // Open GUI
                GuiManager.open(player, permissionWindow);
                break;
            }
            case SLOT_DELETE: {
                DeinProtectPlugin.getPlugin().getProtectionManager().deleteProtection(protection);
                GuiManager.close(player);
                player.sendMessage(
                        DeinProtectPlugin.getPlugin().getLangManager()
                                .getMessage(LangKey.PROTECTION_DELETED, true)
                );
                break;
            }
        }
    }


    private ItemStack buildRemoveMemberItem() {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.GUI_MAIN_MENU_REMOVE_MEMBER, false)
        );

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack buildMembersItem() {
        ItemStack itemStack = new ItemStack(Material.BOOKSHELF);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.GUI_MAIN_MENU_MEMBERS, false)
        );

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack buildAddMemberItem() {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS, 1, (short) 13);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.GUI_MAIN_MENU_ADD_MEMBER, false)
        );

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack buildPublicPermsItem() {
        ItemStack itemStack = new ItemStack(Material.GLASS);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.GUI_MAIN_MENU_PUBLIC_PERMS, false)
        );

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack buildDeleteItem() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.GUI_MAIN_MENU_DELETE_PROTECTION, false)
        );

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }


    private void saveProtection() {
        Bukkit.getScheduler().runTaskAsynchronously(DeinProtectPlugin.getPlugin(), () -> {
            DeinProtectPlugin.getPlugin().getProtectionManager().saveProtection(protection);
        });
    }

}
