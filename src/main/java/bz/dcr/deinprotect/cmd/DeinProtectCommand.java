package bz.dcr.deinprotect.cmd;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.config.LangKey;
import bz.dcr.deinprotect.config.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeinProtectCommand implements CommandExecutor {

    private DeinProtectPlugin plugin;


    public DeinProtectCommand(DeinProtectPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getLangManager().getMessage(LangKey.ERR_PLAYER_COMMAND, false));
            return true;
        }

        final Player player = (Player) sender;

        // Key command
        if (args.length == 1 && args[0].equalsIgnoreCase("key")) {
            // Player has no permission
            if (!player.hasPermission(Permission.CMD_KEY)) {
                player.sendMessage(plugin.getLangManager().getMessage(LangKey.ERR_NO_PERMISSION, false));
                return true;
            }

            // Add key to player inventory
            player.getInventory().addItem(plugin.getKeyItemProvider().getKeyItem());

            // Send status message
            player.sendMessage(plugin.getLangManager().getMessage(LangKey.KEY_GIVEN, true));
        }

        // Wrong command usage
        return false;
    }

}
