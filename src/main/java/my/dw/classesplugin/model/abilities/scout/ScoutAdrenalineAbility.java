package my.dw.classesplugin.model.abilities.scout;

import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Instant;
import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

public class ScoutAdrenalineAbility extends ActiveAbility {

    private final List<PotionEffect> effects;

    public ScoutAdrenalineAbility() {
        super(
            "Adrenaline Shot",
            generateItemMetaTrigger(
                Material.ECHO_SHARD,
                "Adrenaline Shot",
                List.of(
                    "Grants 20% increased movement speed,",
                    "2x jump height and 20% damage reduction for 30s",
                    "Cooldown: 45s"
                )
            ),
            30,
            45
        );
        this.effects = List.of(
            new PotionEffect(PotionEffectType.SPEED, getDuration() * Constants.TICKS_PER_SECOND, 1),
            new PotionEffect(PotionEffectType.JUMP_BOOST, getDuration() * Constants.TICKS_PER_SECOND, 1),
            new PotionEffect(PotionEffectType.RESISTANCE, getDuration() * Constants.TICKS_PER_SECOND, 0)
        );
    }

    @Override
    public void handleAbility(final Player player, ItemStack itemTrigger) {
        player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_HIT, 1F, 1F);
        player.addPotionEffects(effects);
        setLastAbilityInstant(player.getUniqueId(), Instant.now());
    }

}
