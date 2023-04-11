package my.dw.classesplugin.model.abilities.swordsman;

import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

//TODO: Change this to amplify dmg taken instead of wither?
public class SwordsmanBerserkAbility extends ActiveAbility {

    private final List<PotionEffect> effects;

    private static final int MIN_HEALTH_REQUIREMENT = 6;

    public SwordsmanBerserkAbility() {
        super(
            "Berserk",
            generateItemMetaTrigger(Material.BLAZE_POWDER, "Berserk"),
            20,
            30,
            new HashMap<>()
        );
        this.effects = List.of(
            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, this.duration * Constants.TICKS_PER_SECOND, 1),
            new PotionEffect(PotionEffectType.SPEED, this.duration * Constants.TICKS_PER_SECOND, 0),
            new PotionEffect(PotionEffectType.WITHER, (this.duration / 2) * Constants.TICKS_PER_SECOND, 0)
        );
    }

    @Override
    public boolean handleAbility(final Player player) {
        if (player.getHealth() < MIN_HEALTH_REQUIREMENT) {
            player.sendMessage("You need at least 3 Hearts to activate this ability");
            return false;
        }

        return player.addPotionEffects(this.effects);
    }

}
