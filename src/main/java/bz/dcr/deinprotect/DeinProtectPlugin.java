package bz.dcr.deinprotect;

import bz.dcr.dccore.commons.db.MongoDB;
import bz.dcr.deinprotect.Config.DeinProtectConfigKey;
import bz.dcr.deinprotect.protection.KeyItemProvider;
import bz.dcr.deinprotect.protection.ProtectionManager;
import com.mongodb.MongoClientURI;
import org.bukkit.plugin.java.JavaPlugin;

public class DeinProtectPlugin extends JavaPlugin {

    private static DeinProtectPlugin plugin;

    private MongoDB mongoDB;
    private KeyItemProvider keyItemProvider;
    private ProtectionManager protectionManager;


    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        setupMongoDB();

        keyItemProvider = new KeyItemProvider();
        protectionManager = new ProtectionManager();
    }

    @Override
    public void onDisable() {
        if(mongoDB != null) {
            mongoDB.close();
        }
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


    public MongoDB getMongoDB() {
        return mongoDB;
    }

    public KeyItemProvider getKeyItemProvider() {
        return keyItemProvider;
    }

    public ProtectionManager getProtectionManager() {
        return protectionManager;
    }


    public static DeinProtectPlugin getPlugin() {
        return plugin;
    }

}
