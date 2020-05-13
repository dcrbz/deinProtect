package bz.dcr.deinprotect.gui.window;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.gui.window.permission.player.ContainerPermissionWindow;
import bz.dcr.deinprotect.gui.window.permission.player.DoorPermissionWindow;
import bz.dcr.deinprotect.gui.window.permission.player.InteractablePermissionWindow;
import bz.dcr.deinprotect.protection.ProtectionType;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.protection.entity.ProtectionMember;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MembersWindow implements InventoryProvider {

    private Protection protection;

    private List<UUID> slotStorage;


    public MembersWindow(Protection protection) {
        this.protection = protection;
        this.slotStorage = new ArrayList<>();
    }

    private ClickableItem buildMemberItem(String playerName) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName("§e" + playerName);
        }

        itemStack.setItemMeta(itemMeta);

        return ClickableItem.of(itemStack, (event) -> {
            event.setCancelled(true);

            // Get player UUID
            final UUID playerId = slotStorage.size() > event.getSlot() ? slotStorage.get(event.getSlot()) : null;

            // Clicked invalid slot
            if (playerId == null) {
                return;
            }

            final ProtectionMember member = protection.getMember(playerId);

            // Member not found
            if (member == null) {
                return;
            }

            // Get player
            final Player player = (Player) event.getWhoClicked();

            // Create Permission GUI
            if (protection.getType() == ProtectionType.CONTAINER) {
                ContainerPermissionWindow.getInventory(protection, member.getPlayerId()).open(player);
            } else if (protection.getType() == ProtectionType.DOOR) {
                DoorPermissionWindow.getInventory(protection, member.getPlayerId()).open(player);
            } else {
                InteractablePermissionWindow.getInventory(protection, member.getPlayerId()).open(player);
            }
        });
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        // Add skulls to inventory
        Bukkit.getScheduler().runTaskAsynchronously(DeinProtectPlugin.getPlugin(), () -> {
            protection.getMembers().forEach(member -> {
                // Get player name
                final String playerName = DeinProtectPlugin.getPlugin().getIdentityProvider()
                        .getName(member.getPlayerId());

                // Player not found
                if (playerName == null) {
                    return;
                }

                // Add to slot storage
                slotStorage.add(member.getPlayerId());

                // Add skull to inventory
                Bukkit.getScheduler().runTask(DeinProtectPlugin.getPlugin(), () -> {
                    inventoryContents.add(buildMemberItem(playerName));
                });
            });
        });
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    public static SmartInventory getInventory(Protection protection) {
        return SmartInventory.builder()
                .id("membersWindow")
                .provider(new MembersWindow(protection))
                .size(6, 9)
                .title(DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_MEMBERS_TITLE, false))
                .manager(DeinProtectPlugin.getPlugin().getInventoryManager())
                .build();
    }

}
