package my.dw.classesplugin.listener.classes;

import my.dw.classesplugin.model.Class;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Objects;

public class SwordsmanShieldAbilityListener implements Listener {

    private static double MIN_DAMAGE_THRESHOLD = 0.5;

    private static double DAMAGE_REDUCTION = 0.85;

    @EventHandler
    public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && isSwordsmanShieldEvent((Player) event.getEntity())) {
            handleSwordsmanShieldAbilityEvent(event);
        }
    }

    private void handleSwordsmanShieldAbilityEvent(final EntityDamageByEntityEvent event) {
        final Player defender = (Player) event.getEntity();
        final double damageAfterReduction = (defender.getHealth() - (event.getDamage() * (1 - DAMAGE_REDUCTION)) < 0)
            ? defender.getHealth()
            : event.getDamage() * (1 - DAMAGE_REDUCTION);

        if (defender.getHealth() - damageAfterReduction == 0 || damageAfterReduction > MIN_DAMAGE_THRESHOLD) {
            defender.setHealth(defender.getHealth() - damageAfterReduction);
        }
    }

    private boolean isSwordsmanShieldEvent(final Player defender) {
        return Class.isClassEquipped(defender, Class.SWORDSMAN.name()) && defender.isBlocking();
    }

}
