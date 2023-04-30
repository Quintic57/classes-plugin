package my.dw.classesplugin.model.abilities.scout;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.metadata.FixedMetadataValue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
                List.of("Teleports the user to the arrow's terminus. Can only be used to travel up", "Cooldown: 30s"),
                List.of(ItemFlag.HIDE_POTION_EFFECTS)
            ),
            1, // TODO: Change back
            new HashMap<>()
        );
    }

    @Override
    public void onProjectileLaunch(final AbstractArrow arrow) {
        arrow.setMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY,
            new FixedMetadataValue(ClassesPlugin.getPlugin(), arrow.getLocation()));

        super.onProjectileLaunch(arrow);
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

        final Location initialShotLocation = (Location) arrow.getMetadata(INITIAL_SHOT_LOCATION_METADATA_KEY).get(0).value();
        final Location arrowTerminus = event.getHitBlock().getLocation();
        super.onProjectileHit(event);

        if (initialShotLocation.getY() >= arrowTerminus.getY()) { //TODO: Is this a necessary restriction?
            shooter.sendMessage("The grappling hook can only be used to travel up");
            this.playerToLastAbilityInstant.put(shooter.getUniqueId(), Instant.now().minus(this.cooldown, ChronoUnit.SECONDS));
            return;
        } else if (Math.hypot(initialShotLocation.getX() - arrowTerminus.getX(),
                initialShotLocation.getZ() - arrowTerminus.getZ()) > MAX_HORIZONTAL_DISTANCE
            || Math.abs(initialShotLocation.getY() - arrowTerminus.getY()) > MAX_VERTICAL_DISTANCE) {
            shooter.sendMessage("The grappling hook terminus is too far away from your location");
            this.playerToLastAbilityInstant.put(shooter.getUniqueId(), Instant.now().minus(this.cooldown, ChronoUnit.SECONDS));
            return;
        }

        int distanceFromAirBlock = 1;
        while (distanceFromAirBlock <= MAX_DISTANCE_FROM_AIR_BLOCK
            && !arrow.getWorld().getBlockAt(arrowTerminus.clone().add(0, distanceFromAirBlock, 0)).isEmpty()) {
            distanceFromAirBlock++;
        }
        if (distanceFromAirBlock > MAX_DISTANCE_FROM_AIR_BLOCK) {
            shooter.sendMessage("The grappling hook terminus is too far away from a safe grapple point");
            this.playerToLastAbilityInstant.put(shooter.getUniqueId(), Instant.now().minus(this.cooldown, ChronoUnit.SECONDS));
            return;
        } else if (!arrow.getWorld().getBlockAt(arrowTerminus.clone().add(0, distanceFromAirBlock + 1, 0)).isEmpty()) {
            shooter.sendMessage("There is no free space to grapple up to");
            this.playerToLastAbilityInstant.put(shooter.getUniqueId(), Instant.now().minus(this.cooldown, ChronoUnit.SECONDS));
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

}
