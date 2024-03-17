package dk.anderz.fiske.commands.subs;

import dk.anderz.fiske.Fiske;
import dk.anderz.fiske.configuration.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadSub implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("fiske.command")) {
                sender.sendMessage("§bFiske v2.0 by _Anderz_ \n §f/fiske reload §8» §7Reloads the configuration.");
                return true;
            } else {
                sender.sendMessage("You don't have permission to use this command.");
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("fiske.reload")) {
                try {
                    long tidBefore = System.currentTimeMillis();
                    Fiske.instance.reload();
                    Messages.send(sender, "fiske.reload.successful", "\\{ms\\}", String.valueOf(System.currentTimeMillis() - tidBefore));
                } catch (Exception e) {
                    Messages.send(sender, "fiske.reload.unsuccessful", "\\{error\\}", e.toString());
                    e.printStackTrace();
                }
                return true;
            } else {
                sender.sendMessage("You don't have permission to reload the configuration.");
                return false;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
