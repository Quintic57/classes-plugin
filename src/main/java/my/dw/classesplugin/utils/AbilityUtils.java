package my.dw.classesplugin.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class AbilityUtils {

    public static ItemStack generateItemMetaTrigger(final Material material, final String displayName) {
        return generateItemMetaTrigger(material, displayName, List.of("No description implemented"));
    }

    public static ItemStack generateItemMetaTrigger(final Material material,
                                                    final String displayName,
                                                    final List<String> lore) {
        return generateItemMetaTrigger(material, displayName, lore, List.of());
    }

    public static ItemStack generateItemMetaTrigger(final Material material,
                                                    final String displayName,
                                                    final List<String> lore,
                                                    final List<ItemFlag> itemFlags) {
        final ItemStack itemTrigger = new ItemStack(material);
        final ItemMeta itemMeta = itemTrigger.getItemMeta();
        itemMeta.addEnchant(Enchantment.UNBREAKING, 1, false);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemFlags.forEach(itemMeta::addItemFlags);
        itemTrigger.setItemMeta(itemMeta);

        return itemTrigger;
    }

    public static ItemStack generatePotionMetaTrigger(final Material material,
                                                         final String displayName,
                                                         final List<PotionEffect> effects,
                                                         final Color color) {
        return generatePotionMetaTrigger(material, displayName, effects, color, List.of("No description implemented"));
    }

    public static ItemStack generatePotionMetaTrigger(final Material material,
                                                      final String displayName,
                                                      final List<PotionEffect> effects,
                                                      final Color color,
                                                      final List<String> lore) {
        return generatePotionMetaTrigger(material, displayName, effects, color, lore, 1);
    }

    public static ItemStack generatePotionMetaTrigger(final Material material,
                                                      final String displayName,
                                                      final List<PotionEffect> effects,
                                                      final Color color,
                                                      final List<String> lore,
                                                      final int amount) {
        return generatePotionMetaTrigger(material, displayName, effects, color, lore, amount, List.of());
    }

    public static ItemStack generatePotionMetaTrigger(final Material material,
                                                      final String displayName,
                                                      final List<PotionEffect> effects,
                                                      final Color color,
                                                      final List<String> lore,
                                                      final int amount,
                                                      final List<ItemFlag> itemFlags) {
        final ItemStack itemTrigger = new ItemStack(material);
        final PotionMeta itemMeta = (PotionMeta) itemTrigger.getItemMeta();
        effects.forEach(e -> itemMeta.addCustomEffect(e, false));
        itemMeta.setColor(color);
        itemMeta.addEnchant(Enchantment.UNBREAKING, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemFlags.forEach(itemMeta::addItemFlags);
        itemTrigger.setItemMeta(itemMeta);
        itemTrigger.setAmount(amount);

        return itemTrigger;
    }

    public static void addItemForPlayer(final PlayerInventory inventory, final ItemStack item) {
        if (inventory.getItemInOffHand().isSimilar(item)) {
            inventory.getItemInOffHand().setAmount(inventory.getItemInOffHand().getAmount() + item.getAmount());
        } else {
            inventory.addItem(item);
        }
    }

    public static void removeItemsFromPlayer(final PlayerInventory inventory, final ItemStack... items) {
        for (final ItemStack item: items) {
            if (inventory.getItemInOffHand().equals(item)) {
                inventory.setItemInOffHand(null);
                break;
            }
        }
        inventory.removeItem(items);
    }

    public static Duration durationElapsedSinceInstant(final Instant instant) {
        return Duration.between(instant, Instant.now());
    }

}
