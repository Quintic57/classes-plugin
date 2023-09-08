package my.dw.classesplugin.model;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.exception.ClassAlreadyEquippedException;
import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.model.abilities.InitializedAbility;
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
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
            inventory.add(this.weapon.getWeaponItemStack());
            // Active Ability Triggers
            this.getActiveAbilities().forEach(a -> inventory.add(a.getItemTrigger()));
            // Arrow - By design, this makes it so the standard arrow is chosen by default when shooting
            inventory.add(new ItemStack(Material.ARROW));
            // Arrow Ability Triggers
            this.getArrowAbilities().forEach(a -> inventory.add(a.getArrowTrigger()));

            return inventory;
        }
    },
    ASSASSIN(
        Weapon.ASSASSIN,
        Armor.ASSASSIN,
        List.of(
            new AssassinCloakAbility(),
            new AssassinSmokeAbility(),
            new AssassinSmokeArrowAbility(),
            new AssassinTeleportAbility(),
            new AssassinArmorAbility()
        )
    ) {
        @Override
        public List<ItemStack> getInventory() {
            final List<ItemStack> inventory = defaultInventory();

            // Crossbow
            final ItemStack crossbow = new ItemStack(Material.CROSSBOW);
            final ItemMeta crossbowMeta = crossbow.getItemMeta();
            crossbowMeta.setUnbreakable(true);
            crossbow.setItemMeta(crossbowMeta);
            inventory.add(crossbow);
            // Crossbow Bolts
            final ItemStack crossbowBolts = new ItemStack(Material.TIPPED_ARROW, 3);
            final PotionMeta crossbowBoltsMeta = (PotionMeta) crossbowBolts.getItemMeta();
            crossbowBoltsMeta.addCustomEffect(
                new PotionEffect(PotionEffectType.POISON, 10 * Constants.TICKS_PER_SECOND, 0), false);
            crossbowBoltsMeta.setColor(Color.GREEN);
            crossbowBoltsMeta.setDisplayName("Arrow of Poison");
            crossbowBolts.setItemMeta(crossbowBoltsMeta);
            inventory.add(crossbowBolts);

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
            inventory.add(this.weapon.getWeaponItemStack());
            // Active Ability Triggers
            this.getActiveAbilities().forEach(a -> inventory.add(a.getItemTrigger()));
            // Arrow - By design, this makes it so the standard arrow is chosen by default when shooting
            inventory.add(new ItemStack(Material.ARROW));
            // Arrow Ability Triggers
            this.getArrowAbilities().forEach(a -> inventory.add(a.getArrowTrigger()));

            return inventory;
        }
    },
    SPECIALIST(
        Weapon.SPECIALIST,
        Armor.SPECIALIST,
        List.of()
    ) {
        @Override
        public List<ItemStack> getInventory() {
            return defaultInventory();
        }
    },
    SUMMONER(
        Weapon.SUMMONER,
        Armor.SUMMONER,
        List.of(new SummonerLightningAbility())
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
            final List<ItemStack> inventory = defaultInventory();

            final ItemStack shield = new ItemStack(Material.SHIELD);
            final ItemMeta shieldMeta = shield.getItemMeta();
            shieldMeta.setUnbreakable(true);
            shield.setItemMeta(shieldMeta);
            inventory.add(shield);

            return inventory;
        }
    };

    final Weapon weapon;
    final Armor armor;
    private final List<Ability> abilities;

    public static final String CLASS_METADATA_KEY = "character_class";

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

    public Armor getArmor() {
        return armor;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public abstract List<ItemStack> getInventory();

    List<ItemStack> defaultInventory() {
        final List<ItemStack> inventory = new ArrayList<>();

        // Weapon
        inventory.add(this.weapon.getWeaponItemStack());
        // Active Ability Triggers
        this.getActiveAbilities().forEach(a -> inventory.add(a.getItemTrigger()));
        // Arrow Ability Triggers
        this.getArrowAbilities().forEach(a -> inventory.add(a.getArrowTrigger()));

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

    public List<PassiveAbility> getPassiveAbilities() {
        return abilities.stream()
            .filter(a -> a instanceof PassiveAbility)
            .map(a -> (PassiveAbility) a)
            .collect(Collectors.toList());
    }

    private List<InitializedAbility> getInitializedAbilities() {
        return abilities.stream()
            .filter(a -> a instanceof InitializedAbility)
            .map(a -> (InitializedAbility) a)
            .collect(Collectors.toList());
    }

    public void equipClass(final Player player) throws ClassAlreadyEquippedException {
        if (isClassEquipped(player)) {
            final String className = player.getMetadata(CLASS_METADATA_KEY).get(0).asString();
            if (className.equals(this.name())) {
                throw new ClassAlreadyEquippedException();
            }

            Class.valueOf(className).unequipClass(player);
        } else {
            player.getInventory().clear();
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        }

        player.setMetadata(CLASS_METADATA_KEY,
            new FixedMetadataValue(ClassesPlugin.getPlugin(), this.name()));

        final PlayerInventory playerInventory = player.getInventory();
        // Armor
        playerInventory.setItem(EquipmentSlot.HEAD, this.armor.getHelmet());
        playerInventory.setItem(EquipmentSlot.CHEST, this.armor.getChestplate());
        playerInventory.setItem(EquipmentSlot.LEGS, this.armor.getLeggings());
        playerInventory.setItem(EquipmentSlot.FEET, this.armor.getBoots());
        // Inventory
        this.getInventory().forEach(playerInventory::addItem);
        // Initialized Abilities
        for (InitializedAbility a: this.getInitializedAbilities()) {
            a.initialize(player);
        }
    }

    public void unequipClass(final Player player) {
        player.getInventory().clear();
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

        // Initialized Abilities
        for (InitializedAbility a: this.getInitializedAbilities()) {
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
