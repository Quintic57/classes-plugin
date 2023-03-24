package my.dw.classesplugin.listener;

import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class ActiveAbilityListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent event) {
        if (isClassAbilityEvent(event)) {
            handleClassAbilityEvent(event.getPlayer(), event.getItem().getItemMeta());
        }
    }

    private boolean isClassAbilityEvent(final PlayerInteractEvent event) {
        return ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            && (!Objects.isNull(event.getItem()) && !Objects.isNull(event.getItem().getItemMeta()))
            && !Objects.isNull(AbilityUtils.itemTriggerToActiveAbilityMap.get(event.getItem().getItemMeta())));
    }

    private void handleClassAbilityEvent(final Player player, final ItemMeta abilityTrigger) {
        // Check that the player has the character class metadata tag before attempting to resolve the ability
        if (player.getMetadata("character_class").isEmpty()
            || Objects.isNull(player.getMetadata("character_class").get(0))) {
            return;
        }

        final ActiveAbility ability = AbilityUtils.itemTriggerToActiveAbilityMap.get(abilityTrigger);

        if (ability.isAbilityOnCooldown(player.getUniqueId())) {
            player.sendMessage(ability.getName() + " is on cooldown. Remaining CD: "
                + (ability.getCooldown() - Duration.between(ability.getPlayerCooldowns().get(player.getUniqueId()),
                Instant.now()).getSeconds()) + " seconds");
            return;
        }

        ability.handleAbility(player);
    }

}
