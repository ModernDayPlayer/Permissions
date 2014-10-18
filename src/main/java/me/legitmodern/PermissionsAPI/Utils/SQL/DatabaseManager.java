package me.legitmodern.PermissionsAPI.Utils.SQL;

import me.legitmodern.PermissionsAPI.Main;
import me.legitmodern.PermissionsAPI.Objects.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseManager extends DatabaseConnection {

    public DatabaseManager(DatabaseConnectionFactory factory) {
        super(factory);
    }

    private static DatabaseManager instance = new DatabaseManager(
        DatabaseConnectionFactory.builder()
                .withHost(Main.getInstance().Config.getString("MySQL.Address"))
                .withPort(Main.getInstance().Config.getInt("MySQL.Port"))
                .withDatabase(Main.getInstance().Config.getString("MySQL.Database"))
                .withUsername(Main.getInstance().Config.getString("MySQL.Username"))
                .withPassword(Main.getInstance().Config.getString("MySQL.Password"))
        );

    public static DatabaseManager getInstance() {
        return instance;
    }

    /**
     * Check the Database
     */
    public void checkDatabase() {
        try {
            this.getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS permissionDataP(uuid VARCHAR(255), groupname VARCHAR(255), selfpermissions LONGTEXT)");
            this.getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS permissionDataG(groupname VARCHAR(255), permissions LONGTEXT)");

            ResultSet rs = this.getPreparedStatement("SELECT COUNT(*) FROM permissionDataG WHERE groupname='default';").executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                this.getConnection().createStatement().execute("INSERT INTO permissionDataG (groupname, permissions) VALUES('default', '')");
            }

            ResultSet rs1 = this.getPreparedStatement("SELECT COUNT(*) FROM permissionDataG WHERE groupname='Owner';").executeQuery();
            rs1.next();
            if (rs1.getInt(1) == 0) {
                this.getConnection().createStatement().execute("INSERT INTO permissionDataG (groupname, permissions) VALUES('Owner', '')");
            }

            Main.getInstance().getLogger().log(Level.INFO, "Successfully connected to the PermissionsAPI database!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();

            System.out.println("--------------------------------------------");
            System.out.println("*FATAL* Could not connect to the PermissionsAPI database! Disabling plugin... *FATAL*");
            System.out.println("--------------------------------------------");
            Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
        }
    }

    /**
     * Enter a new player into the database
     *
     * @param player Player to enter
     */
    public void enterNewPlayer(UUID player) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT COUNT(*) FROM permissionDataP WHERE uuid='" + player.toString() + "';").executeQuery();
            rs.next();

            if (rs.getInt(1) != 1) {
                this.getPreparedStatement("INSERT INTO permissionDataP (uuid, groupname, selfpermissions) VALUES('" + player.toString() + "', 'default', '')").execute();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if a player exists in the database
     *
     * @param player Player to check
     * @return Does player exist
     */
    public boolean doesPlayerExist(UUID player) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT COUNT(*) FROM permissionDataP WHERE uuid='" + player.toString() + "';").executeQuery();
            rs.next();

            return rs.getInt(1) == 1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get the group of a player
     *
     * @param player Player to get group from
     * @return Player's group
     */
    public Group getPlayerGroup(UUID player) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT groupname FROM permissionDataP WHERE uuid='" + player.toString() + "';").executeQuery();

            if (rs.next()) {
                return new Group(rs.getString(1));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a player's permissions as a list
     *
     * @param player Player to get permissions from
     * @return Player's permissions
     */
    public List<String> getSelfPermissions(UUID player) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT selfpermissions FROM permissionDataP WHERE uuid='" + player.toString() + "';").executeQuery();

            if (rs.next()) {
                return rs.getString(1).contains(",") ? Arrays.asList(rs.getString(1).split(",")) : Arrays.asList(rs.getString(1));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the PermissionsAPI for the specified group
     *
     * @param group Name of group
     * @return PermissionsAPI for group
     */
    public List<String> getGroupPermissions(String group) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT permissions FROM permissionDataG WHERE groupname='" + group + "';").executeQuery();

            if (rs.next()) {
                return rs.getString(1).contains(",") ? Arrays.asList(rs.getString(1).split(",")) : Arrays.asList(rs.getString(1));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Enter a new Group into the database
     *
     * @param group Name of group
     */
    public void enterNewGroup(String group) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT COUNT(*) FROM permissionDataG WHERE groupname='" + group + "'").executeQuery();
            rs.next();

            if (rs.getInt(1) != 1) {
                this.getPreparedStatement("INSERT INTO permissionDataG (groupname, permissions) VALUES('" + group + "', '')").execute();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if a group exists in the database
     *
     * @param group Group to check
     * @return Does Group exist
     */
    public boolean doesGroupExist(String group) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT COUNT(*) FROM permissionDataG WHERE groupname='" + group + "';").executeQuery();
            rs.next();

            return rs.getInt(1) == 1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Set a player's group
     *
     * @param player Player to set group from
     * @param group  Group name to set
     */
    public void setPlayerGroup(UUID player, String group) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT groupname FROM permissionDataP WHERE uuid='" + player.toString() + "';").executeQuery();

            if (rs.next()) {
                this.getPreparedStatement("UPDATE permissionDataP SET groupname='" + group + "' WHERE uuid='" + player.toString() + "';").execute();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}