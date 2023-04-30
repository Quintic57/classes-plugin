package my.dw.classesplugin.model;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public enum Armor {
    ASSASSIN(),
    SWORDSMAN(
        Material.IRON_HELMET,
        Material.IRON_CHESTPLATE,
        Material.IRON_LEGGINGS,
        Material.IRON_BOOTS,
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
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
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
    ),
    SPECIALIST(),
    SCOUT(Material.LEATHER_HELMET,
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3),
        Material.LEATHER_CHESTPLATE,
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3),
        Material.LEATHER_LEGGINGS,
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3),
        Material.LEATHER_BOOTS,
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.PROTECTION_FALL, 4)
    ),
    CAPTAIN(),
    ALCHEMIST(
        Material.GOLDEN_HELMET,
        Material.GOLDEN_CHESTPLATE,
        Material.GOLDEN_LEGGINGS,
        Material.GOLDEN_BOOTS,
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
    ),
    KNIGHT(),
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
