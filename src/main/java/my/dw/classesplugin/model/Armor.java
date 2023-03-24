package my.dw.classesplugin.model;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public enum Armor {
    ASSASSIN(),
    SWORDSMAN(),
    ARCHER(),
    JUGGERNAUT(),
    SPECIALIST(),
    SCOUT(),
    CAPTAIN(),
    ALCHEMIST(),
    KNIGHT();

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    Armor() {}

    Armor(final Material helmet,
          final Material chestplate,
          final Material leggings,
          final Material boots,
          final Map<Enchantment, Integer> enchantments) {
        this(helmet, enchantments, chestplate, enchantments, leggings, enchantments, boots, enchantments);
    }

    Armor(final Material helmetMat,
          final Map<Enchantment, Integer> helmetEnchants,
          final Material chestplateMat,
          final Map<Enchantment, Integer> chestEnchants,
          final Material leggingsMat,
          final Map<Enchantment, Integer> legEnchants,
          final Material bootsMat,
          final Map<Enchantment, Integer> bootEnchants) {
        final ItemStack helmet = new ItemStack(helmetMat);
        helmet.addEnchantments(helmetEnchants);
        final ItemStack chestplate = new ItemStack(chestplateMat);
        helmet.addEnchantments(chestEnchants);
        final ItemStack leggings = new ItemStack(leggingsMat);
        helmet.addEnchantments(legEnchants);
        final ItemStack boots = new ItemStack(bootsMat);
        helmet.addEnchantments(bootEnchants);

        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

}
