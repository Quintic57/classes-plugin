package my.dw.classesplugin.ui;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface GuiFunction {

    void execute(final InventoryClickEvent event) ;

}
