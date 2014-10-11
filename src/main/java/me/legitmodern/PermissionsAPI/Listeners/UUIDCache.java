package me.legitmodern.PermissionsAPI.Listeners;

import me.legitmodern.PermissionsAPI.Utils.UUID.UUIDFetcher;
import me.legitmodern.PermissionsAPI.Utils.UUID.UUIDUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

public class UUIDCache implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws Exception {
        Player player = e.getPlayer();
        UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(player.getName()));
        UUIDUtility.getUUIDs().put(player.getName(), fetcher.call().get(player.getName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUIDUtility.getUUIDs().remove(e.getPlayer().getName());
    }
}
