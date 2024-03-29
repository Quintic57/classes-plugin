package my.dw.classesplugin.command;

import my.dw.classesplugin.model.Class;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//TODO: have this target players in the args
public class UnequipClassCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;

            if (!Class.isClassEquipped(player)) {
                player.sendMessage("You currently do not have a class equipped");
                return true;
            }

            final String className = player.getMetadata(Class.CLASS_METADATA_KEY).get(0).asString();
            try {
                Class.valueOf(className).unequipClass(player);
            } catch (final IllegalArgumentException e) {
                player.sendMessage("Provided classname " + className + " is not a valid classname");
                return false;
            }

            return true;
        }

        return false;
    }

}
