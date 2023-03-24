package my.dw.classesplugin.model;

import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.PassiveAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinArmorAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinCloakAbility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum Class {

    ASSASSIN(
        Weapon.ASSASSIN,
        Armor.ASSASSIN,
        Inventory.assassin(),
        List.of(new AssassinCloakAbility(), new AssassinArmorAbility())
    ),
    SWORDSMAN(
        Weapon.SWORDSMAN,
        Armor.SWORDSMAN,
        List.of(),
        List.of()
    ),
    ARCHER(
        Weapon.ARCHER,
        Armor.ARCHER,
        List.of(),
        List.of()
    ),
    JUGGERNAUT(
        Weapon.JUGGERNAUT,
        Armor.JUGGERNAUT,
        List.of(),
        List.of()
    ),
    SPECIALIST(
        Weapon.SPECIALIST,
        Armor.SPECIALIST,
        List.of(),
        List.of()
    ),
    SCOUT(
        Weapon.SCOUT,
        Armor.SCOUT,
        List.of(),
        List.of()
    ),
    CAPTAIN(
        Weapon.CAPTAIN,
        Armor.CAPTAIN,
        List.of(),
        List.of()
    ),
    ALCHEMIST(
        Weapon.ALCHEMIST,
        Armor.ALCHEMIST,
        List.of(),
        List.of()
    ),
    KNIGHT(
        Weapon.KNIGHT,
        Armor.KNIGHT,
        List.of(),
        List.of()
    );

    private final Weapon weapon;
    private final Armor armor;
    private final List<ItemStack> inventory;
    private final List<Ability> abilities;

    Class(final Weapon weapon,
          final Armor armor,
          final List<ItemStack> inventory,
          final List<Ability> abilities) {
        this.weapon = weapon;
        this.armor = armor;
        this.inventory = inventory;
        this.abilities = abilities;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getArmor() {
        return armor;
    }

    public List<ItemStack> getInventory() {
        return inventory;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public List<ActiveAbility> getActiveAbilities() {
        return abilities.stream()
            .filter(a -> a instanceof ActiveAbility)
            .map(a -> (ActiveAbility) a)
            .collect(Collectors.toList());
    }

    public List<PassiveAbility> getPassiveAbilities() {
        return abilities.stream()
            .filter(a -> a instanceof PassiveAbility)
            .map(a -> (PassiveAbility) a)
            .collect(Collectors.toList());
    }

    public void equipClass(final Player player) {
        final PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear();
        // Weapon
        playerInventory.setItem(0, this.weapon.getWeapon());
        // Armor
        playerInventory.setItem(EquipmentSlot.HEAD, this.armor.getHelmet());
        playerInventory.setItem(EquipmentSlot.CHEST, this.armor.getChestplate());
        playerInventory.setItem(EquipmentSlot.LEGS, this.armor.getLeggings());
        playerInventory.setItem(EquipmentSlot.FEET, this.armor.getBoots());
        // Active Ability Inventory Triggers
        int inventoryIndex = 1;
        for (ActiveAbility a: this.getActiveAbilities()) {
            playerInventory.setItem(inventoryIndex, a.getItemTrigger());
            inventoryIndex++;
        }
        // Misc Inventory. Null item is treated as an empty space in the hot bar
        for (ItemStack item: this.inventory) {
            if (!Objects.isNull(item)) {
                playerInventory.setItem(inventoryIndex, item);
            }
            inventoryIndex++;
        }
        // Passive (Unconditional) Abilities
        this.getPassiveAbilities().stream().filter(a -> !a.isConditional()).forEach(a -> a.handleAbility(player));
    }

    public void unequipClass(final Player player) {
        final PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear();
        // Remove Passive Abilities
        this.getPassiveAbilities().stream().filter(a -> !a.isConditional()).forEach(a -> a.removeAbility(player));
    }

}
