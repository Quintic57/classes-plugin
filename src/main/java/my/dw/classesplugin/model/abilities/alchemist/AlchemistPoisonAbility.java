package my.dw.classesplugin.model.abilities.alchemist;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

public class AlchemistPoisonAbility extends ActiveThrowableAbility {

    public AlchemistPoisonAbility() {
        super(
            "Poison Cloud",
            generatePotionMetaTrigger(
                Material.LINGERING_POTION,
                "Poison Cloud",
                List.of(
                    new PotionEffect(PotionEffectType.POISON, 4 * Constants.TICKS_PER_SECOND, 1),
                    new PotionEffect(PotionEffectType.SLOW, 4 * Constants.TICKS_PER_SECOND, 0),
                    new PotionEffect(PotionEffectType.CONFUSION, 8 * Constants.TICKS_PER_SECOND, 0)
                ),
                Color.GREEN
            ),
            0,
            15,
            new HashMap<>()
        );
    }

}
