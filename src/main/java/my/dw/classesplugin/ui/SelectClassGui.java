package my.dw.classesplugin.ui;

import my.dw.classesplugin.model.Class;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO: With more UIs on the way, may be best to refactor this to support multiple UI inventories
public class SelectClassGui {

    public static final String GUI_NAME;

    public static final Map<ItemStack, String> DISPLAY_ITEM_TO_CLASS_NAME;

    public static final Inventory INVENTORY;

    static {
        GUI_NAME = "Select Class";
        DISPLAY_ITEM_TO_CLASS_NAME = Arrays.stream(Class.values())
            .collect(Collectors.toMap(SelectClassGui::getDisplayItem, Enum::name));
        INVENTORY = Bukkit.createInventory(null, (int) Math.ceil((Class.values().length + 1) / 9.0) * 9, GUI_NAME);
        DISPLAY_ITEM_TO_CLASS_NAME.keySet().stream()
            .sorted((Comparator.comparing(item -> ChatColor.stripColor(item.getItemMeta().getDisplayName()))))
            .forEach(INVENTORY::addItem);
    }

    private static ItemStack getDisplayItem(final Class clazz) {
        switch (clazz) {
            case ASSASSIN:
                return createDisplayItem(
                    Material.LEATHER,
                    ChatColor.DARK_RED + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses stealth, smoke bombs, and teleports to confuse enemies",
                        "Strengths: Strong single-target damage output, invisibility, high mobility",
                        "Weaknesses: No armor, weak AOE capability, poor range"
                    )
                );
            case ALCHEMIST:
                return createDisplayItem(
                    Material.GOLD_INGOT,
                    ChatColor.GOLD + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses potions to damage/disable enemies",
                        "Strengths: Strong against heavily-armored enemies, good AOE capability",
                        "Weaknesses: Lightly armored, poor range"
                    )
                );
            case ARCHER:
                return createDisplayItem(
                    Material.BOW,
                    ChatColor.LIGHT_PURPLE + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses a bow and arrows to damage/disable enemies",
                        "Strengths: Can toolbox against every kind of enemy, excellent range",
                        "Weaknesses: Lightly armored, weak in close-quarter combat"
                    )
                );
            case JUGGERNAUT:
                return createDisplayItem(
                    Material.NETHERITE_CHESTPLATE,
                    ChatColor.BLUE + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses heavy armor to absorb damage from enemy attacks",
                        "Strengths: Heavily armored, excellent survivability",
                        "Weaknesses: Weak offensive capability, slow movement speed"
                    )
                );
            case SCOUT:
                return createDisplayItem(
                    Material.FEATHER,
                    ChatColor.GREEN + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses mobility skills to quickly traverse the map",
                        "Strengths: Excellent horizontal and vertical mobility, good team utility",
                        "Weaknesses: Lightly armored, weak offensive capability"
                    )
                );
            case SWORDSMAN:
                return createDisplayItem(
                    Material.IRON_HELMET,
                    ChatColor.GRAY + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses a sword and shield to combat enemies",
                        "Strengths: Strong in close-quarter combat, good survivability",
                        "Weaknesses: Poor range"
                    )
                );
            case CAPTAIN:
            case KNIGHT:
            case SPECIALIST:
            default:
                return createDisplayItem(
                    clazz.getWeapon().getWeaponItemStack().getType(),
                    clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of("No description implemented")
                );
        }

    }

    private static ItemStack createDisplayItem(final Material itemMaterial,
                                               final String displayName,
                                               final List<String> lore) {
        final ItemStack displayItem = new ItemStack(itemMaterial);
        final ItemMeta meta = displayItem.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        displayItem.setItemMeta(meta);

        return displayItem;
    }

    public static void openInventory(final HumanEntity entity) {
        entity.openInventory(INVENTORY);
    }

}
