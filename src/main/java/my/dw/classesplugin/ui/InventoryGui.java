package my.dw.classesplugin.ui;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: Should prob move this to a common library since it's used by both plugins
public abstract class InventoryGui {

    private final String name;
    private final GuiType type;
    private final Inventory inventory;
    private final Map<ItemKey, GuiFunction> itemToGuiFunction;
    private final Set<ChildGui> childGuis;

    public InventoryGui(final String name,
                        final GuiType type,
                        final int inventorySize) {
        this.name = name;
        this.type = type;
        this.inventory = Bukkit.createInventory(null, Math.min(inventorySize, 54), name);
        this.itemToGuiFunction = new HashMap<>();
        this.childGuis = new LinkedHashSet<>();
    }

    public String getName() {
        return name;
    }

    public GuiType getType() {
        return type;
    }

    public Inventory getInventory() {
        return inventory;
    }

    protected Map<ItemKey, GuiFunction> getItemToGuiFunction() {
        return itemToGuiFunction;
    }

    protected void setGuiFunction(final ItemKey itemKey, final GuiFunction guiFunction) {
        itemToGuiFunction.put(itemKey, guiFunction);
    }

    protected void addGuiItem(final ItemStack guiItem, final GuiFunction guiFunction) {
        itemToGuiFunction.put(ItemKey.generate(guiItem), guiFunction);
        inventory.addItem(guiItem);
    }

    public Set<ChildGui> getChildGuis() {
        return childGuis;
    }

    protected void addChildGui(final ChildGui childGui) {
        childGuis.add(childGui);
    }

    public void openInventory(final HumanEntity entity) {
        if (this instanceof DynamicInventory) {
            ((DynamicInventory) this).refreshInventory();
        }
        entity.openInventory(inventory);
    }

    public boolean handleOnInventoryClickEvent(final InventoryClickEvent event) {
        final GuiFunction guiFunction = itemToGuiFunction.get(ItemKey.generate(event.getCurrentItem()));
        if (guiFunction == null) {
            event.getWhoClicked().sendMessage("There is no functionality implemented for this button");
            return false;
        }

        guiFunction.execute(event);
        return true;
    }

    protected static ItemStack createDisplayItem(final Material itemMaterial,
                                                 final String displayName) {
        return createDisplayItem(itemMaterial, displayName, List.of());
    }

    protected static ItemStack createDisplayItem(final Material itemMaterial,
                                                 final String displayName,
                                                 final List<String> description) {
        final ItemStack displayItem = new ItemStack(itemMaterial);
        final ItemMeta meta = displayItem.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(description);
        meta.addEnchant(Enchantment.UNBREAKING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        displayItem.setItemMeta(meta);

        return displayItem;
    }

    protected static ItemStack createPotionDisplayItem(final Material itemMaterial,
                                                       final Color color,
                                                       final String displayName,
                                                       final List<String> description) {
        final ItemStack displayItem = new ItemStack(itemMaterial);
        final PotionMeta meta = (PotionMeta) displayItem.getItemMeta();
        meta.setColor(color);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setDisplayName(displayName);
        meta.setLore(description);
        meta.addEnchant(Enchantment.UNBREAKING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        displayItem.setItemMeta(meta);

        return displayItem;
    }

    protected abstract void clearInventory();

}
