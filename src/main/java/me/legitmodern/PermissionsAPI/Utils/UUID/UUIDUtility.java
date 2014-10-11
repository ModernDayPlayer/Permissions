package me.legitmodern.PermissionsAPI.Utils.UUID;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDUtility {

    /**
     * Get a player's UUID
     *
     * @param player Player to get UUID from
     * @return Player's UUID
     */
    public static UUID getUUID(String player) {
        if (getUUIDs().containsKey(player)) {
            return getUUIDs().get(player);
        } else {
            UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(player));
            try {
                return fetcher.call().get(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static Map<String, UUID> playerUUIDs = new HashMap<>();

    /**
     * Get the map containing UUIDs
     *
     * @return Map containing player names and UUID objects
     */
    public static Map<String, UUID> getUUIDs() {
        return playerUUIDs;
    }
}
