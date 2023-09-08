package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

// TODO: Change this to an actual smoke bomb w/ particle effects. Possible to create custom throwables or not?
//  https://www.spigotmc.org/resources/realisticgrenades.94240/.
public class AssassinSmokeAbility extends ActiveThrowableAbility {

    public AssassinSmokeAbility() {
        super(
            "Smoke Bomb",
            generatePotionMetaTrigger(
                Material.LINGERING_POTION,
                "Smoke Bomb",
                List.of(new PotionEffect(PotionEffectType.BLINDNESS, 5 * Constants.TICKS_PER_SECOND, 0)),
                Color.BLACK,
                List.of("Creates a large smoke screen for 15s that continuously blinds entities in its AOE",
                    "Charge Refresh: 30s")
            ),
            0,
            30
        );
    }

}
