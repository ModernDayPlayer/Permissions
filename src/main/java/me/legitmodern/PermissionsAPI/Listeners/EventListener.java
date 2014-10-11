package me.legitmodern.PermissionsAPI.Listeners;

import me.legitmodern.PermissionsAPI.Main;
import me.legitmodern.PermissionsAPI.Utils.PermissionManager;
import me.legitmodern.PermissionsAPI.Utils.SQL.DatabaseManager;
import me.legitmodern.PermissionsAPI.Utils.UUID.UUIDUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        UUID uuid = UUIDUtility.getUUID(player.getName());

        if (!DatabaseManager.getInstance().doesPlayerExist(uuid)) {
            DatabaseManager.getInstance().enterNewPlayer(uuid);
        }

        PermissionManager manager = new PermissionManager(player, Main.getInstance());
        manager.setUpPerms();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = UUIDUtility.getUUID(player.getName());

        PermissionManager manager = PermissionManager.permissionManagerMap.get(uuid.toString());
        manager.remove();

        PermissionManager.permissionManagerMap.remove(uuid.toString());
    }

}
