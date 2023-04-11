package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.model.abilities.ActiveThrowableAbility;
import org.bukkit.Material;

import java.util.HashMap;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

public class AssassinTeleportAbility extends ActiveThrowableAbility {

    public AssassinTeleportAbility() {
        super(
            "Teleport",
            generateItemMetaTrigger(Material.ENDER_PEARL, "Teleport"),
            0,
            60,
            new HashMap<>()
        );
    }

}
