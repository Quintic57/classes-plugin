package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.model.abilities.PassiveAbility;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AssassinArmorAbility extends PassiveAbility {

    private final PotionEffect effect;

    public AssassinArmorAbility() {
        super("Nerves of Steal", false);
        this.effect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false);
    }

    @Override
    public void handleAbility(Player player) {
        player.addPotionEffect(effect);
    }

    @Override
    public void removeAbility(Player player) {
        player.removePotionEffect(effect.getType());
    }

}
