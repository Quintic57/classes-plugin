package my.dw.classesplugin.model.abilities.swordsman;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.ListenedAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

//TODO: This might be better as a toggleable ability (i.e. no CD, buffs only last whilst toggled on)
public class SwordsmanFrenzyAbility extends ActiveAbility implements ListenedAbility {

    private final List<PotionEffect> effects;

    private final Map<UUID, Boolean> playerBerserkStatus;

    private static final double INCOMING_DMG_MULT = 1.75;

    public SwordsmanFrenzyAbility() {
        super(
            "Frenzy",
            generateItemMetaTrigger(
                Material.BLAZE_POWDER,
                "Frenzy",
                List.of(
                    "Increases outgoing damage by 6 and speed by 20% for 20s,",
                    "also amplifies incoming damage by 75% for 20s",
                    "Cooldown: 30s"
                )
            ),
            20,
            30
        );
        this.effects = List.of(
            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, this.duration * Constants.TICKS_PER_SECOND, 1),
            new PotionEffect(PotionEffectType.SPEED, this.duration * Constants.TICKS_PER_SECOND, 0)
        );
        this.playerBerserkStatus = new HashMap<>();
    }

    @Override
    public boolean handleAbility(final Player player, ItemStack itemTrigger) {
        playerBerserkStatus.put(player.getUniqueId(), true);
        final BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                playerBerserkStatus.put(player.getUniqueId(), false);
            }
        };
        task.runTaskLater(ClassesPlugin.getPlugin(), (long) this.duration * Constants.TICKS_PER_SECOND);

        return player.addPotionEffects(this.effects);
    }

    @Override
    public ListenerEventType getListenerEventType() {
        return ListenerEventType.ENTITY_DAMAGE_EVENT;
    }

    @Override
    public boolean isValidConditionForAbilityEvent(final Event event) {
        final EntityDamageEvent entityDamageEvent = (EntityDamageEvent) event;
        if (!(entityDamageEvent.getEntity() instanceof Player)) {
            return false;
        }

        final Player defender = (Player) entityDamageEvent.getEntity();
        return Class.isClassEquipped(defender, Class.SWORDSMAN.name())
            && playerBerserkStatus.containsKey(defender.getUniqueId())
            && playerBerserkStatus.get(defender.getUniqueId());
    }

    @Override
    public void handleAbilityEvent(final Event event) {
        final EntityDamageEvent entityDamageEvent = (EntityDamageEvent) event;
        entityDamageEvent.setDamage(entityDamageEvent.getDamage() * INCOMING_DMG_MULT);
    }

}
