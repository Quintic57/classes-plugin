package my.dw.classesplugin.model;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.exception.ClassAlreadyEquippedException;
import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.model.abilities.alchemist.AlchemistDamageAbility;
import my.dw.classesplugin.model.abilities.alchemist.AlchemistDisAbility;
import my.dw.classesplugin.model.abilities.alchemist.AlchemistPoisonAbility;
import my.dw.classesplugin.model.abilities.archer.ArcherExplosiveAbility;
import my.dw.classesplugin.model.abilities.archer.ArcherIceAbility;
import my.dw.classesplugin.model.abilities.archer.ArcherPoisonAbility;
import my.dw.classesplugin.model.abilities.archer.ArcherReconAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinArmorAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinCloakAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinPoisonAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinSmokeAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinSmokeArrowAbility;
import my.dw.classesplugin.model.abilities.assassin.AssassinTeleportAbility;
import my.dw.classesplugin.model.abilities.juggernaut.JuggernautArmorAbility;
import my.dw.classesplugin.model.abilities.juggernaut.JuggernautInvulnerAbility;
import my.dw.classesplugin.model.abilities.scout.ScoutAdrenalineAbility;
import my.dw.classesplugin.model.abilities.scout.ScoutGlideAbility;
import my.dw.classesplugin.model.abilities.scout.ScoutGrappleAbility;
import my.dw.classesplugin.model.abilities.scout.ScoutManeuverAbility;
import my.dw.classesplugin.model.abilities.summoner.SummonerLightningAbility;
import my.dw.classesplugin.model.abilities.swordsman.SwordsmanFrenzyAbility;
import my.dw.classesplugin.model.abilities.swordsman.SwordsmanShieldAbility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum Class {

    ALCHEMIST(
        Weapon.ALCHEMIST,
        Armor.ALCHEMIST,
        List.of(new AlchemistDamageAbility(), new AlchemistPoisonAbility(), new AlchemistDisAbility())
    ) {
        @Override
        public List<ItemStack> getInventory() {
            return defaultInventory();
        }
    },
    ARCHER(
        Weapon.ARCHER,
        Armor.ARCHER,
        List.of(
            new ArcherPoisonAbility(),
            new ArcherIceAbility(),
            new ArcherExplosiveAbility(),
            new ArcherReconAbility()
        )
    ) {
        @Override
        public List<ItemStack> getInventory() {
            final List<ItemStack> inventory = new ArrayList<>();

            // Weapon
            inventory.add(weapon.getWeaponItemStack());
            // Active Ability Triggers
            getActiveAbilities().forEach(a -> inventory.add(a.getItemTrigger()));
            // Arrow - By design, this makes it so the standard arrow is chosen by default when shooting
            inventory.add(new ItemStack(Material.ARROW));
            // Arrow Ability Triggers
            getArrowAbilities().forEach(a -> inventory.add(a.getArrowTrigger()));

            return inventory;
        }
    },
    ASSASSIN(
        Weapon.ASSASSIN,
        Armor.ASSASSIN,
        List.of(
            new AssassinCloakAbility(),
            new AssassinSmokeAbility(),
            new AssassinPoisonAbility(),
            new AssassinSmokeArrowAbility(),
            new AssassinTeleportAbility(),
            new AssassinArmorAbility()
        )
    ) {
        @Override
        public List<ItemStack> getInventory() {
            final List<ItemStack> inventory = new ArrayList<>();

            // Weapon
            inventory.add(weapon.getWeaponItemStack());
            // Active Ability Triggers
            getActiveAbilities().forEach(a -> inventory.add(a.getItemTrigger()));
            // Crossbow
            final ItemStack crossbow = new ItemStack(Material.CROSSBOW);
            final ItemMeta crossbowMeta = crossbow.getItemMeta();
            crossbowMeta.setUnbreakable(true);
            crossbow.setItemMeta(crossbowMeta);
            inventory.add(crossbow);
            // Arrow Ability Triggers
            getArrowAbilities().forEach(a -> inventory.add(a.getArrowTrigger()));

            return inventory;
        }
    },
    CAPTAIN(
        Weapon.CAPTAIN,
        Armor.CAPTAIN,
        List.of()
    ) {
        @Override
        public List<ItemStack> getInventory() {
            return defaultInventory();
        }
    },
    JUGGERNAUT(
        Weapon.JUGGERNAUT,
        Armor.JUGGERNAUT,
        List.of(new JuggernautArmorAbility(), new JuggernautInvulnerAbility())
    ) {
        @Override
        public List<ItemStack> getInventory() {
            return defaultInventory();
        }
    },
    KNIGHT(
        Weapon.KNIGHT,
        Armor.KNIGHT,
        List.of()
    ) {
        @Override
        public List<ItemStack> getInventory() {
            return defaultInventory();
        }
    },
    SCOUT(
        Weapon.SCOUT,
        Armor.SCOUT,
        List.of(
            new ScoutAdrenalineAbility(),
            new ScoutGlideAbility(),
            new ScoutGrappleAbility(),
            new ScoutManeuverAbility()
        )
    ) {
        @Override
        public List<ItemStack> getInventory() {
            final List<ItemStack> inventory = new ArrayList<>();

            // Weapon
            inventory.add(weapon.getWeaponItemStack());
            // Active Ability Triggers
            getActiveAbilities().forEach(a -> inventory.add(a.getItemTrigger()));
            // Arrow - By design, this makes it so the standard arrow is chosen by default when shooting
            inventory.add(new ItemStack(Material.ARROW));
            // Arrow Ability Triggers
            getArrowAbilities().forEach(a -> inventory.add(a.getArrowTrigger()));

            return inventory;
        }
    },
    SPECIALIST(
        Weapon.SPECIALIST,
        Armor.SPECIALIST,
        List.of(),
        false
    ) {
        @Override
        public List<ItemStack> getInventory() {
            return defaultInventory();
        }
    },
    SUMMONER(
        Weapon.SUMMONER,
        Armor.SUMMONER,
        List.of(new SummonerLightningAbility()),
        false
    ) {
        @Override
        public List<ItemStack> getInventory() {
            return defaultInventory();
        }
    },
    SWORDSMAN(
        Weapon.SWORDSMAN,
        Armor.SWORDSMAN,
        List.of(new SwordsmanFrenzyAbility(), new SwordsmanShieldAbility())
    ) {
        @Override
        public List<ItemStack> getInventory() {
            return defaultInventory();
        }
    };

    public static final String CLASS_METADATA_KEY = "character_class";

    final Weapon weapon;
    final Armor armor;
    private final List<Ability> abilities;
    private final boolean active;

    Class(final Weapon weapon,
          final Armor armor,
          final List<Ability> abilities) {
        this(weapon, armor, abilities, true);
    }

    Class(final Weapon weapon,
          final Armor armor,
          final List<Ability> abilities,
          final boolean active) {
        this.weapon = weapon;
        this.armor = armor;
        this.abilities = abilities;
        this.active = active;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getArmor() {
        return armor;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public boolean isActive() {
        return active;
    }

    public abstract List<ItemStack> getInventory();

    List<ItemStack> defaultInventory() {
        final List<ItemStack> inventory = new ArrayList<>();

        // Weapon
        inventory.add(weapon.getWeaponItemStack());
        // Active Ability Triggers
        getActiveAbilities().forEach(a -> inventory.add(a.getItemTrigger()));
        // Arrow Ability Triggers
        getArrowAbilities().forEach(a -> inventory.add(a.getArrowTrigger()));

        return inventory;
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

    public void equipClass(final Player player) throws ClassAlreadyEquippedException {
        if (isClassEquipped(player)) {
            final String className = player.getMetadata(CLASS_METADATA_KEY).get(0).asString();
            if (className.equals(name())) {
                throw new ClassAlreadyEquippedException();
            }
            Class.valueOf(className).unequipClass(player);
        } else {
            player.getInventory().clear();
        }

        player.setMetadata(CLASS_METADATA_KEY,
            new FixedMetadataValue(ClassesPlugin.getPlugin(), name()));

        final PlayerInventory playerInventory = player.getInventory();
        // Armor
        playerInventory.setItem(EquipmentSlot.HEAD, armor.getHelmet());
        playerInventory.setItem(EquipmentSlot.CHEST, armor.getChestplate());
        playerInventory.setItem(EquipmentSlot.LEGS, armor.getLeggings());
        playerInventory.setItem(EquipmentSlot.FEET, armor.getBoots());
        // Inventory
        getInventory().forEach(playerInventory::addItem);
        // Initialize Abilities
        for (final Ability a: abilities) {
            a.initialize(player);
        }
    }

    public void unequipClass(final Player player) {
        // TODO: If there's an expanded inventory you have to change this to remove only the items related to the class
        player.getInventory().clear();
        // Terminate Abilities
        for (final Ability a: abilities) {
            a.terminate(player);
        }
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
