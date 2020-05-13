package bz.dcr.deinprotect.util.identity;

import java.util.UUID;

public interface IdentityProvider {

    /**
     * Get a player's UUID by his name
     * @param name The player's name
     * @return The player's UUID
     */
    UUID getUUID(String name);

    /**
     * Get a player's name by his UUID
     * @param uuid The player's UUID
     * @return The player's name
     */
    String getName(UUID uuid);

}
