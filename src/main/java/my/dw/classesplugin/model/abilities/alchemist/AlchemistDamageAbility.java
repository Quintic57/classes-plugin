package my.dw.classesplugin.model.abilities.alchemist;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

public class AlchemistDamageAbility extends ActiveThrowableAbility {

    public AlchemistDamageAbility() {
        super(
            "Expunge",
            generatePotionMetaTrigger(
                Material.SPLASH_POTION,
                "Expunge",
                List.of(new PotionEffect(PotionEffectType.HARM, 1, 1)),
                Color.MAROON,
                List.of("Deals 6 potion damage in a small AOE", "Number of Charges: 3", "Cooldown: 10s"),
                3
            ),
            0,
            10,
            3
        );
    }

}
