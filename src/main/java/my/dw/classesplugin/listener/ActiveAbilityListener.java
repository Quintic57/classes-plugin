package my.dw.classesplugin.listener;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class ActiveAbilityListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent event) {
        if (isActiveAbilityEvent(event.getAction(), event.getItem())) {
            handleActiveAbilityEvent(event);
        }
    }

    private boolean isActiveAbilityEvent(final Action action, final ItemStack item) {
        return ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
            && Objects.nonNull(item))
            && AbilityUtils.itemTriggerToActiveAbilityMap.containsKey(item);
    }

    private void handleActiveAbilityEvent(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ActiveAbility ability = AbilityUtils.itemTriggerToActiveAbilityMap.get(event.getItem());

        // Check that the player has the correct character class metadata tag before attempting to resolve the ability
        if (!Class.isClassEquipped(player, AbilityUtils.abilityToClassNameMap.get(ability))) {
            return;
        }

        if (ability.isAbilityOnCooldown(player.getUniqueId())) {
            player.sendMessage(ability.getName()
                + " is on cooldown. Remaining CD: "
                + String.format("%.2f", ability.getCooldown() - (Duration.between(ability.getPlayerCooldowns().get(
                    player.getUniqueId()), Instant.now()).toMillis() / 1000.0)) + " seconds");
            event.setCancelled(true);
            return;
        }

        final boolean activated = ability.handleAbility(player);
        if (activated) {
            ability.getPlayerCooldowns().put(player.getUniqueId(), Instant.now());
        }
    }

}
