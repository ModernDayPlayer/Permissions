package me.legitmodern.PermissionsAPI.Objects;

public enum PermissionGroup {

    DEFAULT("default"), PRO("Pro"), PRO_PLUS("Pro+"), HELPER("Helper"), MOD("Moderator"), ADMIN("Admin"), OWNER("Owner");

    private String name;

    PermissionGroup(String name) {
        this.name = name;
    }

    /**
     * Get the name of the group
     *
     * @return Name of the group
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get a group from a name
     *
     * @param name Name of group
     * @return Group from name
     */
    public static PermissionGroup fromName(String name) {
        for (PermissionGroup group : values()) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }

}
