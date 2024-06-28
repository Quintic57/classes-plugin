package my.dw.classesplugin.model.abilities.scout;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.metadata.FixedMetadataValue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

public class ScoutGrappleAbility extends ArrowAbility {

    private static final String INITIAL_SHOT_LOCATION_METADATA_KEY = "initial_shot_location";

    private static final double MAX_VERTICAL_DISTANCE = 40;

    private static final double MAX_HORIZONTAL_DISTANCE = 20;

    private static final double MAX_DISTANCE_FROM_AIR_BLOCK = 4;

    public ScoutGrappleAbility() {
        super(
            "Grapple Hook",
            generateItemMetaTrigger(
                Material.ARROW,
                "Grapple Hook",
                List.of("Can only be used to travel up.", "Teleports the user to the arrow's terminus", "Cooldown: 30s"),
                List.of(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
            ),
            15 // TODO: Change back
        );
    }

    @Override
    public void onProjectileLaunch(final EntityShootBowEvent event) {
        final AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        arrow.setMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY,
            new FixedMetadataValue(ClassesPlugin.getPlugin(), arrow.getLocation()));

        super.onProjectileLaunch(event);
    }

    @Override
    public void onProjectileHit(final ProjectileHitEvent event) {
        final AbstractArrow arrow = (AbstractArrow) event.getEntity();
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        final Player shooter = (Player) event.getEntity().getShooter();

        if (arrow.getMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY).isEmpty()
            || Objects.isNull(arrow.getMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY).get(0))
            || Objects.isNull(event.getHitBlock())) {
            return;
        }

        final Location initialShot = (Location) arrow.getMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY).get(0).value();
        final Location arrowTerminus = event.getHitBlock().getLocation();
        super.onProjectileHit(event);

        if (initialShot.getY() >= arrowTerminus.getY()) {
            resetShotCooldownAndNotify(shooter, "The grappling hook can only be used to travel up");
            return;
        } else if (Math.hypot(initialShot.getX() - arrowTerminus.getX(),
                initialShot.getZ() - arrowTerminus.getZ()) > MAX_HORIZONTAL_DISTANCE
            || Math.abs(initialShot.getY() - arrowTerminus.getY()) > MAX_VERTICAL_DISTANCE) {
            resetShotCooldownAndNotify(shooter, "The grappling hook terminus is too far away from your location");
            return;
        }

        int distanceFromAirBlock = 1;
        while (distanceFromAirBlock <= MAX_DISTANCE_FROM_AIR_BLOCK
            && !arrow.getWorld().getBlockAt(arrowTerminus.clone().add(0, distanceFromAirBlock, 0)).isEmpty()) {
            distanceFromAirBlock++;
        }
        if (distanceFromAirBlock > MAX_DISTANCE_FROM_AIR_BLOCK) {
            resetShotCooldownAndNotify(shooter, "The grappling hook terminus is too far away from a safe grapple point");
            return;
        } else if (!arrow.getWorld().getBlockAt(arrowTerminus.clone().add(0, distanceFromAirBlock + 1, 0)).isEmpty()) {
            resetShotCooldownAndNotify(shooter, "There is no free space to grapple up to");
            return;
        }

        shooter.teleport(
            new Location(
                arrowTerminus.getWorld(),
                arrowTerminus.getX(),
                arrowTerminus.getY() + distanceFromAirBlock,
                arrowTerminus.getZ(),
                shooter.getLocation().getYaw(),
                shooter.getLocation().getPitch()
            )
        );
    }

    private void resetShotCooldownAndNotify(final Player shooter, final String message) {
        setChargeCount(shooter.getUniqueId(), getMaxCharges());
        cancelAndRemoveIncrementChargeTask(shooter.getUniqueId());
        setLastAbilityRefresh(shooter.getUniqueId(), Instant.now().minus(getCooldown(), ChronoUnit.SECONDS));
        shooter.sendMessage(message);
    }

}
