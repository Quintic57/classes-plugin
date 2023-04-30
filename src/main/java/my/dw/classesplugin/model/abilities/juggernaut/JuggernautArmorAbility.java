package my.dw.classesplugin.model.abilities.juggernaut;

import static org.bukkit.potion.PotionEffect.INFINITE_DURATION;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.abilities.PassiveAbility;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class JuggernautArmorAbility extends PassiveAbility {

    private final PotionEffect resistanceEffect;

    private final PotionEffect slowEffect;

    private final Map<Player, Location> playerToLastRecordedLocation;

    public JuggernautArmorAbility() {
        super("Made Like a Tree");
        this.resistanceEffect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (0.9 * Constants.TICKS_PER_SECOND), 0);
        this.slowEffect = new PotionEffect(PotionEffectType.SLOW, INFINITE_DURATION, 0, false, false);
        this.playerToLastRecordedLocation = new HashMap<>();

        final BukkitRunnable task = new BukkitRunnable() {
            private static final double distanceThreshold = 0.15;

            @Override
            public void run() {
                for (Player player: playerToLastRecordedLocation.keySet()) {
                    if (Math.abs(player.getLocation().getX() - playerToLastRecordedLocation.get(player).getX()) <= distanceThreshold
                        && Math.abs(player.getLocation().getY() - playerToLastRecordedLocation.get(player).getY()) <= distanceThreshold
                        && Math.abs(player.getLocation().getZ() - playerToLastRecordedLocation.get(player).getZ()) <= distanceThreshold) {
                        player.addPotionEffect(resistanceEffect);
                    }
                    playerToLastRecordedLocation.put(player, player.getLocation());
                }
            }
        };
        task.runTaskTimer(ClassesPlugin.getPlugin(), 0, 4);
    }

    @Override
    public boolean handleAbility(final Player player) {
        player.addPotionEffect(slowEffect);
        playerToLastRecordedLocation.put(player, player.getLocation());

        return true;
    }

    @Override
    public void initialize(final Player player) {
        handleAbility(player);
    }

    @Override
    public void terminate(final Player player) {
        player.removePotionEffect(slowEffect.getType());
        playerToLastRecordedLocation.remove(player);
    }

}
