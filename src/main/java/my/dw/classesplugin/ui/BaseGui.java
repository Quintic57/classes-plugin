package my.dw.classesplugin.ui;

public abstract class BaseGui extends InventoryGui {

    public BaseGui(final String name, final int inventorySize) {
        super(name, GuiType.UNIQUE, inventorySize);
    }

    @Override
    protected void clearInventory() {
        getInventory().clear();
    }

}
