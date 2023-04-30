package my.dw.classesplugin.model.abilities.scout;

import static org.bukkit.potion.PotionEffect.INFINITE_DURATION;

import my.dw.classesplugin.model.abilities.PassiveAbility;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ScoutManeuverAbility extends PassiveAbility {

    private final PotionEffect effect;

    public ScoutManeuverAbility() {
        super("Travel Lightly");
        this.effect = new PotionEffect(PotionEffectType.SPEED, INFINITE_DURATION, 0);
    }

    @Override
    public boolean handleAbility(final Player player) {
        return player.addPotionEffect(effect);
    }

    @Override
    public void initialize(final Player player) {
        handleAbility(player);
    }

    @Override
    public void terminate(final Player player) {
        player.removePotionEffect(effect.getType());
    }
}
