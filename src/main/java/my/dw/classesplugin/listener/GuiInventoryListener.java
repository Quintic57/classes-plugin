package my.dw.classesplugin.listener;

import my.dw.classesplugin.ui.InventoryGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static my.dw.classesplugin.utils.GuiUtils.INVENTORY_TO_GUI;

public class GuiInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        if (!isInventoryGuiEvent(event)) {
            return;
        }

        final InventoryGui gui = INVENTORY_TO_GUI.get(event.getClickedInventory());
        if (gui.handleOnInventoryClickEvent(event)) {
            event.setCancelled(true);
        }
    }

    private boolean isInventoryGuiEvent(final InventoryClickEvent event) {
        return event.getWhoClicked() instanceof Player
            && event.getClickedInventory() != null
            && INVENTORY_TO_GUI.containsKey(event.getClickedInventory())
            && event.getCurrentItem() != null;
    }

}
