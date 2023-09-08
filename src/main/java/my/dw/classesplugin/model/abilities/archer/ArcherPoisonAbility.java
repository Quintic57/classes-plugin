package my.dw.classesplugin.model.abilities.archer;

import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

public class ArcherPoisonAbility extends ArrowAbility {

    public ArcherPoisonAbility() {
        super(
            "Poison Arrow",
            generatePotionMetaTrigger(
                Material.TIPPED_ARROW,
                "Poison Tipped Arrow",
                List.of(new PotionEffect(PotionEffectType.POISON, 10 * Constants.TICKS_PER_SECOND, 0)),
                Color.GREEN,
                List.of("Deals 8 potion damage to the target over 10s", "Cooldown: 10s"),
                3
            ),
            10,
            3
        );
    }

}
