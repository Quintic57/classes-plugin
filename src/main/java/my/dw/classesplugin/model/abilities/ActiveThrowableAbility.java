package my.dw.classesplugin.model.abilities;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ActiveThrowableAbility extends ActiveAbility {

    private final Map<UUID, Instant> playerToLastAbilityRefresh;
    private final int maxCharges;
    private final Map<UUID, BukkitRunnable> playerToIncrementChargeTask;
    private final Map<UUID, Integer> playerToChargeCount;

    public ActiveThrowableAbility(final String name,
                                  final ItemStack itemTrigger,
                                  final int duration,
                                  final int staticCooldown) {
        this(name, itemTrigger, duration, staticCooldown, 1);
    }

    public ActiveThrowableAbility(final String name,
                                  final ItemStack itemTrigger,
                                  final int duration,
                                  final int staticCooldown,
                                  final int maxCharges) {
        super(name, itemTrigger, duration, staticCooldown);
        this.playerToLastAbilityRefresh = new HashMap<>();
        this.maxCharges = maxCharges;
        this.playerToIncrementChargeTask = new HashMap<>();
        this.playerToChargeCount = new HashMap<>();
    }

    @Override
    public void handleAbility(final Player player, final ItemStack itemTrigger) {
        if (playerToChargeCount.get(player.getUniqueId()) == 1) {
            itemTrigger.setAmount(2);
        }
        playerToChargeCount.put(player.getUniqueId(), playerToChargeCount.get(player.getUniqueId()) - 1);

        if (!playerToIncrementChargeTask.containsKey(player.getUniqueId())) {
            final BukkitRunnable incrementChargeTask = new BukkitRunnable() {
                @Override
                public void run() {
                    playerToChargeCount.put(player.getUniqueId(), playerToChargeCount.get(player.getUniqueId()) + 1);
                    if (playerToChargeCount.get(player.getUniqueId()) > 1) {
                        itemTrigger.setAmount(itemTrigger.getAmount() + 1);
                    }
                    playerToLastAbilityRefresh.put(player.getUniqueId(), Instant.now());
                    player.sendMessage(getName() + " - " + playerToChargeCount.get(player.getUniqueId()) + "/" + maxCharges
                        + " charges");

                    if (playerToChargeCount.get(player.getUniqueId()) >= maxCharges) {
                        playerToIncrementChargeTask.get(player.getUniqueId()).cancel();
                        playerToIncrementChargeTask.remove(player.getUniqueId());
                    }
                }
            };
            incrementChargeTask.runTaskTimer(
                ClassesPlugin.getPlugin(),
                (long) getStaticCooldown() * Constants.TICKS_PER_SECOND,
                (long) getStaticCooldown() * Constants.TICKS_PER_SECOND
            );
            playerToIncrementChargeTask.put(player.getUniqueId(), incrementChargeTask);
            playerToLastAbilityRefresh.put(player.getUniqueId(), Instant.now());
        }
    }

    @Override
    public boolean isAbilityOnCooldown(final UUID playerUUID) {
        return playerToChargeCount.containsKey(playerUUID) && playerToChargeCount.get(playerUUID) == 0;
    }

    @Override
    public double getRemainingCooldown(UUID playerUUID) {
        return getStaticCooldown()
            - ((durationElapsedSinceInstant(playerToLastAbilityRefresh.get(playerUUID))).toMillis() / 1000.0);
    }

    @Override
    public void initialize(final Player player) {
        playerToLastAbilityRefresh.put(player.getUniqueId(), Instant.now());
        playerToChargeCount.put(player.getUniqueId(), maxCharges);
    }

    @Override
    public void terminate(final Player player) {
        playerToLastAbilityRefresh.remove(player.getUniqueId());
        if (playerToIncrementChargeTask.containsKey(player.getUniqueId())) {
            playerToIncrementChargeTask.get(player.getUniqueId()).cancel();
            playerToIncrementChargeTask.remove(player.getUniqueId());
        }
        playerToChargeCount.remove(player.getUniqueId());
    }

}
