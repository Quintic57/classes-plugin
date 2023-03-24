package my.dw.classesplugin.model.abilities.juggernaut;

import my.dw.classesplugin.model.abilities.PassiveAbility;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JuggernautArmorAbility extends PassiveAbility {

    private final PotionEffect effect;

    public JuggernautArmorAbility() {
        super("Made Like a Tree", true);
        this.effect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false);
    }

    @Override
    public void handleAbility(Player player) {
        player.addPotionEffect(effect);
    }

    @Override
    public void removeAbility(Player player) {
        // unused for most conditional passive abilities. TODO: maybe split passives into conditional/unconditional?
    }
}
