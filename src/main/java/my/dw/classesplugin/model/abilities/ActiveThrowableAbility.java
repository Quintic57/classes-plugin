package my.dw.classesplugin.model.abilities;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// TODO: There may be another way to implement this w/o using runnables. Have a Map of player -> queue w/ last ability instances
//  up to the maxCharges. That way, you will know how many charges you have at any given point in time by comparing current instance
//  to the previous usages. Downside to this is you'll only have one of the item in the inventory, and charges will be added silently
public abstract class ActiveThrowableAbility extends ActiveAbility implements InitializedAbility {

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
        this.maxCharges = maxCharges;
        this.playerToIncrementChargeTask = new HashMap<>();
        this.playerToChargeCount = new HashMap<>();
    }

    @Override
    public boolean handleAbility(final Player player, final ItemStack itemTrigger) {
//        System.out.println(playerToChargeCount.get(player.getUniqueId()));
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
                    setLastAbilityInstant(player.getUniqueId(), Instant.now());
                    player.sendMessage(name + " - " + playerToChargeCount.get(player.getUniqueId()) + "/" + maxCharges
                        + " charges");

                    if (playerToChargeCount.get(player.getUniqueId()) >= maxCharges) {
                        playerToIncrementChargeTask.get(player.getUniqueId()).cancel();
                        playerToIncrementChargeTask.remove(player.getUniqueId());
                    }
                }
            };
            incrementChargeTask.runTaskTimer(
                ClassesPlugin.getPlugin(),
                (long) staticCooldown * Constants.TICKS_PER_SECOND,
                (long) staticCooldown * Constants.TICKS_PER_SECOND
            );
            playerToIncrementChargeTask.put(player.getUniqueId(), incrementChargeTask);
            setLastAbilityInstant(player.getUniqueId(), Instant.now());
        }

        return false;
    }

    @Override
    public boolean isAbilityOnCooldown(final UUID playerUUID) {
        return playerToChargeCount.containsKey(playerUUID) && playerToChargeCount.get(playerUUID) == 0;
    }

    @Override
    public void initialize(final Player player) {
        playerToChargeCount.put(player.getUniqueId(), maxCharges);
    }

    @Override
    public void terminate(final Player player) {
        if (playerToIncrementChargeTask.containsKey(player.getUniqueId())) {
            playerToIncrementChargeTask.get(player.getUniqueId()).cancel();
            playerToIncrementChargeTask.remove(player.getUniqueId());
        }
        playerToChargeCount.remove(player.getUniqueId());
    }

}
