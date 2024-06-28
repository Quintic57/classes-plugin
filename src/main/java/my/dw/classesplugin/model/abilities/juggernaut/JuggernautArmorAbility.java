package my.dw.classesplugin.model.abilities.juggernaut;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.abilities.PassiveAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.potion.PotionEffect.INFINITE_DURATION;

public class JuggernautArmorAbility extends PassiveAbility {

    private final PotionEffect resistanceEffect;

    private final PotionEffect slowEffect;

    private final Map<Player, Location> playerToLocation;

    public JuggernautArmorAbility() {
        super("Made Like a Tree");
        this.resistanceEffect = new PotionEffect(PotionEffectType.RESISTANCE, (int) (0.9 * Constants.TICKS_PER_SECOND), 0);
        this.slowEffect = new PotionEffect(PotionEffectType.SLOWNESS, INFINITE_DURATION, 0, false, false);
        this.playerToLocation = new HashMap<>();

        final BukkitRunnable task = new BukkitRunnable() {
            private static final double threshold = 0.15;

            @Override
            public void run() {
                for (Player player: playerToLocation.keySet()) {
                    if (Math.abs(player.getLocation().getX() - playerToLocation.get(player).getX()) <= threshold
                        && Math.abs(player.getLocation().getY() - playerToLocation.get(player).getY()) <= threshold
                        && Math.abs(player.getLocation().getZ() - playerToLocation.get(player).getZ()) <= threshold) {
                        player.addPotionEffect(resistanceEffect);
                    }
                    playerToLocation.put(player, player.getLocation());
                }
            }
        };
        task.runTaskTimer(ClassesPlugin.getPlugin(), 0, 4);
    }

    @Override
    public void initialize(final Player player) {
        player.addPotionEffect(slowEffect);
        playerToLocation.put(player, player.getLocation());
    }

    @Override
    public void terminate(final Player player) {
        final Collection<PotionEffect> currentEffects = player.getActivePotionEffects();
        currentEffects.remove(slowEffect);
        player.getActivePotionEffects().forEach(p -> player.removePotionEffect(p.getType()));
        player.addPotionEffects(currentEffects);
        playerToLocation.remove(player);
    }

}
