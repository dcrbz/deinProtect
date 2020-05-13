package bz.dcr.deinprotect.util.identity;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class LocalIdentityProvider implements IdentityProvider {

    @Override
    public UUID getUUID(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return player.getUniqueId();
    }

    @Override
    public String getName(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return player.getName();
    }

}
