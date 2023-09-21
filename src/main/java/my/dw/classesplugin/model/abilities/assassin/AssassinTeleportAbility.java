package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import org.bukkit.Material;

import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

public class AssassinTeleportAbility extends ActiveThrowableAbility {

    public AssassinTeleportAbility() {
        super(
            "Teleport",
            generateItemMetaTrigger(
                Material.ENDER_PEARL,
                "Teleport",
                List.of("Throws a projectile that teleports the user to its terminus", "Cooldown: 60s")
            ),
            0,
            60
        );
    }

}
