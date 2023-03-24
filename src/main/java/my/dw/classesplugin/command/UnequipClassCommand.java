package my.dw.classesplugin.command;

import my.dw.classesplugin.model.Class;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

//TODO: have this target players in the args
public class UnequipClassCommand implements CommandExecutor {

    private final Plugin plugin;

    public UnequipClassCommand(final Plugin plugin) {
        this.plugin = plugin;
    }

    // Add arguments to allow un-setting of different classes
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.getMetadata("character_class").isEmpty()
                    || Objects.isNull(player.getMetadata("character_class").get(0))) {
                player.sendMessage("You currently do not have a class equipped");
                return true;
            }

            final String className = player.getMetadata("character_class").get(0).asString();
            player.removeMetadata("character_class", plugin);
            Class.valueOf(className).unequipClass(player);

            return true;
        }

        return false;
    }
}
