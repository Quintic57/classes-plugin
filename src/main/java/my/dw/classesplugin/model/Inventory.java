package my.dw.classesplugin.model;

import my.dw.classesplugin.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    public static List<ItemStack> getFullInventory(final Class clazz) {
        switch (clazz) {
            case ASSASSIN:
                return assassin();
            case SWORDSMAN:
                return swordsman();
            case ARCHER:
                return archer();
            case ALCHEMIST:
            case JUGGERNAUT:
            case SPECIALIST:
            case SCOUT:
            case CAPTAIN:
            case KNIGHT:
            default:
                return defaultInventory(clazz);
        }
    }

    private static List<ItemStack> archer() {
        final List<ItemStack> inventory = new ArrayList<>();

        // Weapon
        inventory.add(Class.ARCHER.getWeapon().getWeaponItemStack());
        // Arrow - By design, this makes it so the standard arrow is chosen by default when shooting
        inventory.add(new ItemStack(Material.ARROW));
        // Active Ability Triggers
        Class.ARCHER.getActiveAbilities().forEach(a -> inventory.add(a.getItemTrigger()));
        // Arrow Ability Triggers
        Class.ARCHER.getArrowAbilities().forEach(a -> inventory.add(a.getArrowTrigger()));

        return inventory;
    }

    public static List<ItemStack> assassin() {
        final List<ItemStack> inventory = defaultInventory(Class.ASSASSIN);

        // Crossbow
        final ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        final ItemMeta crossbowMeta = crossbow.getItemMeta();
        crossbowMeta.setUnbreakable(true);
        crossbow.setItemMeta(crossbowMeta);
        inventory.add(crossbow);
        // Crossbow Bolts
        final ItemStack crossbowBolts = new ItemStack(Material.TIPPED_ARROW, 3);
        final PotionMeta crossbowBoltsMeta = (PotionMeta) crossbowBolts.getItemMeta();
        crossbowBoltsMeta.addCustomEffect(
            new PotionEffect(PotionEffectType.POISON, 10 * Constants.TICKS_PER_SECOND, 0), false);
        crossbowBoltsMeta.setColor(Color.GREEN);
        crossbowBoltsMeta.setDisplayName("Arrow of Poison");
        crossbowBolts.setItemMeta(crossbowBoltsMeta);
        inventory.add(crossbowBolts);

        return inventory;
    }

    public static List<ItemStack> swordsman() {
        final List<ItemStack> inventory = defaultInventory(Class.SWORDSMAN);

        final ItemStack shield = new ItemStack(Material.SHIELD);
        final ItemMeta shieldMeta = shield.getItemMeta();
        shieldMeta.setUnbreakable(true);
        shield.setItemMeta(shieldMeta);
        inventory.add(shield);

        return inventory;
    }

    private static List<ItemStack> defaultInventory(final Class clazz) {
        final List<ItemStack> inventory = new ArrayList<>();

        // Weapon
        inventory.add(clazz.getWeapon().getWeaponItemStack());
        // Active Ability Triggers
        clazz.getActiveAbilities().forEach(a -> inventory.add(a.getItemTrigger()));
        // Arrow Ability Triggers
        clazz.getArrowAbilities().forEach(a -> inventory.add(a.getArrowTrigger()));

        return inventory;
    }

}
