package my.dw.classesplugin.model.abilities.assassin;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.ListenedAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;
import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

public class AssassinCloakAbility extends ActiveAbility implements ListenedAbility {

    private final List<PotionEffect> effects;

    public AssassinCloakAbility() {
        super(
            "Cloak",
            generateItemMetaTrigger(
                Material.LEATHER,
                "Cloak of Invisibility",
                List.of(
                    "Grants invisibility and 40% increased movement speed for 15s.",
                    "The first strike from this state will deal amplified damage and remove the invisibility",
                    "Cooldown: 25s"
                )
            ),
            15,
            25
        );
        this.effects = List.of(
            new PotionEffect(PotionEffectType.INVISIBILITY, getDuration() * Constants.TICKS_PER_SECOND, 0, false, false),
            new PotionEffect(PotionEffectType.SPEED, getDuration() * Constants.TICKS_PER_SECOND, 1, false, false)
        );
    }

    @Override
    public void handleAbility(final Player player, final ItemStack itemTrigger) {
        player.getActivePotionEffects().forEach(p -> player.addPotionEffect(
            new PotionEffect(p.getType(), p.getDuration() + 1, p.getAmplifier(), false, false)));
        player.addPotionEffects(effects);
        AbilityUtils.removeItemsFromPlayer(player.getInventory(), itemTrigger);

        final BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                resetPlayerState(player);
            }
        };
        task.runTaskLater(ClassesPlugin.getPlugin(), (long) getDuration() * Constants.TICKS_PER_SECOND);
        setLastAbilityInstant(player.getUniqueId(), Instant.now());
    }

    @Override
    public ListenerEventType getListenerEventType() {
        return ListenerEventType.ENTITY_DAMAGE_BY_ENTITY_EVENT;
    }

    @Override
    public boolean isValidConditionForAbilityEvent(final Event event) {
        final EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return false;
        }

        final Player attacker = (Player) entityDamageByEntityEvent.getDamager();
        return Class.isClassEquipped(attacker, Class.ASSASSIN.name())
            && isAbilityDurationActive(attacker.getUniqueId())
            && isAbilityEffectActive(attacker.getActivePotionEffects());
    }

    @Override
    public void handleAbilityEvent(final Event event) {
        final EntityDamageByEntityEvent abilityEvent = (EntityDamageByEntityEvent) event;
        final Player attacker = (Player) abilityEvent.getDamager();

        final long abilityDuration = durationElapsedSinceInstant(
            getLastAbilityInstant(attacker.getUniqueId())).getSeconds();
        abilityEvent.setDamage(abilityEvent.getDamage() * (1.25 + (0.0625 * Math.min(abilityDuration, 8))));
        attacker.removePotionEffect(PotionEffectType.INVISIBILITY);
        resetPlayerState(attacker);
    }

    private boolean isAbilityEffectActive(final Collection<PotionEffect> activeEffects) {
        final List<PotionEffectType> activeEffectTypes = activeEffects.stream()
            .map(PotionEffect::getType)
            .collect(Collectors.toList());

        return this.effects.stream().map(PotionEffect::getType).allMatch(activeEffectTypes::contains);
    }

    private void resetPlayerState(final Player player) {
        player.getActivePotionEffects().stream()
            .filter(p -> !p.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) // ignore assassin armor ability
            .forEach(p -> player.addPotionEffect(
                new PotionEffect(p.getType(), p.getDuration() + 1, p.getAmplifier(), false, true)));
        if (Class.isClassEquipped(player, Class.ASSASSIN.name())
            && !isItemTriggerOnPlayer(player.getInventory())) {
            player.getInventory().addItem(getItemTrigger());
        }
    }

}
