package me.legitmodern.PermissionsAPI.Utils;

import me.legitmodern.PermissionsAPI.Main;
import me.legitmodern.PermissionsAPI.Objects.User;
import me.legitmodern.PermissionsAPI.Utils.UUID.UUIDUtility;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.Map;

public class PermissionManager {

    public static Map<String, PermissionManager> permissionManagerMap = new HashMap<>();

    public PermissionAttachment perms;
    public Player player;
    public User user;

    public PermissionManager(Player player, Main perms) {
        this.player = player;
        this.perms = player.addAttachment(perms);
        this.user = new User(UUIDUtility.getUUID(player.getName()));

        permissionManagerMap.put(UUIDUtility.getUUID(player.getName()).toString(), this);
    }

    /**
     * Set up permissions
     */
    public void setUpPerms() {
        Map<String, Boolean> perms = user.getPermissions();
        Object[] permissions = perms.keySet().toArray();
        for (Object permission : permissions) {
            String perm = String.valueOf(permission);
            this.givePermission(perm, perms.get(perm));
        }
    }

    /**
     * Get the player from the PermissionManager
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the PermissionAttachment
     */
    public PermissionAttachment getPermissions() {
        return this.perms;
    }

    /**
     * Give a permission to the player
     */
    public void givePermission(String permission, boolean flag) {
        getPermissions().setPermission(permission, flag);
        getPlayer().recalculatePermissions();
    }

    /**
     * Remove the PermissionAttachment from the player
     */
    public void remove() {
        for (String key : getPermissions().getPermissions().keySet()) {
            getPermissions().unsetPermission(key);
        }
        getPlayer().removeAttachment(getPermissions());
        getPlayer().recalculatePermissions();
    }

    /**
     * Remove a permission node from the attachment
     */
    public void removePermissions(String permission) {
        getPermissions().setPermission(permission, false);
        getPlayer().recalculatePermissions();
    }

}
