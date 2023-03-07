package my.dw.classesplugin.model;

import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.AbilityType;
import my.dw.classesplugin.model.abilities.AssassinCloakAbility;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Class {
    ASSASSIN(
            Weapon.ASSASSIN.getWeapon(),
            List.of(),
            List.of(),
            List.of(new AssassinCloakAbility())
    ),
    SWORDSMAN(
            Weapon.SWORDSMAN.getWeapon(),
            List.of(),
            List.of(),
            List.of()
    ),
    ARCHER(Weapon.ARCHER.getWeapon(),
            List.of(),
            List.of(),
            List.of()
    ),
    JUGGERNAUT(Weapon.JUGGERNAUT.getWeapon(),
            List.of(),
            List.of(),
            List.of()
    ),
    SPECIALIST(Weapon.SPECIALIST.getWeapon(),
            List.of(),
            List.of(),
            List.of()
    ),
    SCOUT(Weapon.SCOUT.getWeapon(),
            List.of(),
            List.of(),
            List.of()
    ),
    CAPTAIN(Weapon.CAPTAIN.getWeapon(),
            List.of(),
            List.of(),
            List.of()
    ),
    ALCHEMIST(Weapon.ALCHEMIST.getWeapon(),
            List.of(),
            List.of(),
            List.of()
    ),
    KNIGHT(Weapon.KNIGHT.getWeapon(),
            List.of(),
            List.of(),
            List.of()
    );

    // TODO: Add inventory for each class, and implement all respective abilities
    private final ItemStack weapon;
    private final List<ItemStack> armor;
    private final List<ItemStack> inventory;
    private final List<Ability> abilities;

    Class(ItemStack weapon, List<ItemStack> armor, List<ItemStack> inventory, List<Ability> abilities) {
        this.weapon = weapon;
        this.armor = armor;
        this.inventory = new ArrayList<>();
        this.abilities = abilities;
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public List<ItemStack> getArmor() {
        return armor;
    }

    public List<ItemStack> getInventory() {
        return inventory;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public static Map<String, Ability> generateItemTriggerToAbilityMap() {
        final Map<String, Ability> itemTriggerToAbility = new HashMap<>();
        for (Class c: Class.values()) {
            c.getAbilities().stream().filter(a -> a.getType().equals(AbilityType.ACTIVE)).forEach(a ->
                    itemTriggerToAbility.put(a.getItemTrigger(), a)
            );
        }
        return itemTriggerToAbility;
    }
}
