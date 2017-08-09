package bz.dcr.deinprotect;

import bz.dcr.dccore.DcCorePlugin;
import bz.dcr.dccore.commons.db.MongoDB;
import bz.dcr.deinprotect.config.DeinProtectConfigKey;
import bz.dcr.deinprotect.gui.GUIManager;
import bz.dcr.deinprotect.lang.LangManager;
import bz.dcr.deinprotect.listener.BlockBreakListener;
import bz.dcr.deinprotect.listener.InteractListener;
import bz.dcr.deinprotect.listener.InventoryListener;
import bz.dcr.deinprotect.protection.KeyItemProvider;
import bz.dcr.deinprotect.protection.ProtectionManager;
import com.mongodb.MongoClientURI;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DeinProtectPlugin extends JavaPlugin {

    private static DeinProtectPlugin plugin;

    private LangManager langManager;
    private DcCorePlugin dcCore;
    private MongoDB mongoDB;
    private KeyItemProvider keyItemProvider;
    private ProtectionManager protectionManager;
    private GUIManager guiManager;


    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        setupLangManager();
        setupDcCore();
        setupMongoDB();

        keyItemProvider = new KeyItemProvider();
        protectionManager = new ProtectionManager();
        guiManager = new GUIManager();

        registerListeners();
    }

    @Override
    public void onDisable() {
        if(mongoDB != null) {
            mongoDB.close();
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

    private void setupDcCore() {
        Plugin dcCorePlugin = getServer().getPluginManager().getPlugin("dcCore");

        if (dcCorePlugin == null) {
            getLogger().warning("Could not find dcCore!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        dcCore = (DcCorePlugin) dcCorePlugin;
    }

    /**
     * Connect to the configured database
     */
    private void setupMongoDB() {
        final MongoClientURI uri = new MongoClientURI(
                getConfig().getString(DeinProtectConfigKey.MONGODB_URI)
        );

        mongoDB = new MongoDB(uri, getClassLoader());
    }

    /**
     * Register all event listeners
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
    }


    public LangManager getLangManager() {
        return langManager;
    }

    public DcCorePlugin getDcCore() {
        return dcCore;
    }

    public MongoDB getMongoDB() {
        return mongoDB;
    }

    public KeyItemProvider getKeyItemProvider() {
        return keyItemProvider;
    }

    public ProtectionManager getProtectionManager() {
        return protectionManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }


    public static DeinProtectPlugin getPlugin() {
        return plugin;
    }

}
