package my.dw.classesplugin.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ClassAbilityListener implements Listener {

    private final Map<UUID, Instant> abilityCooldowns;

    public ClassAbilityListener() {
        this.abilityCooldowns = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            handlePlayerAbilityEvent(event);
        }
    }

    // TODO: Move these to (java) classes
    private void handlePlayerAbilityEvent(PlayerInteractEvent event) {
        if (Objects.isNull(event.getItem())) {
            return;
        }
        // Assassin
        if (event.getItem().getType() == Material.LEATHER) {
            handleAssassinEvent(event.getPlayer());
        }
    }

    // TODO: each class should have abilities, abilities each have implementations of handleAbility() method. CDs for said events are specified within ability classes
    public void handleAssassinEvent(Player player) {
        if (abilityCooldowns.containsKey(player.getUniqueId())
                && isAbilityOnCooldown(abilityCooldowns.get(player.getUniqueId()), 30L)) {
            player.sendMessage("Ability is on cooldown. Remaining CD: "
                    + (30L - Duration.between(abilityCooldowns.get(player.getUniqueId()), Instant.now()).getSeconds())
                    + " seconds");
            return;
        }

        List<PotionEffect> effects = List.of(
                new PotionEffect(PotionEffectType.INVISIBILITY, 300, 0, false, false),
                new PotionEffect(PotionEffectType.SPEED, 300, 1, false, false));
        player.addPotionEffects(effects);

        abilityCooldowns.put(player.getUniqueId(), Instant.now());
    }

    private boolean isAbilityOnCooldown(final Instant lastUsedTime, final Long cooldown) {
        return Duration.between(lastUsedTime, Instant.now()).getSeconds() < cooldown;
    }
}
