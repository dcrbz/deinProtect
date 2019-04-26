package bz.dcr.deinprotect.protection;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.config.DeinProtectConfigKey;
import bz.dcr.deinprotect.protection.entity.Protection;
import bz.dcr.deinprotect.util.MultiPartUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@link ProtectionManager} manages saving, loading and modifying
 * Protections.
 */
public class ProtectionManager {

    private Map<Material, ProtectionType> protectionTypes;


    public ProtectionManager() {
        protectionTypes = new HashMap<>();

        loadProtectionType(DeinProtectConfigKey.PROTECTIONS_CONTAINER, ProtectionType.CONTAINER);
        loadProtectionType(DeinProtectConfigKey.PROTECTIONS_DOOR, ProtectionType.DOOR);
        loadProtectionType(DeinProtectConfigKey.PROTECTIONS_INTERACTABLE, ProtectionType.INTERACTABLE);
    }


    /**
     * Load a {@link ProtectionType} from the plugin configuration
     *
     * @param typePath The path to the protection type list
     * @param type     The type the block types are related to
     */
    private void loadProtectionType(String typePath, ProtectionType type) {
        final FileConfiguration config = DeinProtectPlugin.getPlugin().getConfig();

        Material material;
        for (String materialName : config.getStringList(typePath)) {
            material = Material.getMaterial(materialName);

            // Invalid Material
            if (material == null) {
                DeinProtectPlugin.getPlugin().getLogger().warning(materialName + " is not a valid material!");
                continue;
            }

            protectionTypes.put(material, type);
        }
    }


    /**
     * Save a single {@link Protection} to the database
     *
     * @param protection The {@link Protection} to save
     */
    public void saveProtection(Protection protection) {
        DeinProtectPlugin.getPlugin().getMongoDB().getDatastore()
                .save(protection);
    }

    /**
     * Delete a single {@link Protection} from the database
     *
     * @param protection The {@link Protection} to delete
     */
    public void deleteProtection(Protection protection) {
        DeinProtectPlugin.getPlugin().getMongoDB().getDatastore()
                .delete(protection);
    }

    /**
     * Delete a single part of a {@link Protection}
     *
     * @param location The {@link BlockLocation} of the protection part
     */
    public void deleteProtectionPart(BlockLocation location) {
        final Protection protection = getProtection(location);

        if (protection.getParts().size() == 1) {
            // Delete protection
            deleteProtection(protection);
        } else if (protection.getParts().size() > 1) {
            // Delete entire door
            if (protection.getType() == ProtectionType.DOOR) {
                deleteProtection(protection);
            }

            // Remove part from protection
            protection.removePart(location);

            // Save protection
            saveProtection(protection);
        }
    }

    /**
     * Create a new {@link Protection}
     *
     * @param owner    The owner of the {@link Protection}
     * @param location The {@link BlockLocation} of the protection
     * @return The newly created {@link Protection}
     */
    public Protection createProtection(Player owner, BlockLocation location) {
        // Get type of protection
        final ProtectionType type = protectionTypes.get(location.getBlock().getType());

        // Block is not protectable
        if (type == null) {
            return null;
        }

        // Get parts of protection
        final List<BlockLocation> parts = MultiPartUtil.getParts(location.getBlock())
                .stream()
                .map(BlockLocation::new)
                .collect(Collectors.toList());

        // Create new protection
        final Protection protection = new Protection(owner.getUniqueId(), type, parts);

        // Save protection
        saveProtection(protection);

        return protection;
    }

    /**
     * Get a {@link Protection} by it's {@link BlockLocation}
     *
     * @param location The {@link BlockLocation} of the {@link Protection}
     * @return The {@link Protection} at the specified {@link BlockLocation}
     */
    public Protection getProtection(BlockLocation location) {
        return DeinProtectPlugin.getPlugin().getMongoDB().getDatastore()
                .createQuery(Protection.class)
                .field("parts")
                .hasThisOne(location)
                .get();
    }

    /**
     * Check whether a {@link Block} is protectable or not
     *
     * @param block The {@link Block} to check
     * @return Whether the specified {@link Block} is protectable or not
     */
    public boolean isProtectable(Block block) {
        if (block == null) {
            return false;
        }

        return protectionTypes.containsKey(block.getType());
    }

}
