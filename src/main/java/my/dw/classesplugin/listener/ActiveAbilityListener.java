package my.dw.classesplugin.listener;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import my.dw.classesplugin.utils.ItemStackKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.Objects;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;

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
            && AbilityUtils.ITEM_TRIGGER_TO_ACTIVE_ABILITY.containsKey(ItemStackKey.from(item));
    }

    private void handleActiveAbilityEvent(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemTrigger = event.getItem();
        final ActiveAbility ability = AbilityUtils.ITEM_TRIGGER_TO_ACTIVE_ABILITY.get(ItemStackKey.from(itemTrigger));

        // Check that the player has the correct character class metadata tag before attempting to resolve the ability
        if (!Class.isClassEquipped(player, AbilityUtils.ABILITY_TO_CLASS_NAME.get(ability))) {
            return;
        }

        if (ability.isAbilityOnCooldown(player.getUniqueId())) {
            final int cooldown = ability.getCooldown(player.getUniqueId());
            player.sendMessage(ability.getName()
                + " is on cooldown. Remaining CD: "
                + String.format("%.2f", (long) cooldown - (durationElapsedSinceInstant(
                    ability.getLastAbilityInstant(player.getUniqueId())).toMillis() / 1000.0))
                + " seconds");
            event.setCancelled(true);
            return;
        }

        final boolean activated = ability.handleAbility(player, itemTrigger);
        if (activated) {
            ability.setLastAbilityInstant(player.getUniqueId(), Instant.now());
        }
    }

}
