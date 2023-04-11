package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.model.abilities.PassiveAbility;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.potion.PotionEffect.INFINITE_DURATION;

public class AssassinArmorAbility extends PassiveAbility {

    private final PotionEffect effect;

    public AssassinArmorAbility() {
        super("Nerves of Steal");
        this.effect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, INFINITE_DURATION, 0, false, false);
    }

    @Override
    public boolean handleAbility(final Player player) {
        return player.addPotionEffect(this.effect);
    }

    @Override
    public void removeAbility(final Player player) {
        player.removePotionEffect(this.effect.getType());
    }

}
