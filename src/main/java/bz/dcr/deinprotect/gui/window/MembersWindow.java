package bz.dcr.deinprotect.gui.window;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.gui.window.permission.player.ContainerPermissionWindow;
import bz.dcr.deinprotect.gui.window.permission.player.DoorPermissionWindow;
import bz.dcr.deinprotect.gui.window.permission.player.InteractablePermissionWindow;
import bz.dcr.deinprotect.gui.window.permission.player.PermissionWindow;
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
import xyz.upperlevel.spigot.gui.GuiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MembersWindow extends CustomGui {

    private Protection protection;

    private List<UUID> slotStorage;


    public MembersWindow(Protection protection, Player player) {
        super(
                9 * 6,
                DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_MEMBERS_TITLE, false)
        );

        this.protection = protection;
        this.slotStorage = new ArrayList<>();

        // Add skulls to inventory
        Bukkit.getScheduler().runTaskAsynchronously(DeinProtectPlugin.getPlugin(), () -> {
            protection.getMembers().forEach(member -> {
                // Get player name
                final String playerName = DeinProtectPlugin.getPlugin().getDcCore()
                        .getIdentificationProvider().getName(member.getPlayerId());

                // Player not found
                if (playerName == null) {
                    return;
                }

                // Add to slot storage
                slotStorage.add(member.getPlayerId());

                // Add skull to inventory
                Bukkit.getScheduler().runTask(DeinProtectPlugin.getPlugin(), () -> {
                    addItem(buildMemberItem(playerName));
                });
            });

            // Open GUI
            Bukkit.getScheduler().runTask(DeinProtectPlugin.getPlugin(), () -> GuiManager.open(player, this));
        });
    }


    @Override
    public void onClick(InventoryClickEvent event) {
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

        PermissionWindow permissionWindow;

        // Create Permission GUI
        if (protection.getType() == ProtectionType.CONTAINER) {
            permissionWindow = new ContainerPermissionWindow(protection, member.getPlayerId());
        } else if (protection.getType() == ProtectionType.DOOR) {
            permissionWindow = new DoorPermissionWindow(protection, member.getPlayerId());
        } else {
            permissionWindow = new InteractablePermissionWindow(protection, member.getPlayerId());
        }

        // Open permission GUI
        GuiManager.open(player, permissionWindow);
    }


    private ItemStack buildMemberItem(String playerName) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName("§e" + playerName);
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
