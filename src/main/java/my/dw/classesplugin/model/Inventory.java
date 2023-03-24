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
import java.util.Arrays;
import java.util.List;

public class Inventory {

    public static List<ItemStack> assassin() {
        final ItemStack blank = null;

        final ItemStack lingeringBlindness = new ItemStack(Material.LINGERING_POTION, 3);
        final PotionMeta lingeringBlindnessMeta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.LINGERING_POTION);
        lingeringBlindnessMeta.addCustomEffect(
                new PotionEffect(PotionEffectType.BLINDNESS, 5 * Constants.TICKS_PER_SECOND, 0), false);
        lingeringBlindnessMeta.setColor(Color.BLACK);
        lingeringBlindnessMeta.setDisplayName("Lingering Potion of Blindness");
        lingeringBlindness.setItemMeta(lingeringBlindnessMeta);

        final ItemStack enderPearl = new ItemStack(Material.ENDER_PEARL);

        final ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        final ItemMeta crossbowMeta = Bukkit.getItemFactory().getItemMeta(Material.CROSSBOW);
        crossbowMeta.setUnbreakable(true);
        crossbow.setItemMeta(crossbowMeta);

        final ItemStack crossbowBolts = new ItemStack(Material.TIPPED_ARROW, 5);
        final PotionMeta crossbowBoltsMeta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.TIPPED_ARROW);
        crossbowBoltsMeta.addCustomEffect(
                new PotionEffect(PotionEffectType.POISON, 10 * Constants.TICKS_PER_SECOND, 0), false);
        crossbowBoltsMeta.setColor(Color.GREEN);
        crossbowBoltsMeta.setDisplayName("Arrow of Poison");
        crossbowBolts.setItemMeta(crossbowBoltsMeta);

        return new ArrayList<>(Arrays.asList(blank, lingeringBlindness, enderPearl, crossbow, crossbowBolts));
    }

}
