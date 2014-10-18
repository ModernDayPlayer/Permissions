package me.legitmodern.PermissionsAPI.Objects;

import me.legitmodern.PermissionsAPI.Utils.SQL.DatabaseManager;

import java.util.List;

public class Group {

    private String group;

    public Group(String group) {
        this.group = group;
    }

    /**
     * Get the name of the group
     *
     * @return Group Name
     */
    public String getName() {
        return this.group;
    }

    /**
     * Get the permissions for the group
     *
     * @return Group PermissionsAPI
     */
    public List<String> getPermissions() {
        return DatabaseManager.getInstance().getGroupPermissions(getName());
    }
}