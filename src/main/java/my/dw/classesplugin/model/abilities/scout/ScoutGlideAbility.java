package my.dw.classesplugin.model.abilities.scout;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;
import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ActiveDynamicAbility;
import my.dw.classesplugin.model.abilities.ListenedAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ScoutGlideAbility extends ActiveDynamicAbility implements ListenedAbility {

    private final Map<UUID, Set<BukkitRunnable>> playerToAbilityTasks;

    public ScoutGlideAbility() {
        super(
            "Para-glider",
            generateItemMetaTrigger(
                Material.ELYTRA,
                "Para-glider",
                List.of(
                    "Allows the user to glide through the air for a max of 20s",
                    "Cooldown: Max 180s, increases based on flight duration"
                ),
                List.of(ItemFlag.HIDE_ATTRIBUTES)
            ),
            20,
            new HashMap<>()
        );
        playerToAbilityTasks = new HashMap<>();
    }

    @Override
    public void handleAbility(final Player player, final ItemStack itemTrigger) {
        // ability logic is handled inherently by the elytra item functionality
    }

    @Override
    public ListenerEventType getListenerEventType() {
        return ListenerEventType.ENTITY_TOGGLE_GLIDE_EVENT;
    }

    @Override
    public boolean isValidConditionForAbilityEvent(final Event event) {
        final EntityToggleGlideEvent entityToggleGlideEvent = (EntityToggleGlideEvent) event;
        if (!(entityToggleGlideEvent.getEntity() instanceof Player)) {
            return false;
        }

        final Player player = (Player) entityToggleGlideEvent.getEntity();
        // Since player can manually equip glider, have to check cooldown again when handling gliding event.
        if (isAbilityOnCooldown(player.getUniqueId())) {
            player.sendMessage(
                getName()
                + " is on cooldown. Remaining CD: "
                + String.format("%.2f", getRemainingCooldown(player.getUniqueId()))
                + " seconds"
            );
            resetChestArmor(player.getInventory());
            entityToggleGlideEvent.setCancelled(true);
            return false;
        }

        return Class.isClassEquipped(player, Class.SCOUT.name());
    }

    @Override
    public void handleAbilityEvent(final Event event) {
        final EntityToggleGlideEvent abilityEvent = (EntityToggleGlideEvent) event;
        final Player player = (Player) abilityEvent.getEntity();
        final PlayerInventory inventory = player.getInventory();

        if (abilityEvent.isGliding()) {
            final BukkitRunnable messageTask = new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage("Ability duration will end in 5 seconds");
                }
            };
            final BukkitRunnable equipArmorTask = new BukkitRunnable() {
                @Override
                public void run() {
                    inventory.setChestplate(null);
                }
            };
            messageTask.runTaskLater(ClassesPlugin.getPlugin(), (long) (getDuration() - 5) * Constants.TICKS_PER_SECOND);
            equipArmorTask.runTaskLater(ClassesPlugin.getPlugin(), (long) getDuration() * Constants.TICKS_PER_SECOND);
            playerToAbilityTasks.put(player.getUniqueId(), Set.of(messageTask, equipArmorTask));
            setDynamicCooldown(player.getUniqueId(), 0);
        } else {
            resetChestArmor(inventory);
            if (!playerToAbilityTasks.containsKey(player.getUniqueId())) {
                return;
            }
            playerToAbilityTasks.get(player.getUniqueId()).forEach(BukkitRunnable::cancel);
            playerToAbilityTasks.remove(player.getUniqueId());
            setDynamicCooldown(player.getUniqueId(), calculateDynamicCooldown(durationElapsedSinceInstant(
                getLastAbilityInstant(player.getUniqueId())).getSeconds()));
        }

        setLastAbilityInstant(player.getUniqueId(), Instant.now());
    }

    @Override
    public int calculateDynamicCooldown(final long abilityDuration) {
        final int cooldownMultiplier;
        if (abilityDuration < 5) {
            cooldownMultiplier = 4;
        } else if (abilityDuration < 10) {
            cooldownMultiplier = 6;
        } else {
            cooldownMultiplier = 9;
        }

        return (int) Math.ceil(abilityDuration) * cooldownMultiplier;
    }

    private void resetChestArmor(final PlayerInventory inventory) {
        AbilityUtils.removeItemsFromPlayer(inventory, Class.SCOUT.getArmor().getChestplate(), getItemTrigger());
        inventory.setChestplate(Class.SCOUT.getArmor().getChestplate());
        inventory.addItem(getItemTrigger());
    }

}
