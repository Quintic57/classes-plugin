package my.dw.classesplugin.model.abilities.archer;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

public class ArcherReconAbility extends ArrowAbility {

    private final PotionEffect effect;

    private static final double CUBE_LENGTH = 20;

    public ArcherReconAbility() {
        super(
            "Reconnaissance",
            generateItemMetaTrigger(
                Material.SPECTRAL_ARROW,
                "Recon Arrow",
                List.of("Highlights all entities in a 10-block radius of the arrow's terminus", "Cooldown: 30s")
            ),
            30
        );
        //TODO: Temp solution, in team games marked entities will be glowing to everyone, needs to only be glowing for
        // the archer
        this.effect = new PotionEffect(PotionEffectType.GLOWING, 6 * Constants.TICKS_PER_SECOND, 0);
    }

    @Override
    public void onProjectileHit(final ProjectileHitEvent event) {
        final AbstractArrow arrow = (AbstractArrow) event.getEntity();
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);

        final BukkitRunnable task = new BukkitRunnable() {
            int taskRunCount = 0;
            @Override
            public void run() {
                taskRunCount++;

                if (taskRunCount > 5) {
                    arrow.remove();
                    this.cancel();
                    return;
                }

                final Collection<Entity> entitiesInCube = arrow.getWorld().getNearbyEntities(arrow.getLocation(),
                    CUBE_LENGTH, CUBE_LENGTH, CUBE_LENGTH);
                entitiesInCube.stream()
                    .filter(e -> e instanceof LivingEntity)
                    .filter(e -> !e.getUniqueId().equals(((Player) arrow.getShooter()).getUniqueId()))
                    .forEach(e -> ((LivingEntity) e).addPotionEffect(effect));
                arrow.getWorld().spawnParticle(Particle.REDSTONE, arrow.getLocation().add(0, 0.5, 0), 20, 1.0, 0.25, 1.0, 0.25,
                    new Particle.DustOptions(Color.YELLOW, 2F));
                arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_SHEEP_SHEAR, (float) (CUBE_LENGTH / 10), 1F);
            }
        };
        task.runTaskTimer(ClassesPlugin.getPlugin(), 0, 5L * Constants.TICKS_PER_SECOND);
    }

}
