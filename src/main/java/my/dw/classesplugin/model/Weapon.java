package my.dw.classesplugin.model;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public enum Weapon {
    ASSASSIN(Material.NETHERITE_SWORD, Map.of(Enchantment.DAMAGE_ALL, 5), "Kellen's Dagger"),
    SWORDSMAN(Material.IRON_SWORD, Map.of(Enchantment.DAMAGE_ALL, 3, Enchantment.FIRE_ASPECT, 1), "Excaliburn"),
    ARCHER(Material.BOW, Map.of(Enchantment.ARROW_DAMAGE, 3, Enchantment.ARROW_INFINITE, 1), "Deadeye"),
    JUGGERNAUT(Material.WOODEN_SWORD, Map.of(Enchantment.SWEEPING_EDGE, 3), "Beat Stick"),
    SPECIALIST(Material.STONE_SWORD, Map.of(), ""),
    SCOUT(Material.BOW, Map.of(Enchantment.ARROW_INFINITE, 1), "Recurve Bow"),
    CAPTAIN(Material.STONE_SWORD, Map.of(), ""),
    ALCHEMIST(Material.GOLDEN_SWORD, Map.of(Enchantment.FIRE_ASPECT, 2), "Citrinitas"),
    KNIGHT(Material.DIAMOND_SWORD, Map.of(), ""),
    SUMMONER(Material.STONE_SWORD, Map.of(), "");

    private final ItemStack weaponItemStack;

    Weapon(final Material weaponItemStack, Map<Enchantment, Integer> enchantments, final String displayName) {
        this.weaponItemStack = new ItemStack(weaponItemStack);
        final ItemMeta itemMeta = this.weaponItemStack.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.setDisplayName(displayName);
        this.weaponItemStack.setItemMeta(itemMeta);
        this.weaponItemStack.addEnchantments(enchantments);
    }

    Weapon() {
        this.weaponItemStack = new ItemStack(Material.AIR);
    }

    public ItemStack getWeaponItemStack() {
        return weaponItemStack;
    }
}
