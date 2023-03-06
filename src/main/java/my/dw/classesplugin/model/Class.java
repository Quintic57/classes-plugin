package my.dw.classesplugin.model;

import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.AbilityType;
import my.dw.classesplugin.model.abilities.AssassinCloakAbility;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Class {
    ASSASSIN(List.of(new AssassinCloakAbility())),
    SWORDSMAN(List.of()),
    ARCHER(List.of()),
    JUGGERNAUT(List.of()),
    SPECIALIST(List.of()),
    SCOUT(List.of()),
    CAPTAIN(List.of()),
    ALCHEMIST(List.of()),
    KNIGHT(List.of());

    // TODO: Add inventory for each class, and implement all respective abilities
    private final List<Material> inventory;
    private final List<Ability> abilities;

    Class(List<Ability> abilities) {
        this.inventory = new ArrayList<>();
        this.abilities = abilities;
    }

    public List<Material> getInventory() {
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
