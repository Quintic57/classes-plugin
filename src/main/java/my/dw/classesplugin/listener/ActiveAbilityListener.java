package my.dw.classesplugin.listener;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.utils.ItemStackKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
            && ClassesPlugin.ITEM_TRIGGER_TO_ACTIVE_ABILITY.containsKey(ItemStackKey.from(item));
    }

    private void handleActiveAbilityEvent(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemTrigger = event.getItem();
        final ActiveAbility ability = ClassesPlugin.ITEM_TRIGGER_TO_ACTIVE_ABILITY.get(ItemStackKey.from(itemTrigger));

        // Check that the player has the correct character class metadata tag before attempting to resolve the ability
        if (!Class.isClassEquipped(player, ClassesPlugin.ABILITY_TO_CLASS_NAME.get(ability))) {
            return;
        }

        if (ability.isAbilityOnCooldown(player.getUniqueId())) {
            player.sendMessage(
                ability.getName()
                + " is on cooldown. Remaining CD: "
                + String.format("%.2f", ability.getRemainingCooldown(player.getUniqueId()))
                + " seconds"
            );
            event.setCancelled(true);
            return;
        }

        ability.handleAbility(player, itemTrigger);
    }

}
