package my.dw.classesplugin.command;

import my.dw.classesplugin.ui.selectclass.SelectClassGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static my.dw.classesplugin.utils.GuiUtils.NAME_TO_UNIQUE_GUI;

public class SelectClassCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            NAME_TO_UNIQUE_GUI.get(SelectClassGui.NAME).openInventory(player);

            return true;
        }

        return false;
    }

}
