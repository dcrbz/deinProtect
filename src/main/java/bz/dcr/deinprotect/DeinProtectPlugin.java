package bz.dcr.deinprotect;

import bz.dcr.deinprotect.cmd.DeinProtectCommand;
import bz.dcr.deinprotect.config.DeinProtectConfigKey;
import bz.dcr.deinprotect.lang.LangManager;
import bz.dcr.deinprotect.listener.*;
import bz.dcr.deinprotect.listener.worldedit.DeinProtectBlocksHubLogger;
import bz.dcr.deinprotect.persistence.JsonFilePersistence;
import bz.dcr.deinprotect.persistence.Persistence;
import bz.dcr.deinprotect.protection.KeyItemProvider;
import bz.dcr.deinprotect.protection.ProtectionManager;
import bz.dcr.deinprotect.util.identity.IdentityProvider;
import bz.dcr.deinprotect.util.identity.LocalIdentityProvider;
import bz.dcr.deinprotect.util.prompt.AbstractPromptManager;
import bz.dcr.deinprotect.util.prompt.PromptManager;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.primesoft.blockshub.BlocksHubBukkit;

import java.io.File;
import java.io.IOException;

public class DeinProtectPlugin extends JavaPlugin {

    private static DeinProtectPlugin plugin;

    private Persistence persistence;

    private IdentityProvider identityProvider;
    private AbstractPromptManager promptManager;

    private LangManager langManager;
    private BlocksHubBukkit blocksHub;
    private KeyItemProvider keyItemProvider;
    private ProtectionManager protectionManager;

    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();

        identityProvider = new LocalIdentityProvider();
        promptManager = new PromptManager(this);

        setupLangManager();
        try {
            setupPersistence();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize persistence", e);
        }
        setupBlocksHub();

        keyItemProvider = new KeyItemProvider();
        protectionManager = new ProtectionManager();

        // Register event listeners
        registerListeners();

        // Register SmartInvs InventoryManager
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        // Register BlocksHub logger
        getBlocksHub().getApi().registerBlocksLogger(new DeinProtectBlocksHubLogger(this));

        // Register command
        getCommand("deinprotect").setExecutor(new DeinProtectCommand(this));
    }

    @Override
    public void onDisable() {
        if (persistence != null) {
            try {
                persistence.close();
            } catch (IOException e) {
                getLogger().severe("Failed to close database connection.");
                e.printStackTrace();
            }
        }
    }


    private void setupLangManager() {
        final File langFile = new File(getDataFolder(), "lang.yml");

        // Create file if not existing
        if (!langFile.exists()) {
            try {
                langFile.getParentFile().mkdirs();
                saveResource("lang.yml", false);
            } catch (Exception ex) {
                ex.printStackTrace();
                getLogger().warning("Could not create language file (lang.yml)!");
                getServer().getPluginManager().disablePlugin(this);
            }
        }

        // Load configuration from file
        final Configuration langConfig = YamlConfiguration.loadConfiguration(langFile);

        // Initialize LangManager
        langManager = new LangManager(langConfig);
    }

    /**
     * Connect to the configured database
     */
    private void setupPersistence() throws IOException {
        /*persistence = new MySQLPersistence(
                getConfig().getString(DeinProtectConfigKey.MYSQL_URI),
                getConfig().getString(DeinProtectConfigKey.MYSQL_USERNAME),
                getConfig().getString(DeinProtectConfigKey.MYSQL_PASSWORD)
        );*/
        persistence = new JsonFilePersistence(
                new File(getDataFolder(), "protections.json"),
                getConfig().getLong(DeinProtectConfigKey.STORAGE_SAVE_INTERVAL)
        );
    }

    private void setupBlocksHub() {
        Plugin blocksHubPlugin = getServer().getPluginManager().getPlugin("BlocksHub");

        if (blocksHubPlugin == null) {
            getLogger().warning("Could not find BlocksHub!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        blocksHub = (BlocksHubBukkit) blocksHubPlugin;
    }

    /**
     * Register all event listeners
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new KeyItemListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
    }


    public Persistence getPersistence() {
        return persistence;
    }

    public IdentityProvider getIdentityProvider() {
        return identityProvider;
    }

    public AbstractPromptManager getPromptManager() {
        return promptManager;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    public BlocksHubBukkit getBlocksHub() {
        return blocksHub;
    }

    public KeyItemProvider getKeyItemProvider() {
        return keyItemProvider;
    }

    public ProtectionManager getProtectionManager() {
        return protectionManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public static DeinProtectPlugin getPlugin() {
        return plugin;
    }

}
