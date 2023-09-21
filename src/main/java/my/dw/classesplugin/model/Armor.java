package my.dw.classesplugin.model;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Map;

public enum Armor {
    ASSASSIN(),
    SWORDSMAN(
        Material.IRON_HELMET,
        Material.DIAMOND_CHESTPLATE,
        Material.IRON_LEGGINGS,
        Material.IRON_BOOTS,
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
    ),
    ARCHER(
        Material.CHAINMAIL_HELMET,
        Material.CHAINMAIL_CHESTPLATE,
        Material.CHAINMAIL_LEGGINGS,
        Material.CHAINMAIL_BOOTS,
        Map.of(Enchantment.PROTECTION_PROJECTILE, 3)
    ),
    JUGGERNAUT(
        Material.NETHERITE_HELMET,
        Material.NETHERITE_CHESTPLATE,
        Material.NETHERITE_LEGGINGS,
        Material.NETHERITE_BOOTS,
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
    ),
    SPECIALIST(),
    SCOUT(
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1),
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1),
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1),
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1, Enchantment.PROTECTION_FALL, 4),
        Color.BLACK
    ),
    CAPTAIN(),
    ALCHEMIST(
        Material.GOLDEN_HELMET,
        Material.GOLDEN_CHESTPLATE,
        Material.GOLDEN_LEGGINGS,
        Material.GOLDEN_BOOTS,
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
    ),
    KNIGHT(
        Material.DIAMOND_HELMET,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_BOOTS,
        Map.of()
    ),
    SUMMONER(
        Material.LEATHER_HELMET,
        Material.LEATHER_CHESTPLATE,
        Material.LEATHER_LEGGINGS,
        Material.LEATHER_BOOTS,
        Map.of()
    );

    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;

    Armor() {
        this.helmet = new ItemStack(Material.AIR);
        this.chestplate = new ItemStack(Material.AIR);
        this.leggings = new ItemStack(Material.AIR);
        this.boots = new ItemStack(Material.AIR);
    }

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
        final ItemMeta helmetMeta = helmet.getItemMeta();
        helmetMeta.setUnbreakable(true);
        helmet.setItemMeta(helmetMeta);
        helmet.addEnchantments(helmetEnchants);

        final ItemStack chestplate = new ItemStack(chestplateMat);
        final ItemMeta chestplateMeta = chestplate.getItemMeta();
        chestplateMeta.setUnbreakable(true);
        chestplate.setItemMeta(chestplateMeta);
        chestplate.addEnchantments(chestEnchants);

        final ItemStack leggings = new ItemStack(leggingsMat);
        final ItemMeta leggingsMeta = leggings.getItemMeta();
        leggingsMeta.setUnbreakable(true);
        leggings.setItemMeta(leggingsMeta);
        leggings.addEnchantments(legEnchants);

        final ItemStack boots = new ItemStack(bootsMat);
        final ItemMeta bootsMeta = boots.getItemMeta();
        bootsMeta.setUnbreakable(true);
        boots.setItemMeta(bootsMeta);
        boots.addEnchantments(bootEnchants);

        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    // Leather Armor Constructor
    Armor(final Map<Enchantment, Integer> helmetEnchants,
          final Map<Enchantment, Integer> chestEnchants,
          final Map<Enchantment, Integer> legEnchants,
          final Map<Enchantment, Integer> bootEnchants,
          final Color color) {
        final ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        final LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setUnbreakable(true);
        helmetMeta.setColor(color);
        helmetMeta.addItemFlags(ItemFlag.HIDE_DYE);
        helmet.setItemMeta(helmetMeta);
        helmet.addEnchantments(helmetEnchants);

        final ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        final LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateMeta.setUnbreakable(true);
        chestplateMeta.setColor(color);
        chestplateMeta.addItemFlags(ItemFlag.HIDE_DYE);
        chestplate.setItemMeta(chestplateMeta);
        chestplate.addEnchantments(chestEnchants);

        final ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        final LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsMeta.setUnbreakable(true);
        leggingsMeta.setColor(color);
        leggingsMeta.addItemFlags(ItemFlag.HIDE_DYE);
        leggings.setItemMeta(leggingsMeta);
        leggings.addEnchantments(legEnchants);

        final ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        final LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setUnbreakable(true);
        bootsMeta.setColor(color);
        bootsMeta.addItemFlags(ItemFlag.HIDE_DYE);
        boots.setItemMeta(bootsMeta);
        boots.addEnchantments(bootEnchants);

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
