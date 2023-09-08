package my.dw.classesplugin.utils;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemStackKey {

    private final ItemStack itemStack;

    public ItemStackKey(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemStackKey)) {
            return false;
        }
        final ItemStackKey itemStackKey = (ItemStackKey) o;

        return this.itemStack.isSimilar(itemStackKey.itemStack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            itemStack.getData(),
            itemStack.getEnchantments(),
            itemStack.getItemMeta(),
            itemStack.getMaxStackSize(),
            itemStack.getTranslationKey(),
            itemStack.getType()
        );
    }

    public static ItemStackKey from(final ItemStack itemStack) {
        return new ItemStackKey(itemStack);
    }

}
