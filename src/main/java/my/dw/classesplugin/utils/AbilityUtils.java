package my.dw.classesplugin.utils;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.model.abilities.ListenedAbility;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbilityUtils {

    public static final Map<ItemStack, ActiveAbility> ITEM_TRIGGER_TO_ACTIVE_ABILITY;
    public static final Map<ItemStack, ArrowAbility> ARROW_TRIGGER_TO_ARROW_ABILITY;
    public static final Map<String, ArrowAbility> ABILITY_NAME_TO_ARROW_ABILITY;
    public static final Map<Ability, String> ABILITY_TO_CLASS_NAME;
    public static final List<ListenedAbility> LISTENED_ABILITIES;

    static {
        ITEM_TRIGGER_TO_ACTIVE_ABILITY = Arrays.stream(Class.values())
            .flatMap(c -> c.getActiveAbilities().stream())
            .collect(Collectors.toMap(ActiveAbility::getItemTrigger, a -> a));
        ARROW_TRIGGER_TO_ARROW_ABILITY = Arrays.stream(Class.values())
            .flatMap(c -> c.getArrowAbilities().stream())
            .collect(Collectors.toMap(ArrowAbility::getArrowTrigger, a -> a));
        ABILITY_NAME_TO_ARROW_ABILITY = Arrays.stream(Class.values())
            .flatMap(c -> c.getArrowAbilities().stream())
            .collect(Collectors.toMap(ArrowAbility::getName, a -> a));
        ABILITY_TO_CLASS_NAME = new HashMap<>();
        Arrays.stream(Class.values())
            .forEach(c -> c.getAbilities().forEach(a -> ABILITY_TO_CLASS_NAME.put(a, c.name())));
        LISTENED_ABILITIES = Arrays.stream(Class.values())
            .flatMap(c -> c.getAbilities().stream())
            .filter(a -> a instanceof ListenedAbility)
            .map(a -> (ListenedAbility) a)
            .collect(Collectors.toList());
    }

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
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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
        return generatePotionMetaTrigger(material, displayName, effects, color, lore, List.of());
    }

    public static ItemStack generatePotionMetaTrigger(final Material material,
                                                      final String displayName,
                                                      final List<PotionEffect> effects,
                                                      final Color color,
                                                      final List<String> lore,
                                                      final List<ItemFlag> itemFlags) {
        final ItemStack itemTrigger = new ItemStack(material);
        final PotionMeta itemMeta = (PotionMeta) itemTrigger.getItemMeta();
        effects.forEach(e -> itemMeta.addCustomEffect(e, false));
        itemMeta.setColor(color);
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemFlags.forEach(itemMeta::addItemFlags);
        itemTrigger.setItemMeta(itemMeta);

        return itemTrigger;
    }

    public static void removeItemsFromPlayer(final PlayerInventory inventory, final ItemStack... items) {
        for (ItemStack item: items) {
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
