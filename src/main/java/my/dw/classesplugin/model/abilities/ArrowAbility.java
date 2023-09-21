package my.dw.classesplugin.model.abilities;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.utils.AbilityUtils;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ArrowAbility implements Ability {

    public static final String ARROW_METADATA_KEY = "arrow_ability_name";

    private final String name;
    private final ItemStack arrowTrigger;
    private final int cooldown;
    private final Map<UUID, Instant> playerToLastAbilityRefresh;
    private final int maxCharges;
    private final Map<UUID, BukkitRunnable> playerToIncrementChargeTask;
    private final Map<UUID, Integer> playerToChargeCount;

    public ArrowAbility(final String name,
                        final ItemStack arrowTrigger,
                        final int cooldown) {
        this(name, arrowTrigger, cooldown, 1);
    }

    public ArrowAbility(final String name,
                        final ItemStack arrowTrigger,
                        final int cooldown,
                        final int maxCharges) {
        this.name = name;
        this.arrowTrigger = arrowTrigger;
        this.cooldown = cooldown;
        this.playerToLastAbilityRefresh = new HashMap<>();
        this.maxCharges = maxCharges;
        this.playerToIncrementChargeTask = new HashMap<>();
        this.playerToChargeCount = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public ItemStack getArrowTrigger() {
        return arrowTrigger;
    }

    protected int getCooldown() {
        return cooldown;
    }

    public double getRemainingCooldown(final UUID playerUUID) {
        return cooldown
            - (durationElapsedSinceInstant(playerToLastAbilityRefresh.get(playerUUID)).toMillis() / 1000.0);
    }

    public boolean isAbilityOnCooldown(final UUID playerUUID) {
        return playerToChargeCount.containsKey(playerUUID) && playerToChargeCount.get(playerUUID) == 0;
    }

    protected void setLastAbilityRefresh(final UUID playerUUID, final Instant lastAbilityRefresh) {
        playerToLastAbilityRefresh.put(playerUUID, lastAbilityRefresh);
    }

    protected int getMaxCharges() {
        return maxCharges;
    }

    protected void cancelAndRemoveIncrementChargeTask(final UUID playerUUID) {
        if (playerToIncrementChargeTask.containsKey(playerUUID)) {
            playerToIncrementChargeTask.get(playerUUID).cancel();
            playerToIncrementChargeTask.remove(playerUUID);
        }
    }

    protected void setChargeCount(final UUID playerUUID, final Integer chargeCount) {
        playerToChargeCount.put(playerUUID, chargeCount);
    }

    public void onProjectileLaunch(final EntityShootBowEvent event) {
        final AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        final ItemStack arrowTrigger = event.getConsumable();
        final Player player = (Player) event.getEntity();

        arrow.setMetadata(ArrowAbility.ARROW_METADATA_KEY, new FixedMetadataValue(ClassesPlugin.getPlugin(), name));

        if (playerToChargeCount.get(player.getUniqueId()) == 1) {
            AbilityUtils.removeItemsFromPlayer(player.getInventory(), arrowTrigger);
            player.getInventory().addItem(arrowTrigger);
        }
        playerToChargeCount.put(player.getUniqueId(), playerToChargeCount.get(player.getUniqueId()) - 1);

        if (!playerToIncrementChargeTask.containsKey(player.getUniqueId())) {
            final BukkitRunnable incrementChargeTask = new BukkitRunnable() {
                @Override
                public void run() {
                    playerToChargeCount.put(player.getUniqueId(), playerToChargeCount.get(player.getUniqueId()) + 1);
                    if (playerToChargeCount.get(player.getUniqueId()) > 1) {
                        final ItemStack arrowTriggerCopy = ArrowAbility.this.arrowTrigger.clone();
                        arrowTriggerCopy.setAmount(1);
                        AbilityUtils.addItemForPlayer(player.getInventory(), arrowTriggerCopy);
                    }
                    player.sendMessage(name + " - " + playerToChargeCount.get(player.getUniqueId()) + "/" + maxCharges
                        + " charges");

                    if (playerToChargeCount.get(player.getUniqueId()) >= maxCharges) {
                        playerToIncrementChargeTask.get(player.getUniqueId()).cancel();
                        playerToIncrementChargeTask.remove(player.getUniqueId());
                    }
                    playerToLastAbilityRefresh.put(player.getUniqueId(), Instant.now());
                }
            };
            incrementChargeTask.runTaskTimer(
                ClassesPlugin.getPlugin(),
                (long) cooldown * Constants.TICKS_PER_SECOND,
                (long) cooldown * Constants.TICKS_PER_SECOND
            );
            playerToIncrementChargeTask.put(player.getUniqueId(), incrementChargeTask);
            playerToLastAbilityRefresh.put(player.getUniqueId(), Instant.now());
        }
    }

    public void onProjectileHit(final ProjectileHitEvent event) {
        event.getEntity().remove();
    }

    @Override
    public void initialize(final Player player) {
        playerToLastAbilityRefresh.put(player.getUniqueId(), Instant.now());
        playerToChargeCount.put(player.getUniqueId(), maxCharges);
    }

    @Override
    public void terminate(final Player player) {
        playerToLastAbilityRefresh.remove(player.getUniqueId());
        cancelAndRemoveIncrementChargeTask(player.getUniqueId());
        playerToChargeCount.remove(player.getUniqueId());
    }

}
