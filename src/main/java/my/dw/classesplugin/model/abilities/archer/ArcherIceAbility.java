package my.dw.classesplugin.model.abilities.archer;

import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

public class ArcherIceAbility extends ArrowAbility {

    public ArcherIceAbility() {
        super(
            "Ice Arrow",
            generatePotionMetaTrigger(
                Material.TIPPED_ARROW,
                "Ice Tipped Arrow",
                List.of(new PotionEffect(PotionEffectType.SLOWNESS, 8 * Constants.TICKS_PER_SECOND, 3)),
                Color.GRAY,
                List.of("Slows the target by 60% for 8s", "Number of Charges: 2", "Cooldown: 15s"),
                2
            ),
            15,
            2
        );
    }

}
