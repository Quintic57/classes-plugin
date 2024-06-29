package my.dw.classesplugin.model.abilities.swordsman;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.ListenedAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

public class SwordsmanFrenzyAbility extends ActiveAbility implements ListenedAbility<EntityDamageEvent>, Listener {

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
            new PotionEffect(PotionEffectType.STRENGTH, getDuration() * Constants.TICKS_PER_SECOND, 1),
            new PotionEffect(PotionEffectType.SPEED, getDuration() * Constants.TICKS_PER_SECOND, 0)
        );
        this.playerBerserkStatus = new HashMap<>();
    }

    @Override
    public void initializeListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, ClassesPlugin.getPlugin());
    }

    @Override()
    public void handleAbility(final Player player, ItemStack itemTrigger) {
        playerBerserkStatus.put(player.getUniqueId(), true);
        final BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                playerBerserkStatus.put(player.getUniqueId(), false);
            }
        };
        task.runTaskLater(ClassesPlugin.getPlugin(), (long) getDuration() * Constants.TICKS_PER_SECOND);

        player.addPotionEffects(this.effects);
        setLastAbilityInstant(player.getUniqueId(), Instant.now());
    }

    @Override
    public boolean isValidConditionForAbilityEvent(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return false;
        }

        final Player defender = (Player) event.getEntity();
        return Class.isClassEquipped(defender, Class.SWORDSMAN.name())
            && playerBerserkStatus.containsKey(defender.getUniqueId())
            && playerBerserkStatus.get(defender.getUniqueId());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    @Override
    public void handleAbilityEvent(final EntityDamageEvent event) {
        if (!isValidConditionForAbilityEvent(event)) {
            return;
        }

        event.setDamage(event.getDamage() * INCOMING_DMG_MULT);
    }

}
