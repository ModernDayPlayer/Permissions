package me.legitmodern.PermissionsAPI.API;

import me.legitmodern.PermissionsAPI.Objects.Group;
import me.legitmodern.PermissionsAPI.Objects.User;

import java.util.UUID;

public class PermissionsAPI {

    /**
     * Get the user that you defined
     *
     * @param username Name of user to get
     * @return User
     */
    public static User getUser(UUID username) {
        return new User(username);
    }

    /**
     * Get the group that you defined
     *
     * @param groupname Name of group to get
     * @return Group
     */
    public static Group getGroup(String groupname) {
        return new Group(groupname);
    }

}
