package me.legitmodern.PermissionsAPI;

import me.legitmodern.PermissionsAPI.API.PermissionsAPI;
import me.legitmodern.PermissionsAPI.Objects.PermissionGroup;
import me.legitmodern.PermissionsAPI.Utils.UUID.UUIDUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class PermissionsAPIExample implements Listener {

    // This is one example of many on how to go about implementing the PermissionsAPI.
    // Documentation is also included in most areas of the project.

    private static final Map<PermissionGroup, String> chatFormats = new HashMap<>();
    static {
        chatFormats.put(PermissionGroup.DEFAULT, "&7player: ");
        chatFormats.put(PermissionGroup.PRO, "&a[Pro] player: &f");
        chatFormats.put(PermissionGroup.PRO_PLUS, "&b[Pro&a+&b] player: &f");
        chatFormats.put(PermissionGroup.HELPER, "&3[Helper] player: &f");
        chatFormats.put(PermissionGroup.MOD, "&1[Mod] player: &f");
        chatFormats.put(PermissionGroup.ADMIN, "&c[ADMIN] player: &f");
        chatFormats.put(PermissionGroup.OWNER, "&4[OWNER] player: &f");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (player.hasPermission("pmcg.vip.chatcolor")) {
            e.setMessage(colorize(e.getMessage()));
        }

        e.setFormat(colorize(chatFormats.get(PermissionsAPI.getUser(UUIDUtility.getUUID(player.getName())).getGroup().getGroupType()).replaceAll("player", player.getName())) + "%2$s");
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}

