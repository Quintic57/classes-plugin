package my.dw.classesplugin.model.abilities.alchemist;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

public class AlchemistDisAbility extends ActiveThrowableAbility {

    public AlchemistDisAbility() {
        super(
            "Nigredo",
            generatePotionMetaTrigger(
                Material.LINGERING_POTION,
                "Nigredo",
                List.of(
                    new PotionEffect(PotionEffectType.BLINDNESS, 5 * Constants.TICKS_PER_SECOND, 0),
                    new PotionEffect(PotionEffectType.SLOWNESS, 5 * Constants.TICKS_PER_SECOND, 2)
                ),
                Color.BLACK,
                List.of("Continuously blinds and slows in a large AOE", "Number of Charges: 2", "Cooldown: 15s"),
                2
            ),
            0,
            15,
            2
        );
    }

}
