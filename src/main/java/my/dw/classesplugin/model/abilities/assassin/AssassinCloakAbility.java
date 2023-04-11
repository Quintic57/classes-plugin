package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.Removable;
import my.dw.classesplugin.utils.AbilityUtils;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

public class AssassinCloakAbility extends ActiveAbility implements Removable {

    private final List<PotionEffect> effects;

    public AssassinCloakAbility() {
        super(
            "Cloak",
            generateItemMetaTrigger(Material.LEATHER, "Cloak of Invisibility"),
            15,
            25,
            new HashMap<>()
        );
        this.effects = List.of(
            new PotionEffect(PotionEffectType.INVISIBILITY, this.duration * Constants.TICKS_PER_SECOND, 0, false, false),
            new PotionEffect(PotionEffectType.SPEED, this.duration * Constants.TICKS_PER_SECOND, 1, false, false),
            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, this.duration * Constants.TICKS_PER_SECOND, 1, false, false)
        );
    }

    @Override
    public boolean handleAbility(final Player player) {
        final boolean abilityApplied = player.addPotionEffects(this.effects);

        if (abilityApplied) {
            final BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!isItemTriggerOnPlayer(player.getInventory())) {
                        player.getInventory().addItem(AssassinCloakAbility.this.itemTrigger);
                    }
                }
            };
            task.runTaskLater(ClassesPlugin.getPlugin(), (long) this.duration * Constants.TICKS_PER_SECOND);
            player.getActivePotionEffects().forEach(p -> player.addPotionEffect(
                new PotionEffect(p.getType(), p.getDuration() + 1, p.getAmplifier(), false, false)));
            AbilityUtils.removeItemFromPlayer(player.getInventory(), this.itemTrigger);
        }

        return abilityApplied;
    }

    @Override
    public void removeAbility(final Player player) {
        this.effects.stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
        player.getActivePotionEffects().stream()
                .filter(p -> !p.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) // ignore assassin armor ability
                .forEach(p -> player.addPotionEffect(
                    new PotionEffect(p.getType(), p.getDuration() + 1, p.getAmplifier(), false, true)));
        if (!isItemTriggerOnPlayer(player.getInventory())) {
            player.getInventory().addItem(AssassinCloakAbility.this.itemTrigger);
        }
    }

    public boolean isAbilityEffectActive(final Collection<PotionEffect> activeEffects) {
        final List<PotionEffectType> activeEffectTypes = activeEffects.stream()
                .map(PotionEffect::getType)
                .collect(Collectors.toList());

        return this.effects.stream().map(PotionEffect::getType).allMatch(activeEffectTypes::contains);
    }

}
