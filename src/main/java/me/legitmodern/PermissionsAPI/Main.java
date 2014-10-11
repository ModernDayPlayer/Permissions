package me.legitmodern.PermissionsAPI;

import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import me.legitmodern.PermissionsAPI.Commands.Commands;
import me.legitmodern.PermissionsAPI.Listeners.EventListener;
import me.legitmodern.PermissionsAPI.Listeners.UUIDCache;
import me.legitmodern.PermissionsAPI.Utils.ConfigManager;
import me.legitmodern.PermissionsAPI.Utils.SQL.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private static Main instance;
    public static Main getInstance() {
        return instance;
    }

    private ConfigManager conifgManager;
    public File configFile;
    public FileConfiguration Config;

    private CommandsManager commandsManager;
    private CommandsManagerRegistration commandsRegistration;

    @Override
    public void onEnable() {
        instance = this;

        this.conifgManager = new ConfigManager(this);
        this.commandsManager = new BukkitCommandsManager();
        this.commandsRegistration = new CommandsManagerRegistration(this, this.commandsManager);

        System.out.println("--------------------------------------------");
        System.out.println("Welcome to PermissionsAPI v" + getDescription().getName() + "!");
        System.out.println("--------------------------------------------");

        configFile = new File(getDataFolder(), "config.yml");
        conifgManager.addFile(configFile);

        try {
            conifgManager.firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Config = conifgManager.getConfigFile("config.yml");

        conifgManager.load();
        conifgManager.save();

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getPluginManager().registerEvents(new UUIDCache(), this);

        commandsRegistration.register(Commands.class);

        getLogger().log(Level.INFO, "Attempting to connect to the PermissionsAPI database...");
        DatabaseManager.getInstance().checkDatabase();

        System.out.println("--------------------------------------------");
        System.out.println("The PermissionsAPI plugin has successfully enabled!");
        System.out.println("--------------------------------------------");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commandsManager.execute(cmd.getName(), args, sender, sender);
            return true;
        } catch (CommandPermissionsException e) {
            sender.sendMessage(colorize("&c&lERROR: &cYou do not have permission to use that command!"));
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(colorize(e.getUsage()));
        } catch (CommandUsageException e) {
            sender.sendMessage(colorize("&c&lERROR: &cIncorrect arguments! Usage: &6" + e.getUsage()));
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(colorize("&c&lERROR: &cA number was expected!"));
            } else {
                sender.sendMessage(colorize("&c&lERROR: &cAn unknown error has occurred!"));
            }
        } catch (CommandException e) {
            sender.sendMessage(colorize(e.getMessage()));
        }

        return true;
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
