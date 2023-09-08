package my.dw.classesplugin.model.abilities.swordsman;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.ListenedAbility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

// TODO: Should probably convert this to an ActiveAbility (similar to how ScoutGlideAbility works)
public class SwordsmanShieldAbility implements Ability, ListenedAbility {

    private static final double MIN_DAMAGE_THRESHOLD = 0.5;

    private static final double SHIELD_DAMAGE_REDUCTION = 0.85;

    private final ItemStack shieldItem;

    public SwordsmanShieldAbility() {
        final ItemStack shieldItem = new ItemStack(Material.SHIELD);
        final ItemMeta shieldMeta = shieldItem.getItemMeta();
        shieldMeta.setUnbreakable(true);
        shieldItem.setItemMeta(shieldMeta);
        this.shieldItem = shieldItem;
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
            final BukkitRunnable killTask =  new BukkitRunnable() {
                @Override
                public void run() {
                    defender.getInventory().addItem(shieldItem);
                    defender.damage(1000, entityDamageByEntityEvent.getDamager());
                }
            };
            killTask.runTaskLater(ClassesPlugin.getPlugin(), 4);
        } else if (damageAfterReduction > MIN_DAMAGE_THRESHOLD) {
            defender.setHealth(defender.getHealth() - damageAfterReduction);
            defender.sendHurtAnimation(0);
        }
    }

}
