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
                Color.BLACK
            ),
            0,
            15,
            new HashMap<>()
        );
    }

}
