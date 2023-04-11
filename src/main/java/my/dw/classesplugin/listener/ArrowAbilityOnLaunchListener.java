package my.dw.classesplugin.listener;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class ArrowAbilityOnLaunchListener implements Listener {

    @EventHandler
    public void onEntityShootBowEvent(final EntityShootBowEvent event) {
        if (isActiveArrowAbilityEvent(event)) {
            handleActiveArrowAbilityEvent(event);
        }
    }

    private boolean isActiveArrowAbilityEvent(final EntityShootBowEvent event) {
        return event.getEntity() instanceof Player
            && event.getProjectile() instanceof AbstractArrow
            && (Objects.nonNull(event.getConsumable())
            && AbilityUtils.arrowTriggerToArrowAbilityMap.containsKey(event.getConsumable()));
    }

    private void handleActiveArrowAbilityEvent(final EntityShootBowEvent event) {
        final Player player = (Player) event.getEntity();
        final ArrowAbility ability = AbilityUtils.arrowTriggerToArrowAbilityMap.get(event.getConsumable());

        if (!Class.isClassEquipped(player, AbilityUtils.abilityToClassNameMap.get(ability))) {
            return;
        }

        if (ability.isAbilityOnCooldown(player.getUniqueId())) {
            player.sendMessage(ability.getName()
                + " is on cooldown. Remaining CD: "
                + String.format("%.2f", ability.getCooldown() - (Duration.between(ability.getPlayerCooldowns().get(
                player.getUniqueId()), Instant.now()).toMillis() / 1000.0)) + " seconds");

            /* There's currently a bug when cancelling arrow events that causes the arrow to become invisible in the
             item slot. updateInventory() fixes the issue*/
            player.updateInventory();
            event.setCancelled(true);
            return;
        }

        ability.onProjectileLaunch((AbstractArrow) event.getProjectile());
        ability.handleAbility(player);
        ability.getPlayerCooldowns().put(player.getUniqueId(), Instant.now());
    }

}
