package my.dw.classesplugin.model.abilities.assassin;

import static my.dw.classesplugin.utils.AbilityUtils.durationElapsedSinceInstant;
import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AssassinCloakAbility extends ActiveAbility implements ListenedAbility {

    private final List<PotionEffect> effects;

    public AssassinCloakAbility() {
        super(
            "Cloak",
            generateItemMetaTrigger(
                Material.LEATHER,
                "Cloak of Invisibility",
                List.of("Grants invisibility and 40% increased movement speed for 15s, also cleanses the user.",
                    "Cooldown: 25s")
            ),
            15,
            25
        );
        this.effects = List.of(
            new PotionEffect(PotionEffectType.INVISIBILITY, this.duration * Constants.TICKS_PER_SECOND, 0, false, false),
            new PotionEffect(PotionEffectType.SPEED, this.duration * Constants.TICKS_PER_SECOND, 1, false, false)
        );
    }

    @Override
    public boolean handleAbility(final Player player) {
        final boolean abilityApplied = player.addPotionEffects(this.effects);
        if (!abilityApplied) {
            return false;
        }

        final BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (Class.isClassEquipped(player, Class.ASSASSIN.name())
                    && !isItemTriggerOnPlayer(player.getInventory())) {
                    player.getInventory().addItem(AssassinCloakAbility.this.itemTrigger);
                }
            }
        };
        task.runTaskLater(ClassesPlugin.getPlugin(), (long) this.duration * Constants.TICKS_PER_SECOND);
        player.getActivePotionEffects().forEach(p -> player.addPotionEffect(
            new PotionEffect(p.getType(), p.getDuration() + 1, p.getAmplifier(), false, false)));
        AbilityUtils.removeItemsFromPlayer(player.getInventory(), this.itemTrigger);

        return true;
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
            playerToLastAbilityInstant.get(attacker.getUniqueId())).getSeconds();
        abilityEvent.setDamage(abilityEvent.getDamage() * (1.75 + (0.0625 * Math.min(abilityDuration, 8))));

        this.effects.stream().map(PotionEffect::getType).forEach(attacker::removePotionEffect);
        attacker.getActivePotionEffects().stream()
            .filter(p -> !p.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) // ignore assassin armor ability
            .forEach(p -> attacker.addPotionEffect(
                new PotionEffect(p.getType(), p.getDuration() + 1, p.getAmplifier(), false, true)));
        if (!isItemTriggerOnPlayer(attacker.getInventory())) {
            attacker.getInventory().addItem(AssassinCloakAbility.this.itemTrigger);
        }
    }

    private boolean isAbilityEffectActive(final Collection<PotionEffect> activeEffects) {
        final List<PotionEffectType> activeEffectTypes = activeEffects.stream()
            .map(PotionEffect::getType)
            .collect(Collectors.toList());

        return this.effects.stream().map(PotionEffect::getType).allMatch(activeEffectTypes::contains);
    }

}
