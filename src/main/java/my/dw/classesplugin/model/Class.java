package my.dw.classesplugin.model;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.model.abilities.PassiveAbility;
import my.dw.classesplugin.model.abilities.alchemist.AlchemistDamageAbility;
import my.dw.classesplugin.model.abilities.alchemist.AlchemistDisAbility;
import my.dw.classesplugin.model.abilities.alchemist.AlchemistPoisonAbility;
import my.dw.classesplugin.model.abilities.archer.ArcherExplosiveAbility;
import my.dw.classesplugin.model.abilities.archer.ArcherIceAbility;
import my.dw.classesplugin.model.abilities.archer.ArcherPoisonAbility;
import my.dw.classesplugin.model.abilities.archer.ArcherReconAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinArmorAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinCloakAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinSmokeAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinTeleportAbility;
import my.dw.classesplugin.model.abilities.juggernaut.JuggernautArmorAbility;
import my.dw.classesplugin.model.abilities.juggernaut.JuggernautInvulnerAbility;
import my.dw.classesplugin.model.abilities.swordsman.SwordsmanBerserkAbility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum Class {

    ASSASSIN(
        Weapon.ASSASSIN,
        Armor.ASSASSIN,
        List.of(
            new AssassinCloakAbility(),
            new AssassinSmokeAbility(),
            new AssassinTeleportAbility(),
            new AssassinArmorAbility()
        )
    ),
    SWORDSMAN(
        Weapon.SWORDSMAN,
        Armor.SWORDSMAN,
        List.of(new SwordsmanBerserkAbility())
    ),
    ARCHER(
        Weapon.ARCHER,
        Armor.ARCHER,
        List.of(
            new ArcherPoisonAbility(),
            new ArcherIceAbility(),
            new ArcherExplosiveAbility(),
            new ArcherReconAbility()
        )
    ),
    JUGGERNAUT(
        Weapon.JUGGERNAUT,
        Armor.JUGGERNAUT,
        List.of(new JuggernautArmorAbility(), new JuggernautInvulnerAbility())
    ),
    SPECIALIST(
        Weapon.SPECIALIST,
        Armor.SPECIALIST,
        List.of()
    ),
    SCOUT(
        Weapon.SCOUT,
        Armor.SCOUT,
        List.of()
    ),
    CAPTAIN(
        Weapon.CAPTAIN,
        Armor.CAPTAIN,
        List.of()
    ),
    ALCHEMIST(
        Weapon.ALCHEMIST,
        Armor.ALCHEMIST,
        List.of(new AlchemistDamageAbility(), new AlchemistPoisonAbility(), new AlchemistDisAbility())
    ),
    KNIGHT(
        Weapon.KNIGHT,
        Armor.KNIGHT,
        List.of()
    );

    private final Weapon weapon;
    private final Armor armor;
    private final List<Ability> abilities;

    public final static String CLASS_METADATA_KEY = "character_class";

    Class(final Weapon weapon,
          final Armor armor,
          final List<Ability> abilities) {
        this.weapon = weapon;
        this.armor = armor;
        this.abilities = abilities;
    }

    public Weapon getWeapon() {
        return weapon;
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

    public List<ArrowAbility> getArrowAbilities() {
        return abilities.stream()
            .filter(a -> a instanceof ArrowAbility)
            .map(a -> (ArrowAbility) a)
            .collect(Collectors.toList());
    }

    public List<PassiveAbility> getPassiveAbilities() {
        return abilities.stream()
            .filter(a -> a instanceof PassiveAbility)
            .map(a -> (PassiveAbility) a)
            .collect(Collectors.toList());
    }

    public void equipClass(final Player player) {
        if (isClassEquipped(player)) {
            final String className = player.getMetadata(CLASS_METADATA_KEY).get(0).asString();
            unequipClass(player, Class.valueOf(className));
        }

        player.setMetadata(CLASS_METADATA_KEY,
            new FixedMetadataValue(ClassesPlugin.getPlugin(), this.name()));

        final PlayerInventory playerInventory = player.getInventory();
        // Armor
        playerInventory.setItem(EquipmentSlot.HEAD, this.armor.getHelmet());
        playerInventory.setItem(EquipmentSlot.CHEST, this.armor.getChestplate());
        playerInventory.setItem(EquipmentSlot.LEGS, this.armor.getLeggings());
        playerInventory.setItem(EquipmentSlot.FEET, this.armor.getBoots());
        // Full Inventory
        final List<ItemStack> fullInventory = Inventory.getFullInventory(this);
        fullInventory.forEach(playerInventory::addItem);
        // Passive (Unconditional) Abilities
        this.getPassiveAbilities().forEach(a -> a.handleAbility(player));
    }

    public static void unequipClass(final Player player, final Class equippedClass) {
        player.getInventory().clear();
        // Remove Passive Abilities
        equippedClass.getPassiveAbilities().forEach(a -> a.removeAbility(player));
        player.removeMetadata(CLASS_METADATA_KEY, ClassesPlugin.getPlugin());
    }

    public static boolean isClassEquipped(final Player player) {
        return !player.getMetadata(CLASS_METADATA_KEY).isEmpty()
            && Objects.nonNull(player.getMetadata(CLASS_METADATA_KEY).get(0));
    }

    public static boolean isClassEquipped(final Player player, final String className) {
        return isClassEquipped(player) && player.getMetadata(CLASS_METADATA_KEY).get(0).asString().equals(className);
    }

}
