package my.dw.classesplugin.listener;

import my.dw.classesplugin.ClassesPlugin;
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
            && ClassesPlugin.ARROW_TRIGGER_TO_ARROW_ABILITY.containsKey(ItemStackKey.from(event.getConsumable())));
    }

    private void handleArrowAbilityOnLaunchEvent(final EntityShootBowEvent event) {
        final Player player = (Player) event.getEntity();
        final ItemStack arrowTrigger = event.getConsumable();
        final ArrowAbility ability
            = ClassesPlugin.ARROW_TRIGGER_TO_ARROW_ABILITY.get(ItemStackKey.from(arrowTrigger));

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

            AbilityUtils.removeItemsFromPlayer(player.getInventory(), arrowTrigger);
            player.getInventory().addItem(arrowTrigger);
            event.setCancelled(true);
            return;
        }

        ability.onProjectileLaunch(event);
    }

}
