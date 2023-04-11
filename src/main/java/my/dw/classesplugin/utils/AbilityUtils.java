package my.dw.classesplugin.utils;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbilityUtils {

    public final static Map<ItemStack, ActiveAbility> itemTriggerToActiveAbilityMap;
    public final static Map<ItemStack, ArrowAbility> arrowTriggerToArrowAbilityMap;
    public final static Map<String, ActiveAbility> activeAbilityNameToActiveAbilityMap;
    public final static Map<String, ArrowAbility> arrowAbilityNameToArrowAbilityMap;
    public final static Map<Ability, String> abilityToClassNameMap;

    static {
        itemTriggerToActiveAbilityMap = Arrays.stream(Class.values())
                .flatMap(c -> c.getActiveAbilities().stream())
                .collect(Collectors.toMap(ActiveAbility::getItemTrigger, a -> a));
        arrowTriggerToArrowAbilityMap = Arrays.stream(Class.values())
                .flatMap(c -> c.getArrowAbilities().stream())
                .collect(Collectors.toMap(ArrowAbility::getArrowTrigger, a -> a));
        activeAbilityNameToActiveAbilityMap = Arrays.stream(Class.values())
                .flatMap(c -> c.getActiveAbilities().stream())
                .collect(Collectors.toMap(ActiveAbility::getName, a -> a));
        arrowAbilityNameToArrowAbilityMap = Arrays.stream(Class.values())
            .flatMap(c -> c.getArrowAbilities().stream())
            .collect(Collectors.toMap(ArrowAbility::getName, a -> a));
        abilityToClassNameMap = new HashMap<>();
        Arrays.stream(Class.values())
            .forEach(c -> c.getAbilities().forEach(a -> abilityToClassNameMap.put(a, c.name())));
    }

    public static ItemStack generateItemMetaTrigger(final Material material, final String displayName) {
        return generateItemMetaTrigger(material, displayName, List.of(), List.of());
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
        return generatePotionMetaTrigger(material, displayName, effects, color, List.of(), List.of());
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

    public static void removeItemFromPlayer(final PlayerInventory inventory, final ItemStack item) {
        if (inventory.getItemInOffHand().equals(item)) {
            inventory.setItemInOffHand(null);
        } else {
            inventory.removeItem(item);
        }
    }

}
