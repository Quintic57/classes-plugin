package my.dw.classesplugin.ui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemKey {

    private final Material material;
    private final String displayName;

    public ItemKey(final Material material, final String displayName) {
        this.material = material;
        this.displayName = displayName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemKey)) {
            return false;
        }
        final ItemKey itemKey = (ItemKey) o;
        return material == itemKey.material && displayName.equals(itemKey.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, displayName);
    }

    public static ItemKey generate(final ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            throw new IllegalArgumentException("Item Meta can not be null");
        }
        return new ItemKey(itemStack.getType(), itemStack.getItemMeta().getDisplayName());
    }

}
