package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;

// TODO: Is it possible to add multiple charges for abilities w/ the current framework? This would only apply to
//  ArrowAbility and ActiveThrowableAbility. Maybe schedule a repeating task with a period of the ability CD to add charges
//  back to the player, and cancel once max charges are reached.
// TODO: Making active abilities cancellable? Maybe have this mapped to left-click
public abstract class ActiveAbility implements Ability {

    protected final String name;
    // TODO: Encapsulate this to a new object called ItemStackKey, which overrides equals method and uses isSimilar() method, and also overrides hashcode() by hashing itemMeta
    protected final ItemStack itemTrigger;
    protected final int duration;
    final int staticCooldown;
    protected final Map<UUID, Instant> playerToLastAbilityInstant;

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

    public int getCooldown(final UUID playerUUID) {
        return staticCooldown;
    }

    public Instant getLastAbilityInstant(final UUID playerUUID) {
        return playerToLastAbilityInstant.get(playerUUID);
    }

    public void setLastAbilityInstant(final UUID playerUUID, final Instant abilityInstant) {
        playerToLastAbilityInstant.put(playerUUID, abilityInstant);
    }

    public boolean isAbilityDurationActive(final UUID playerUUID) {
        return playerToLastAbilityInstant.containsKey(playerUUID)
            && durationElapsedSinceInstant(playerToLastAbilityInstant.get(playerUUID)).getSeconds() < this.duration;
    }

    public boolean isAbilityOnCooldown(final UUID playerUUID) {
        return playerToLastAbilityInstant.containsKey(playerUUID)
            && durationElapsedSinceInstant(playerToLastAbilityInstant.get(playerUUID)).getSeconds() < this.staticCooldown;
    }

    protected boolean isItemTriggerOnPlayer(final PlayerInventory inventory) {
        return inventory.contains(this.itemTrigger) || inventory.getItemInOffHand().equals(this.itemTrigger);
    }

    public abstract boolean handleAbility(final Player player, final ItemStack itemTrigger);

}
