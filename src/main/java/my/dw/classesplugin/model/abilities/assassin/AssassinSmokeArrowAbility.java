package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generatePotionMetaTrigger;

public class AssassinSmokeArrowAbility extends ArrowAbility {

    public AssassinSmokeArrowAbility() {
        super(
            "Phosphorus Arrow",
            generatePotionMetaTrigger(
                Material.TIPPED_ARROW,
                "Phosphorus Arrow",
                List.of(new PotionEffect(PotionEffectType.UNLUCK, 5 * Constants.TICKS_PER_SECOND, 0)),
                Color.YELLOW,
                List.of("Creates a large smoke screen at the arrow's terminus")
            ),
            1 // TODO: Change this back
        );
    }

    @Override
    public void onProjectileHit(final ProjectileHitEvent event) {
        final AbstractArrow arrow = (AbstractArrow) event.getEntity();

        // TODO: Change this to use sunflower seed distribution
        for (int i = 1; i <= 4; i++) {
            arrow.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, arrow.getLocation().clone().add(i, 0, 0), 4, 0.25, 0.25, 0.25, 0);
            arrow.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, arrow.getLocation().clone().add(0, 0, i), 4, 0.25, 0.25, 0.25, 0);
            arrow.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, arrow.getLocation().clone().add(i, 0, i), 4, 0.25, 0.25, 0.25, 0);
            arrow.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, arrow.getLocation().clone().add(-i, 0, i), 4, 0.25, 0.25, 0.25, 0);
            arrow.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, arrow.getLocation().clone().add(i, 0, -i), 4, 0.25, 0.25, 0.25, 0);
            arrow.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, arrow.getLocation().clone().subtract(0, 0, i), 4, 0.25, 0.25, 0.25, 0);
            arrow.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, arrow.getLocation().clone().subtract(i, 0, i), 4, 0.25, 0.25, 0.25, 0);
            arrow.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, arrow.getLocation().clone().subtract(i, 0, i), 4, 0.25, 0.25, 0.25, 0);
        }

        super.onProjectileHit(event);
    }

}
