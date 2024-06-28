package my.dw.classesplugin.model.abilities.swordsman;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ListenedAbility;
import my.dw.classesplugin.model.abilities.PassiveAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import my.dw.classesplugin.utils.Constants;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SwordsmanShieldAbility extends PassiveAbility implements ListenedAbility {

    private static final int MAX_SHIELD_HEALTH = 336;
    private static final int SHIELD_DAMAGE_MULT = 12;

    private final ItemStack shieldItem;
    private final Map<UUID, BukkitRunnable> playerToShieldRegenTask;

    public SwordsmanShieldAbility() {
        super("Shield");

        final ItemStack shieldItem = new ItemStack(Material.SHIELD);
        final ItemMeta shieldMeta = shieldItem.getItemMeta();
        shieldMeta.addEnchant(Enchantment.UNBREAKING, 1000, true);
        shieldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        shieldItem.setItemMeta(shieldMeta);
        this.shieldItem = shieldItem;
        playerToShieldRegenTask = new HashMap<>();
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
            && defender.isBlocking();
    }

    @Override
    public void handleAbilityEvent(final Event event) {
        final EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
        final Player defender = (Player) entityDamageByEntityEvent.getEntity();
        final ItemStack shield = defender.getItemInUse();
        final Damageable shieldMeta = (Damageable) shield.getItemMeta();
        final int damageOnShield = shieldMeta.getDamage();
        final int damage = (int) Math.ceil(entityDamageByEntityEvent.getDamage() * SHIELD_DAMAGE_MULT);

        if (damageOnShield + damage >= MAX_SHIELD_HEALTH) {
            defender.playSound(defender.getLocation(), Sound.ITEM_SHIELD_BREAK, 1F, 1F);
            defender.setCooldown(Material.SHIELD, 15 * Constants.TICKS_PER_SECOND);
            AbilityUtils.removeItemsFromPlayer(defender.getInventory(), shield);
            final BukkitRunnable task =  new BukkitRunnable() {
                @Override
                public void run() {
                    shieldMeta.setDamage(0);
                    shield.setItemMeta(shieldMeta);
                    defender.getInventory().addItem(shield);
                }
            };
            task.runTaskLater(ClassesPlugin.getPlugin(), 4);
            return;
        }

        shieldMeta.setDamage(damageOnShield + damage);
        shield.setItemMeta(shieldMeta);
        if (playerToShieldRegenTask.containsKey(defender.getUniqueId())) {
            playerToShieldRegenTask.get(defender.getUniqueId()).cancel();
        }
        final BukkitRunnable shieldRegenTask =  new BukkitRunnable() {
            @Override
            public void run() {
                shieldMeta.setDamage(0);
                shield.setItemMeta(shieldMeta);
            }
        };
        shieldRegenTask.runTaskLater(ClassesPlugin.getPlugin(), 15L * Constants.TICKS_PER_SECOND);
        playerToShieldRegenTask.put(defender.getUniqueId(), shieldRegenTask);
    }

    @Override
    public void initialize(final Player player) {
        player.getInventory().addItem(shieldItem);
    }

    @Override
    public void terminate(final Player player) {
        if (playerToShieldRegenTask.containsKey(player.getUniqueId())) {
            playerToShieldRegenTask.get(player.getUniqueId()).cancel();
            playerToShieldRegenTask.remove(player.getUniqueId());
        }
    }

}
