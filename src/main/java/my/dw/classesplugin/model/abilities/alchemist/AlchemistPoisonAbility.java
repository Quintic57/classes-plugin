package my.dw.classesplugin.model.abilities.alchemist;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
                    new PotionEffect(PotionEffectType.POISON, 5 * Constants.TICKS_PER_SECOND, 1),
                    new PotionEffect(PotionEffectType.SLOW, 5 * Constants.TICKS_PER_SECOND, 0),
                    new PotionEffect(PotionEffectType.CONFUSION, 8 * Constants.TICKS_PER_SECOND, 0)
                ),
                Color.GREEN,
                List.of("Continuously disorients and poisons in a large AOE", "Number of Charges: 2", "Cooldown: 15s"),
                2
            ),
            0,
            15,
            2
        );
    }

}
