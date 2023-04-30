package my.dw.classesplugin.model.abilities;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public abstract class ActiveDynamicAbility extends ActiveAbility {

    protected final Map<UUID, Integer> playerToDynamicCooldown;

    public ActiveDynamicAbility(final String name,
                                final ItemStack itemTrigger,
                                final int duration,
                                final Map<UUID, Integer> playerToDynamicCooldown) {
        super(name, itemTrigger, duration, -1);
        this.playerToDynamicCooldown = playerToDynamicCooldown;
    }

    public abstract int calculateDynamicCooldown(final long abilityDuration);

    @Override
    public boolean isAbilityOnCooldown(final UUID playerUUID) {
        return playerToLastAbilityInstant.containsKey(playerUUID)
            && playerToDynamicCooldown.containsKey(playerUUID)
            && durationElapsedSinceInstant(playerToLastAbilityInstant.get(playerUUID)).getSeconds()
                < playerToDynamicCooldown.get(playerUUID);
    }

    public int getDynamicCooldown(final UUID playerUUID) {
        return playerToDynamicCooldown.get(playerUUID);
    }

}