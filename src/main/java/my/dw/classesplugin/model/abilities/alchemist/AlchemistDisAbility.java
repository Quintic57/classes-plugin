package my.dw.classesplugin.model.abilities.alchemist;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class AlchemistDisAbility extends ActiveThrowableAbility {

    public AlchemistDisAbility() {
        super(
            "Incapacitate",
            generatePotionMetaTrigger(
                Material.LINGERING_POTION,
                "Incapacitate",
                List.of(
                    new PotionEffect(PotionEffectType.BLINDNESS, 5 * Constants.TICKS_PER_SECOND, 0),
                    new PotionEffect(PotionEffectType.SLOW, 5 * Constants.TICKS_PER_SECOND, 2)
                ),
                Color.BLACK,
                List.of("Continuously blinds and slows in a large AOE", "Cooldown: 15s")
            ),
            0,
            15
        );
    }

}
