package my.dw.classesplugin.listener;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import my.dw.classesplugin.utils.ItemStackKey;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;

public class ArrowAbilityOnLaunchListener implements Listener {

    @EventHandler
    public void onEntityShootBowEvent(final EntityShootBowEvent event) {
        if (isArrowAbilityOnLaunchEvent(event)) {
            handleArrowAbilityOnLaunchEvent(event);
        }
    }

    private boolean isArrowAbilityOnLaunchEvent(final EntityShootBowEvent event) {
        return event.getEntity() instanceof Player
            && event.getProjectile() instanceof AbstractArrow
            && (Objects.nonNull(event.getConsumable())
            && AbilityUtils.ARROW_TRIGGER_TO_ARROW_ABILITY.containsKey(ItemStackKey.from(event.getConsumable())));
    }

    private void handleArrowAbilityOnLaunchEvent(final EntityShootBowEvent event) {
        final Player player = (Player) event.getEntity();
        final ItemStack arrowTrigger = event.getConsumable();
        final ArrowAbility ability
            = AbilityUtils.ARROW_TRIGGER_TO_ARROW_ABILITY.get(ItemStackKey.from(arrowTrigger));

        if (!Class.isClassEquipped(player, AbilityUtils.ABILITY_TO_CLASS_NAME.get(ability))) {
            return;
        }

        if (ability.isAbilityOnCooldown(player.getUniqueId())) {
            player.sendMessage(ability.getName()
                + " is on cooldown. Remaining CD: "
                + String.format("%.2f", ability.getCooldown() - (durationElapsedSinceInstant(
                    ability.getLastAbilityInstant(player.getUniqueId())).toMillis() / 1000.0))
                + " seconds");

            AbilityUtils.removeItemsFromPlayer(player.getInventory(), arrowTrigger);
            player.getInventory().addItem(arrowTrigger);
            event.setCancelled(true);
            return;
        }

        ability.onProjectileLaunch(event);
    }

}
