package me.legitmodern.PermissionsAPI.Commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import me.legitmodern.PermissionsAPI.Main;
import me.legitmodern.PermissionsAPI.Utils.Chat.MessageUtils;
import me.legitmodern.PermissionsAPI.Utils.PermissionManager;
import me.legitmodern.PermissionsAPI.Utils.SQL.DatabaseManager;
import me.legitmodern.PermissionsAPI.Utils.UUID.UUIDUtility;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Commands {

    @Command(
            aliases = {"permissions", "perm"},
            desc = "PermissionsAPI index command",
            max = -1,
            usage = "[arguments]"
    )
    @CommandPermissions({"permissions.admin"})
    public static void permissions(CommandContext args, CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.argsLength() == 0) {
                MessageUtils.message(player,
                        "&e&m" + MessageUtils.StringType.CHAT_BAR.getString(),
                        "&6/perm setrank [player] [rank] &e- Set a player's rank!",
                        "&6/perm addrank [rank] &e- Add a rank to the database!",
                        "&e&m" + MessageUtils.StringType.CHAT_BAR.getString()
                );

                return;
            } else if (args.argsLength() == 2) {
                if (args.getString(0).equalsIgnoreCase("addrank")) {
                    String group = args.getString(1);
                    if (!DatabaseManager.getInstance().doesGroupExist(group)) {
                        DatabaseManager.getInstance().enterNewGroup(group);
                        MessageUtils.messagePrefix(player, "Added group &6" + group + "&7!");
                    } else {
                        MessageUtils.messagePrefix(player, "&cThat group already exists!");
                    }

                    return;
                }
            } else if (args.argsLength() == 3) {
                if (args.getString(0).equalsIgnoreCase("setrank")) {
                    String target = args.getString(1);
                    UUID targetUUID = UUIDUtility.getUUID(target);
                    if (DatabaseManager.getInstance().doesPlayerExist(targetUUID)) {
                        String group = args.getString(2);
                        if (DatabaseManager.getInstance().doesGroupExist(group)) {
                            DatabaseManager.getInstance().setPlayerGroup(targetUUID, group);

                            if (Bukkit.getPlayer(target) != null) {
                                PermissionManager.permissionManagerMap.get(targetUUID.toString()).remove();
                                PermissionManager.permissionManagerMap.remove(targetUUID.toString());

                                PermissionManager permissionManager = new PermissionManager(Bukkit.getPlayer(target), Main.getInstance());
                                permissionManager.setUpPerms();
                            }

                            MessageUtils.messagePrefix(player, "Set &6" + target + " (" + targetUUID.toString() + ")&7's group to &6" + group + "&7!");
                        } else {
                            MessageUtils.messagePrefix(player, "&cThat group doesn't exist in the database!");
                        }
                    } else {
                        MessageUtils.messagePrefix(player, "&cThat player doesn't exist in the database!");
                    }

                    return;
                }
            }

            MessageUtils.message(player,
                    "&4&m" + MessageUtils.StringType.CHAT_BAR.getString(),
                    " &c&lERROR : &7Invalid usage!",
                    "&4&m" + MessageUtils.StringType.CHAT_BAR.getString()
            );
        }
    }

}
