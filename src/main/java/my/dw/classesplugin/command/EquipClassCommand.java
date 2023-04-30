package my.dw.classesplugin.command;

import my.dw.classesplugin.exception.ClassAlreadyEquippedException;
import my.dw.classesplugin.model.Class;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//TODO: have this target players in the args
public class EquipClassCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage("Expected exactly 1 argument, but got 0.");
                return false;
            }

            final String className = args[0].toUpperCase();
            try {
                final Class c = Class.valueOf(className);
                c.equipClass(player);
            } catch (final IllegalArgumentException e) {
                player.sendMessage("Provided classname " + className + " is not a valid classname");
                return false;
            } catch (final ClassAlreadyEquippedException e) {
                player.sendMessage(e.getMessage());
                return false;
            }

            return true;
        }

        return false;
    }

}
