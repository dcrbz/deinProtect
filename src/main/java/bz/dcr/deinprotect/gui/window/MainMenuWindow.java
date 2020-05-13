package bz.dcr.deinprotect.gui.window;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.gui.window.permission.pub.PublicContainerPermissionWindow;
import bz.dcr.deinprotect.gui.window.permission.pub.PublicDoorPermissionWindow;
import bz.dcr.deinprotect.gui.window.permission.pub.PublicInteractablePermissionWindow;
import bz.dcr.deinprotect.protection.ProtectionType;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionMember;
import bz.dcr.deinprotect.util.prompt.AbstractPrompt;
import bz.dcr.deinprotect.util.prompt.StringPrompt;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class MainMenuWindow implements InventoryProvider {

    private static final SlotPos SLOT_MEMBERS = SlotPos.of(1, 1);
    private static final SlotPos SLOT_ADD_MEMBER = SlotPos.of(1, 3);
    private static final SlotPos SLOT_REMOVE_MEMBER = SlotPos.of(1, 5);
    private static final SlotPos SLOT_PUBLIC_PERMS = SlotPos.of(1, 7);
    private static final SlotPos SLOT_DELETE = SlotPos.of(2, 8);

    private Protection protection;

    public MainMenuWindow(Protection protection) {
        this.protection = protection;
    }

    private ClickableItem buildRemoveMemberItem() {
        ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.GUI_MAIN_MENU_REMOVE_MEMBER, false)
        );

        itemStack.setItemMeta(itemMeta);

        return ClickableItem.of(itemStack, (event) -> {
            event.setCancelled(true);

            final Player player = (Player) event.getWhoClicked();

            final StringPrompt prompt = new StringPrompt(player.getUniqueId(), 3, 16);
            prompt.setAction(new AbstractPrompt.PromptCallback<StringPrompt>() {
                @Override
                public void onSuccess(StringPrompt prompt) {
                    final UUID playerId = DeinProtectPlugin.getPlugin().getIdentityProvider()
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
            DeinProtectPlugin.getPlugin().getPromptManager().registerPrompt(prompt);

            // Close the inventory
            player.closeInventory();

            // Send prompt message
            player.sendMessage(DeinProtectPlugin.getPlugin().getLangManager()
                    .getMessage(LangKey.PROMPT_REMOVE_MEMBER, true));
        });
    }

    private ClickableItem buildMembersItem() {
        ItemStack itemStack = new ItemStack(Material.BOOKSHELF);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.GUI_MAIN_MENU_MEMBERS, false)
        );

        itemStack.setItemMeta(itemMeta);

        return ClickableItem.of(itemStack, (event) -> {
            DeinProtectPlugin.getPlugin().getLogger().info("Clicked members item");
            event.setCancelled(true);
            MembersWindow.getInventory(protection).open((Player) event.getWhoClicked());
        });
    }

    private ClickableItem buildAddMemberItem() {
        ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.GUI_MAIN_MENU_ADD_MEMBER, false)
        );

        itemStack.setItemMeta(itemMeta);

        return ClickableItem.of(itemStack, (event) -> {
            event.setCancelled(true);

            final Player player = (Player) event.getWhoClicked();

            final StringPrompt prompt = new StringPrompt(player.getUniqueId(), 3, 16);
            prompt.setAction(new AbstractPrompt.PromptCallback<StringPrompt>() {
                @Override
                public void onSuccess(StringPrompt prompt) {
                    final UUID playerId = DeinProtectPlugin.getPlugin().getIdentityProvider()
                            .getUUID(prompt.getInput());

                    // Player not found
                    if (playerId == null) {
                        player.sendMessage(
                                DeinProtectPlugin.getPlugin().getLangManager()
                                        .getMessage(LangKey.ERR_PLAYER_NOT_EXISTING, true)
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
            DeinProtectPlugin.getPlugin().getPromptManager().registerPrompt(prompt);

            // Close the inventory
            player.closeInventory();

            // Send prompt message
            player.sendMessage(DeinProtectPlugin.getPlugin().getLangManager()
                    .getMessage(LangKey.PROMPT_ADD_MEMBER, true));
        });
    }

    private ClickableItem buildPublicPermsItem() {
        ItemStack itemStack = new ItemStack(Material.GLASS);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.GUI_MAIN_MENU_PUBLIC_PERMS, false)
        );

        itemStack.setItemMeta(itemMeta);

        return ClickableItem.of(itemStack, (event) -> {
            event.setCancelled(true);

            final Player player = (Player) event.getWhoClicked();

            // Create Permission GUI
            if (protection.getType() == ProtectionType.CONTAINER) {
                PublicContainerPermissionWindow.getInventory(protection).open(player);
            } else if (protection.getType() == ProtectionType.DOOR) {
                PublicDoorPermissionWindow.getInventory(protection).open(player);
            } else {
                PublicInteractablePermissionWindow.getInventory(protection).open(player);
            }
        });
    }

    private ClickableItem buildDeleteItem() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(
                DeinProtectPlugin.getPlugin().getLangManager()
                        .getMessage(LangKey.GUI_MAIN_MENU_DELETE_PROTECTION, false)
        );

        itemStack.setItemMeta(itemMeta);

        return ClickableItem.of(itemStack, (event) -> {
            event.setCancelled(true);

            final Player player = (Player) event.getWhoClicked();

            DeinProtectPlugin.getPlugin().getProtectionManager().deleteProtection(protection);
            player.closeInventory();
            player.sendMessage(
                    DeinProtectPlugin.getPlugin().getLangManager()
                            .getMessage(LangKey.PROTECTION_DELETED, true)
            );
        });
    }


    private void saveProtection() {
        Bukkit.getScheduler().runTaskAsynchronously(DeinProtectPlugin.getPlugin(), () -> {
            DeinProtectPlugin.getPlugin().getProtectionManager().saveProtection(protection);
        });
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(SLOT_REMOVE_MEMBER, buildRemoveMemberItem());
        inventoryContents.set(SLOT_MEMBERS, buildMembersItem());
        inventoryContents.set(SLOT_ADD_MEMBER, buildAddMemberItem());
        inventoryContents.set(SLOT_PUBLIC_PERMS, buildPublicPermsItem());
        inventoryContents.set(SLOT_DELETE, buildDeleteItem());
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    public static SmartInventory getInventory(Protection protection) {
        return SmartInventory.builder()
                .id("mainMenuWindow")
                .provider(new MainMenuWindow(protection))
                .size(3, 9)
                .title(DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_MAIN_MENU_TITLE, false))
                .manager(DeinProtectPlugin.getPlugin().getInventoryManager())
                .build();
    }

}
