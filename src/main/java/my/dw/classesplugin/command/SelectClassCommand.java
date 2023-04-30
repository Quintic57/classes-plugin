package my.dw.classesplugin.command;

import my.dw.classesplugin.ui.SelectClassGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectClassCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            SelectClassGui.openInventory(player);

            return true;
        }

        return false;
    }

}
