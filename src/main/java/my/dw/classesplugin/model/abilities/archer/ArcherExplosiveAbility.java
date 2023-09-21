package my.dw.classesplugin.model.abilities.archer;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

// TODO: Investigate why this doesn't do as much dmg as intended
public class ArcherExplosiveAbility extends ArrowAbility {

    private static final String INITIAL_SHOT_LOCATION_METADATA_KEY = "initial_shot_location";

    private static final String TRAIL_TASK_ID_METADATA_KEY = "trail_task_id";

    private static final float TNT_EXPLOSION_POWER = 4F;

    private static final double LEVEL_1_PRIMING_DISTANCE = 10;

    private static final double LEVEL_2_PRIMING_DISTANCE = 20;

    private static final double MIN_IGNITION_SPEED = 0.15;

    public ArcherExplosiveAbility() {
        super(
            "Explosive Arrow",
            generateItemMetaTrigger(
                Material.ARROW,
                "Explosive Tipped Arrow",
                List.of("Deals AOE explosive damage based on distance traveled", "Number of Charges: 1", "Cooldown: 30s"),
                List.of(ItemFlag.HIDE_POTION_EFFECTS)
            ),
            1 // TODO: Change this back once you've had your fun
        );
    }

    @Override
    public void onProjectileLaunch(final EntityShootBowEvent event) {
        final AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        final BukkitRunnable trailTask = new BukkitRunnable() {
            @Override
            public void run() {
                /* if this condition is true, either the arrow was shot straight up and is about to reach its apex or
                it's travelling down water, thus initial shot location should be reset to enable/disable explosion */
                if ((arrow.getVelocity().getX() < MIN_IGNITION_SPEED && arrow.getVelocity().getX() > -MIN_IGNITION_SPEED)
                    && (arrow.getVelocity().getY() < MIN_IGNITION_SPEED && arrow.getVelocity().getY() > -MIN_IGNITION_SPEED)
                    && (arrow.getVelocity().getZ() < MIN_IGNITION_SPEED && arrow.getVelocity().getZ() > -MIN_IGNITION_SPEED)) {
                    arrow.setMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY,
                        new FixedMetadataValue(ClassesPlugin.getPlugin(), arrow.getLocation()));
                }

                final Location initialShotLocation = (Location) arrow.getMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY)
                    .get(0).value();
                final Color color;
                if (initialShotLocation.distance(arrow.getLocation()) < LEVEL_1_PRIMING_DISTANCE) {
                    color = Color.YELLOW;
                } else if (initialShotLocation.distance(arrow.getLocation()) < LEVEL_2_PRIMING_DISTANCE) {
                    color = Color.ORANGE;
                } else {
                    color = Color.RED;
                }
                arrow.getWorld().spawnParticle(Particle.REDSTONE, arrow.getLocation(), 2, 0.2, 0.2, 0.2, 0,
                    new Particle.DustOptions(color, 2F));

            }
        };
        trailTask.runTaskTimer(ClassesPlugin.getPlugin(), 0, 0).getTaskId();

        arrow.setMetadata(TRAIL_TASK_ID_METADATA_KEY,
            new FixedMetadataValue(ClassesPlugin.getPlugin(), trailTask));
        arrow.setMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY,
            new FixedMetadataValue(ClassesPlugin.getPlugin(), arrow.getLocation()));
        arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1F, 1F);

        super.onProjectileLaunch(event);
    }

    @Override
    public void onProjectileHit(final ProjectileHitEvent event) {
        final AbstractArrow arrow = (AbstractArrow) event.getEntity();

        if (arrow.getMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY).isEmpty()
            || Objects.isNull(arrow.getMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY).get(0))
            || arrow.getMetadata(TRAIL_TASK_ID_METADATA_KEY).isEmpty()
            || Objects.isNull(arrow.getMetadata(TRAIL_TASK_ID_METADATA_KEY).get(0))) {
            return;
        }

        final BukkitRunnable trailTask = (BukkitRunnable) arrow.getMetadata(TRAIL_TASK_ID_METADATA_KEY).get(0).value();
        trailTask.cancel();

        final Location initialShotLocation = (Location) arrow.getMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY).get(0).value();
        if (initialShotLocation.distance(arrow.getLocation()) < LEVEL_1_PRIMING_DISTANCE) {
            super.onProjectileHit(event);
            return;
        }
        final float explosionPower = initialShotLocation.distance(arrow.getLocation()) < LEVEL_2_PRIMING_DISTANCE
            ? TNT_EXPLOSION_POWER - 1F
            : TNT_EXPLOSION_POWER;
        final Location explosionLocation = Objects.isNull(event.getHitBlock())
            ? arrow.getLocation()
            : event.getHitBlock().getLocation().add(0, 1, 0);

        arrow.getWorld().createExplosion(explosionLocation, explosionPower, false, false, (Entity) arrow.getShooter());
        super.onProjectileHit(event);
    }

}
