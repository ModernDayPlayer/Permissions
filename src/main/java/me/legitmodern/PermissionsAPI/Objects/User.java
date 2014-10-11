package me.legitmodern.PermissionsAPI.Objects;

import me.legitmodern.PermissionsAPI.Utils.SQL.DatabaseManager;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {

    private UUID name;

    public User(UUID name) {
        this.name = name;
    }

    /**
     * Get the group of the user
     *
     * @return Group of the user
     */
    public Group getGroup() {
        return DatabaseManager.getInstance().getPlayerGroup(this.name);
    }

    /**
     * Get the name of the user
     *
     * @return User's name
     */
    public String getName() {
        return Bukkit.getOfflinePlayer(this.name).getName();
    }

    /**
     * Get the permissions of the user
     *
     * @return User's permissions
     */
    public Map<String, Boolean> getPermissions() {
        Map<String, Boolean> perms = new HashMap<>();

        if (DatabaseManager.getInstance().getSelfPermissions(this.name) != null) {
            for (String permission : DatabaseManager.getInstance().getSelfPermissions(this.name)) {
                perms.put(permission, !permission.startsWith("^"));
            }
        }

        if (DatabaseManager.getInstance().getGroupPermissions(this.getGroup().getGroupType()) != null) {
            for (String permission : DatabaseManager.getInstance().getGroupPermissions(getGroup().getGroupType())) {
                if (!(perms.containsKey(permission) || perms.containsKey("^" + permission))) {
                    perms.put(permission, !permission.startsWith("^"));
                }
            }
        }

        return perms;
    }

}
