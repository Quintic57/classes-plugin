package my.dw.classesplugin.listener;

import static my.dw.classesplugin.ui.SelectClassGui.DISPLAY_ITEM_TO_CLASS_NAME;

import my.dw.classesplugin.exception.ClassAlreadyEquippedException;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.ui.SelectClassGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class GuiInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player) || Objects.isNull(event.getCurrentItem())) {
            return;
        }

        if (event.getView().getTitle().equalsIgnoreCase(SelectClassGui.GUI_NAME)) {
            event.setCancelled(true);
            final Player player = (Player) event.getWhoClicked();
            final String className = DISPLAY_ITEM_TO_CLASS_NAME.get(event.getCurrentItem());

            if (Objects.isNull(className)) {
                player.sendMessage("This GUI item does not have an associated class to equip");
                return;
            }

            try {
                Class.valueOf(className).equipClass(player);
                player.closeInventory();
            } catch (final IllegalArgumentException e) {
                player.sendMessage("Provided classname " + className + " is not a valid classname");
            } catch (final ClassAlreadyEquippedException e) {
                player.sendMessage(e.getMessage());
            }
        }
    }

}
