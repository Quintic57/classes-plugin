package my.dw.classesplugin.model.abilities.juggernaut;

import my.dw.classesplugin.model.abilities.PassiveAbility;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JuggernautDisAbility extends PassiveAbility {

    public JuggernautDisAbility() {
        super("Made Like a Tree", false);
    }

    @Override
    public void handleAbility(final Player player) {
        player.setSprinting(false);
    }

    @Override
    public void removeAbility(final Player player) {
        player.setSprinting(true);
    }
}
