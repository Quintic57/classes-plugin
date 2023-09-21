package my.dw.classesplugin.ui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public abstract class ChildGui extends InventoryGui {

    protected final InventoryGui parentGui;

    public ChildGui(final String name,
                    final GuiType type,
                    final int inventorySize,
                    final InventoryGui parentGui) {
        super(name, type, inventorySize);
        this.parentGui = parentGui;

        final ItemStack backButtonItem = createDisplayItem(Material.BARRIER, "Go Back");
        final GuiFunction backButtonFunction = event -> {
            final Player player = (Player) event.getWhoClicked();
            if (parentGui == null) {
                player.sendMessage("There is no page to go back to");
                return;
            }
            if (parentGui instanceof DynamicInventory) {
                ((DynamicInventory) parentGui).refreshInventory();
            }
            parentGui.openInventory(player);
        };
        setGuiFunction(ItemKey.generate(backButtonItem), backButtonFunction);
        getInventory().setItem(inventorySize - 1, backButtonItem);
    }

    @Override
    protected void clearInventory() {
        final ItemStack backButton = getInventory().getItem(getInventory().getSize() - 1);
        if (backButton == null) {
            throw new IllegalStateException("Back button can never be null");
        }
        getInventory().clear();
        getInventory().setItem(getInventory().getSize() - 1, backButton);
        getItemToGuiFunction().keySet().retainAll(Set.of(ItemKey.generate(backButton)));
    }

}
