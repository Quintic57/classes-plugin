package my.dw.classesplugin.model.abilities.alchemist;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class AlchemistDamageAbility extends ActiveThrowableAbility {

    public AlchemistDamageAbility() {
        super(
            "Expunge",
            generatePotionMetaTrigger(
                Material.SPLASH_POTION,
                "Expunge",
                List.of(new PotionEffect(PotionEffectType.HARM, 1, 1)),
                Color.MAROON,
                List.of("Deals 6 potion damage in a small AOE", "Cooldown: 10s")
            ),
            0,
            10
        );
    }

}
