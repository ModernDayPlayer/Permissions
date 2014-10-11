package me.legitmodern.PermissionsAPI.Objects;

import me.legitmodern.PermissionsAPI.Utils.SQL.DatabaseManager;

import java.util.List;

public class Group {

    private PermissionGroup group;

    public Group(PermissionGroup group) {
        this.group = group;
    }

    /**
     * Get the name of the group
     *
     * @return Group Name
     */
    public PermissionGroup getGroupType() {
        return this.group;
    }

    /**
     * Get the permissions for the group
     *
     * @return Group PermissionsAPI
     */
    public List<String> getPermissions() {
        return DatabaseManager.getInstance().getGroupPermissions(getGroupType());
    }
}