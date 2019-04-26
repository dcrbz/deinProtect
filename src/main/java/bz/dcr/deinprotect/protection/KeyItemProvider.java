package bz.dcr.deinprotect.protection;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.DeinProtectConfigKey;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class KeyItemProvider {

    private ItemStack keyItem;
    private ShapedRecipe keyItemRecipe;


    public KeyItemProvider() {
        init();
    }


    private void init() {
        final FileConfiguration config = DeinProtectPlugin.getPlugin().getConfig();

        keyItem = loadItem(config);
        keyItemRecipe = loadRecipe(config, keyItem);

        // Register crafting recipe
        DeinProtectPlugin.getPlugin().getServer().addRecipe(keyItemRecipe);
    }

    private ItemStack loadItem(FileConfiguration config) {
        // Build ItemStack
        keyItem = new ItemStack(
                Material.valueOf(config.getString(DeinProtectConfigKey.KEY_ITEM_MATERIAL))
        );

        // Get ItemMeta
        final ItemMeta itemMeta = keyItem.getItemMeta();

        // Set Lore
        final List<String> lore = config.getStringList(DeinProtectConfigKey.KEY_ITEM_LORE).stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
        itemMeta.setLore(lore);

        // Set display name
        itemMeta.setDisplayName(
                ChatColor.translateAlternateColorCodes(
                        '&',
                        config.getString(DeinProtectConfigKey.KEY_ITEM_NAME))
        );

        // Apply ItemMeta to ItemStack
        keyItem.setItemMeta(itemMeta);

        return keyItem;
    }

    private ShapedRecipe loadRecipe(FileConfiguration config, ItemStack item) {
        // Get raw crafting format
        final List<String> craftingFormat = config.getStringList(DeinProtectConfigKey.KEY_ITEM_CRAFTING_FORMAT);

        // Create recipe
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(DeinProtectPlugin.getPlugin(), "key"), item);
        recipe.shape(craftingFormat.toArray(new String[craftingFormat.size()]));

        // Load materials
        final Set<String> configKeys = config.getConfigurationSection(
                DeinProtectConfigKey.KEY_ITEM_CRAFTING_INGREDIENTS
        ).getKeys(false);

        // Add ingredients
        for (String key : configKeys) {
            recipe.setIngredient(
                    key.charAt(0),
                    Material.getMaterial(config.getString(DeinProtectConfigKey.KEY_ITEM_CRAFTING_INGREDIENTS + "." + key))
            );
        }

        return recipe;
    }

    /**
     * Checks if the given {@link ItemStack} is a key item
     *
     * @param item The {@link ItemStack} to compare
     * @return Whether the given {@link ItemStack} is a key item or not
     */
    public boolean isKeyItem(ItemStack item) {
        // No item
        if (item == null) {
            return false;
        }

        // No ItemMeta existing
        if (!item.hasItemMeta()) {
            return false;
        }

        // Material not equal
        if (item.getType() != this.keyItem.getType()) {
            return false;
        }

        // Display name not equal
        if (!item.getItemMeta().getDisplayName().equals(this.keyItem.getItemMeta().getDisplayName())) {
            return false;
        }

        // Lore not equal
        if (!item.getItemMeta().getLore().equals(this.keyItem.getItemMeta().getLore())) {
            return false;
        }

        return true;
    }


    public ShapedRecipe getRecipe() {
        return keyItemRecipe;
    }

    public ItemStack getKeyItem() {
        return keyItem.clone();
    }

}
