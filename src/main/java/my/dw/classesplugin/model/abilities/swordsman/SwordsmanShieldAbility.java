package my.dw.classesplugin.model.abilities.swordsman;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.ListenedAbility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SwordsmanShieldAbility implements Ability, ListenedAbility {

    private static final double MIN_DAMAGE_THRESHOLD = 0.5;

    private static final double SHIELD_DAMAGE_REDUCTION = 0.85;

    @Override
    public boolean handleAbility(final Player player) {
        return true; // ability logic is handled inherently by the shield item
    }

    @Override
    public ListenerEventType getListenerEventType() {
        return ListenerEventType.ENTITY_DAMAGE_BY_ENTITY_EVENT;
    }

    @Override
    public boolean isValidConditionForAbilityEvent(final Event event) {
        final EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return false;
        }

        final Player defender = (Player) entityDamageByEntityEvent.getEntity();
        return Class.isClassEquipped(defender, Class.SWORDSMAN.name())
            && defender.isBlocking()
            && defender.getHealth() > 0;
    }

    @Override
    public void handleAbilityEvent(final Event event) {
        final EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
        final Player defender = (Player) entityDamageByEntityEvent.getEntity();
        final double damageAfterReduction = entityDamageByEntityEvent.getDamage() * (1 - SHIELD_DAMAGE_REDUCTION);

        // TODO: Cleaner way to do this?
        if (defender.getHealth() - damageAfterReduction <= 0) {
            defender.getInventory().remove(Material.SHIELD);
            final BukkitRunnable killTask = new BukkitRunnable() {
                @Override
                public void run() {
                    defender.damage(damageAfterReduction * 2, entityDamageByEntityEvent.getDamager());
                }
            };
            killTask.runTaskLater(ClassesPlugin.getPlugin(), 4);
        } else if (damageAfterReduction > MIN_DAMAGE_THRESHOLD) {
            defender.setHealth(defender.getHealth() - damageAfterReduction);
            defender.sendHurtAnimation(0);
        }
    }

}
