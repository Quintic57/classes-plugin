package my.dw.classesplugin.listener;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Objects;

public class ArrowAbilityOnHitListener implements Listener {

    @EventHandler
    public void onProjectileHitEvent(final ProjectileHitEvent event) {
        if (isArrowAbilityOnHitEvent(event.getEntity())) {
            handleArrowAbilityOnHitEvent(event);
        }
    }

    private boolean isArrowAbilityOnHitEvent(final Projectile projectile) {
        return projectile instanceof AbstractArrow
            && !projectile.getMetadata(ArrowAbility.ARROW_METADATA_KEY).isEmpty()
            && Objects.nonNull(projectile.getMetadata(ArrowAbility.ARROW_METADATA_KEY).get(0))
            && AbilityUtils.arrowAbilityNameToArrowAbilityMap.containsKey(
                projectile.getMetadata(ArrowAbility.ARROW_METADATA_KEY).get(0).asString())
            && projectile.getShooter() instanceof Player;
    }

    private void handleArrowAbilityOnHitEvent(final ProjectileHitEvent event) {
        final AbstractArrow arrow = (AbstractArrow) event.getEntity();
        final ArrowAbility ability = AbilityUtils.arrowAbilityNameToArrowAbilityMap.get(
            arrow.getMetadata(ArrowAbility.ARROW_METADATA_KEY).get(0).asString());

        if (!Class.isClassEquipped((Player) arrow.getShooter(), AbilityUtils.abilityToClassNameMap.get(ability))) {
            return;
        }

        ability.onProjectileHit(event);
    }

}
