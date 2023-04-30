package my.dw.classesplugin.model.abilities.scout;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class ScoutAdrenalineAbility extends ActiveAbility {

    private final List<PotionEffect> effects;

    public ScoutAdrenalineAbility() {
        super(
            "Adrenaline Shot",
            generateItemMetaTrigger(
                Material.FEATHER,
                "Adrenaline Shot",
                List.of("Grants 20% increased movement speed, 2x jump height and 20% damage reduction for 30s",
                    "Cooldown: 45s")
            ),
            30,
            45
        );
        this.effects = List.of(
            new PotionEffect(PotionEffectType.SPEED, this.duration * Constants.TICKS_PER_SECOND, 1),
            new PotionEffect(PotionEffectType.JUMP, this.duration * Constants.TICKS_PER_SECOND, 1),
            new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, this.duration * Constants.TICKS_PER_SECOND, 0));
    }

    @Override
    public boolean handleAbility(final Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_BOTTLE_FILL, 1F, 1F);
        return player.addPotionEffects(effects);
    }

}
