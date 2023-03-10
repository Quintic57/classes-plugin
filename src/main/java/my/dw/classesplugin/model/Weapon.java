package my.dw.classesplugin.model;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public enum Weapon {
    ASSASSIN(Material.NETHERITE_SWORD, Map.of(Enchantment.DAMAGE_ALL, 5)),
    SWORDSMAN(Material.IRON_SWORD, Map.of(Enchantment.DAMAGE_ALL, 3)),
    ARCHER(Material.BOW, Map.of()),
    JUGGERNAUT(Material.WOODEN_SWORD, Map.of()),
    SPECIALIST(Material.STONE_SWORD, Map.of()),
    SCOUT(Material.BOW, Map.of()),
    CAPTAIN(Material.STONE_SWORD, Map.of()),
    ALCHEMIST(Material.GOLDEN_SWORD, Map.of()),
    KNIGHT(Material.DIAMOND_SWORD, Map.of());

    private final ItemStack weapon;

    Weapon(final Material material, Map<Enchantment, Integer> enchantments) {
        this.weapon = new ItemStack(material);
        final ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(material);
        itemMeta.setUnbreakable(true);
        this.weapon.setItemMeta(itemMeta);
        this.weapon.addEnchantments(enchantments);
    }

    public ItemStack getWeapon() {
        return weapon;
    }
}
