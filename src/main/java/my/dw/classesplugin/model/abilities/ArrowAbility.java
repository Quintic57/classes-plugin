package my.dw.classesplugin.model.abilities;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.utils.AbilityUtils;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public abstract class ArrowAbility implements Ability {

    protected final String name;
    protected final ItemStack arrowTrigger;
    protected final int cooldown;
    protected final Map<UUID, Instant> playerToLastAbilityInstant;

    public static final String ARROW_METADATA_KEY = "arrow_ability_name";

    public ArrowAbility(final String name,
                        final ItemStack arrowTrigger,
                        final int cooldown,
                        final Map<UUID, Instant> playerToLastAbilityInstant) {
        this.name = name;
        this.arrowTrigger = arrowTrigger;
        this.cooldown = cooldown;
        this.playerToLastAbilityInstant = playerToLastAbilityInstant;
    }

    public String getName() {
        return name;
    }

    public ItemStack getArrowTrigger() {
        return arrowTrigger;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Map<UUID, Instant> getPlayerToLastAbilityInstant() {
        return playerToLastAbilityInstant;
    }

    public Instant getLastAbilityInstant(final UUID playerUUID) {
        return playerToLastAbilityInstant.get(playerUUID);
    }

    public boolean isAbilityOnCooldown(final UUID playerUUID) {
        return this.playerToLastAbilityInstant.containsKey(playerUUID)
                && durationElapsedSinceInstant(playerToLastAbilityInstant.get(playerUUID)).getSeconds() < cooldown;
    }

    @Override
    public boolean handleAbility(final Player player) {
        AbilityUtils.removeItemsFromPlayer(player.getInventory(), arrowTrigger);
        player.getInventory().addItem(arrowTrigger);

        return true;
    }

    public void onProjectileLaunch(final AbstractArrow arrow) {
        arrow.setMetadata(ArrowAbility.ARROW_METADATA_KEY,
            new FixedMetadataValue(ClassesPlugin.getPlugin(), name));
    }

    public void onProjectileHit(final ProjectileHitEvent event) {
        event.getEntity().remove();
    }

}

