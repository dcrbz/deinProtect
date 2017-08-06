package bz.dcr.deinprotect.gui.window;

import bz.dcr.dccore.commons.identification.CorePlayer;
import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.upperlevel.spigot.gui.CustomGui;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MembersWindow extends CustomGui {

    private Protection protection;

    private Map<String, UUID> nameStorage;


    public MembersWindow(Protection protection) {
        super(
                9 * 6,
                DeinProtectPlugin.getPlugin().getLangManager().getMessage(LangKey.GUI_MEMBERS_TITLE, false)
        );

        this.protection = protection;
        this.nameStorage = new HashMap<>();

        // Add skulls to inventory
        protection.getMembers().values().parallelStream().forEach(member -> {
            // Get CorePlayer
            final Optional<CorePlayer> corePlayer = DeinProtectPlugin.getPlugin().getDcCore()
                    .getPlayerManager().getCorePlayer(member.getPlayerId());

            // Player not found
            if (!corePlayer.isPresent()) {
                return;
            }

            // Add to player storage
            nameStorage.put(corePlayer.get().getName(), corePlayer.get().getUuid());

            // Add skull to inventory
            Bukkit.getScheduler().runTask(DeinProtectPlugin.getPlugin(), () -> {
                addItem(buildMemberItem(corePlayer.get().getName()));
            });
        });
    }


    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        final ItemStack item = event.getCurrentItem();

        // Not a player skull
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        // Not a skull item
        if (item.getType() != Material.SKULL_ITEM) {
            return;
        }

        final ItemMeta itemMeta = item.getItemMeta();

        // No ItemMeta or SkullMeta
        if (itemMeta == null || !(itemMeta instanceof SkullMeta)) {
            return;
        }

        // Get SkullMeta
        final SkullMeta skullMeta = (SkullMeta) itemMeta;

        // Get player UUID
        final UUID playerId = nameStorage.get(skullMeta.getOwner());

        // TODO: Finish members GUI
        event.getWhoClicked().sendMessage("Player ID: " + playerId.toString());
    }


    private ItemStack buildMemberItem(String playerName) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        skullMeta.setOwner(playerName);
        itemStack.setItemMeta(skullMeta);

        return itemStack;
    }

}
