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

public class ProtectionManager {

    private Map<Material, ProtectionType> protectionTypes;


    public ProtectionManager() {
        protectionTypes = new HashMap<>();

        loadProtectionType(DeinProtectConfigKey.PROTECTIONS_CONTAINER, ProtectionType.CONTAINER);
        loadProtectionType(DeinProtectConfigKey.PROTECTIONS_DOOR, ProtectionType.DOOR);
        loadProtectionType(DeinProtectConfigKey.PROTECTIONS_INTERACTABLE, ProtectionType.INTERACTABLE);
    }


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


    public void saveProtection(Protection protection) {
        DeinProtectPlugin.getPlugin().getMongoDB().getDatastore()
                .save(protection);
    }

    public void deleteProtection(Protection protection) {
        DeinProtectPlugin.getPlugin().getMongoDB().getDatastore()
                .delete(protection);
    }

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

    public Protection getProtection(BlockLocation location) {
        return DeinProtectPlugin.getPlugin().getMongoDB().getDatastore()
                .createQuery(Protection.class)
                .field("parts")
                .hasThisOne(location)
                .get();
    }

    public boolean isProtectable(Block block) {
        return protectionTypes.containsKey(block.getType());
    }

}
