package my.dw.classesplugin.model.abilities.assassin;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import org.bukkit.Material;

import java.util.List;

public class AssassinTeleportAbility extends ActiveThrowableAbility {

    public AssassinTeleportAbility() {
        super(
            "Teleport",
            generateItemMetaTrigger(
                Material.ENDER_PEARL,
                "Teleport",
                List.of("Throws a projectile that teleports the user to the projectile's terminus", "Cooldown: 60s")
            ),
            0,
            60
        );
    }

}