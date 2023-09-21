package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;

public abstract class ActiveDynamicAbility extends ActiveAbility {

    private final Map<UUID, Integer> playerToDynamicCooldown;

    public ActiveDynamicAbility(final String name,
                                final ItemStack itemTrigger,
                                final int duration,
                                final Map<UUID, Integer> playerToDynamicCooldown) {
        super(name, itemTrigger, duration, -1);
        this.playerToDynamicCooldown = playerToDynamicCooldown;
    }

    protected void setDynamicCooldown(final UUID playerUUID, final Integer cooldown) {
        playerToDynamicCooldown.put(playerUUID, cooldown);
    }

    @Override
    public boolean isAbilityOnCooldown(final UUID playerUUID) {
        return playerToDynamicCooldown.containsKey(playerUUID)
            && durationElapsedSinceInstant(getLastAbilityInstant(playerUUID)).getSeconds()
                < playerToDynamicCooldown.get(playerUUID);
    }

    @Override
    public double getRemainingCooldown(final UUID playerUUID) {
        return playerToDynamicCooldown.get(playerUUID)
            - ((durationElapsedSinceInstant(getLastAbilityInstant(playerUUID))).toMillis() / 1000.0);
    }

    @Override
    public void initialize(Player player) {
        super.initialize(player);
        playerToDynamicCooldown.put(player.getUniqueId(), 0);
    }

    @Override
    public void terminate(final Player player) {
        super.terminate(player);
        playerToDynamicCooldown.remove(player.getUniqueId());
    }

    public abstract int calculateDynamicCooldown(final long abilityDuration);

}
