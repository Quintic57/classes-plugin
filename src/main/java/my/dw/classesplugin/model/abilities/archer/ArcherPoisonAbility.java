package my.dw.classesplugin.model.abilities.archer;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;

public class ArcherPoisonAbility extends ArrowAbility {

    public ArcherPoisonAbility() {
        super(
            "Poison Arrow",
            generatePotionMetaTrigger(
                Material.TIPPED_ARROW,
                "Poison Tipped Arrow",
                List.of(new PotionEffect(PotionEffectType.POISON, 10 * Constants.TICKS_PER_SECOND, 1)),
                Color.GREEN,
                List.of("Deals 8 potion damage to the target over 10s", "Cooldown: 10s")
            ),
            10,
            new HashMap<>()
        );
    }

}
