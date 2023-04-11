package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

public class AssassinSmokeAbility extends ActiveThrowableAbility {

    public AssassinSmokeAbility() {
        super(
            "Smoke Bomb",
            generatePotionMetaTrigger(
                Material.LINGERING_POTION,
                "Smoke Bomb",
                List.of(new PotionEffect(PotionEffectType.BLINDNESS, 5 * Constants.TICKS_PER_SECOND, 0)),
                Color.BLACK
            ),
            0,
            30,
            new HashMap<>()
        );
    }

}
