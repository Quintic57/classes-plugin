package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;

// TODO: Making active abilities cancellable? Maybe have this mapped to left-click
public abstract class ActiveAbility implements Ability {

    private final String name;
    private final ItemStack itemTrigger;
    private final int duration;
    private final int staticCooldown;
    private final Map<UUID, Instant> playerToLastAbilityInstant;

    public ActiveAbility(final String name,
                         final ItemStack itemTrigger,
                         final int duration,
                         final int staticCooldown) {
        this(name, itemTrigger, duration, staticCooldown, new HashMap<>());
    }

    public ActiveAbility(final String name,
                         final ItemStack itemTrigger,
                         final int duration,
                         final int staticCooldown,
                         final Map<UUID, Instant> playerToLastAbilityInstant) {
        this.name = name;
        this.itemTrigger = itemTrigger;
        this.duration = duration;
        this.staticCooldown = staticCooldown;
        this.playerToLastAbilityInstant = playerToLastAbilityInstant;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItemTrigger() {
        return itemTrigger;
    }

    protected int getDuration() {
        return duration;
    }

    protected boolean isAbilityDurationActive(final UUID playerUUID) {
        return durationElapsedSinceInstant(playerToLastAbilityInstant.get(playerUUID)).getSeconds() < this.duration;
    }

    protected int getStaticCooldown() {
        return staticCooldown;
    }

    public boolean isAbilityOnCooldown(final UUID playerUUID) {
        return durationElapsedSinceInstant(playerToLastAbilityInstant.get(playerUUID)).getSeconds() < staticCooldown;
    }

    public double getRemainingCooldown(final UUID playerUUID) {
        return staticCooldown
            - ((durationElapsedSinceInstant(playerToLastAbilityInstant.get(playerUUID))).toMillis() / 1000.0);
    }

    protected Instant getLastAbilityInstant(final UUID playerUUID) {
        return playerToLastAbilityInstant.get(playerUUID);
    }

    protected void setLastAbilityInstant(final UUID playerUUID, final Instant lastAbilityInstant) {
        playerToLastAbilityInstant.put(playerUUID, lastAbilityInstant);
    }

    protected boolean isItemTriggerOnPlayer(final PlayerInventory inventory) {
        return inventory.contains(this.itemTrigger) || inventory.getItemInOffHand().equals(this.itemTrigger);
    }

    @Override
    public void initialize(final Player player) {
        playerToLastAbilityInstant.put(player.getUniqueId(), Instant.now().minus(staticCooldown, ChronoUnit.SECONDS));
    }

    @Override
    public void terminate(final Player player) {
        playerToLastAbilityInstant.remove(player.getUniqueId());
    }

    public abstract void handleAbility(final Player player, final ItemStack itemTrigger);

}
