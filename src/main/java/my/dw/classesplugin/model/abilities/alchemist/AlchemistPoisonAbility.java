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

public class AlchemistPoisonAbility extends ActiveThrowableAbility {

    public AlchemistPoisonAbility() {
        super(
            "Albedo",
            generatePotionMetaTrigger(
                Material.LINGERING_POTION,
                "Albedo",
                List.of(
                    new PotionEffect(PotionEffectType.POISON, 5 * Constants.TICKS_PER_SECOND, 1),
                    new PotionEffect(PotionEffectType.SLOWNESS, 5 * Constants.TICKS_PER_SECOND, 0),
                    new PotionEffect(PotionEffectType.NAUSEA, 8 * Constants.TICKS_PER_SECOND, 0)
                ),
                Color.WHITE,
                List.of("Continuously disorients and poisons in a large AOE", "Number of Charges: 1", "Cooldown: 15s")
            ),
            0,
            15
        );
    }

}
