package my.dw.classesplugin.model.abilities.scout;

import my.dw.classesplugin.model.abilities.PassiveAbility;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

import static org.bukkit.potion.PotionEffect.INFINITE_DURATION;

public class ScoutManeuverAbility extends PassiveAbility {

    private final PotionEffect effect;

    public ScoutManeuverAbility() {
        super("Travel Lightly");
        this.effect = new PotionEffect(PotionEffectType.SPEED, INFINITE_DURATION, 0, false, false);
    }

    @Override
    public void initialize(final Player player) {
        player.addPotionEffect(effect);
    }

    @Override
    public void terminate(final Player player) {
        final Collection<PotionEffect> currentEffects = player.getActivePotionEffects();
        currentEffects.remove(effect);
        player.getActivePotionEffects().forEach(p -> player.removePotionEffect(p.getType()));
        player.addPotionEffects(currentEffects);
    }

}
