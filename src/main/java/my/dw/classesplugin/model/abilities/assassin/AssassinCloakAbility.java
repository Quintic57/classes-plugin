package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AssassinCloakAbility extends ActiveAbility {

    private final List<PotionEffect> effects;

    public AssassinCloakAbility() {
        super("Cloak", generateItemTrigger(Material.LEATHER, "Cloak of Invisibility"), 15, 25, new HashMap<>());
        this.effects = List.of(
            new PotionEffect(PotionEffectType.INVISIBILITY, this.duration * Constants.TICKS_PER_SECOND, 0, false, false),
            new PotionEffect(PotionEffectType.SPEED, this.duration * Constants.TICKS_PER_SECOND, 1, false, false),
            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, this.duration * Constants.TICKS_PER_SECOND, 1, false, false)
        );
    }

    @Override
    public void handleAbility(final Player player) {
        if (!player.getMetadata("character_class").get(0).asString().equals(Class.ASSASSIN.name())) {
            return;
        }

        player.getActivePotionEffects()
                .forEach(p -> player.addPotionEffect(new PotionEffect(p.getType(), p.getDuration() + 1, p.getAmplifier(), false, false)));
        player.addPotionEffects(this.effects);
        this.playerCooldowns.put(player.getUniqueId(), Instant.now());
    }

    public void removeAbility(final Player player) {
        this.effects.stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
        player.getActivePotionEffects().stream()
                .filter(p -> !p.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) // ignore assassin armor ability
                .forEach(p -> player.addPotionEffect(new PotionEffect(p.getType(), p.getDuration() + 1, p.getAmplifier(), false, true)));
    }

    public boolean isAbilityEffectActive(final Collection<PotionEffect> activeEffects) {
        final List<PotionEffectType> activeEffectTypes = activeEffects.stream()
                .map(PotionEffect::getType)
                .collect(Collectors.toList());

        return this.effects.stream().map(PotionEffect::getType).allMatch(activeEffectTypes::contains);
    }

}
