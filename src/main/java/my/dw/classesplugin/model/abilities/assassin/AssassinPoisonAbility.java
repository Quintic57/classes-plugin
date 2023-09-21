package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

public class AssassinPoisonAbility extends ArrowAbility {

    public AssassinPoisonAbility() {
        super(
            "Poison Dart",
            generatePotionMetaTrigger(
                Material.TIPPED_ARROW,
                "Poison Dart",
                List.of(new PotionEffect(PotionEffectType.WITHER, 10 * Constants.TICKS_PER_SECOND, 0)),
                Color.BLACK,
                List.of("Deals 5 wither damage to the target over 10s", "Number of Charges: 2", "Cooldown: 30s"),
                2
            ),
            30,
            2
        );
    }

}
