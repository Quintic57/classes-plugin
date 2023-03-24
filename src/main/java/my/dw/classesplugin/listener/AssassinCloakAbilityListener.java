package my.dw.classesplugin.listener;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.assassin.AssassinCloakAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Objects;

public class AssassinCloakAbilityListener implements Listener {

    private final AssassinCloakAbility assassinCloakAbility;

    public AssassinCloakAbilityListener() {
        this.assassinCloakAbility = (AssassinCloakAbility) AbilityUtils.abilityNameToActiveAbilityMap.get("Cloak");
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && isAssassinCloakAbilityEvent((Player) event.getDamager())) {
            handleAssassinCloakAbilityEvent((Player) event.getDamager());
        }
    }

    private void handleAssassinCloakAbilityEvent(final Player attacker) {
        if (assassinCloakAbility.isAbilityDurationActive(attacker.getUniqueId())
                && assassinCloakAbility.isAbilityEffectActive(attacker.getActivePotionEffects())) {
            assassinCloakAbility.removeAbility(attacker);
        }
    }

    private boolean isAssassinCloakAbilityEvent(final Player attacker) {
        return !attacker.getMetadata("character_class").isEmpty()
                && !Objects.isNull(attacker.getMetadata("character_class").get(0))
                && attacker.getMetadata("character_class").get(0).asString().equals(Class.ASSASSIN.name());
    }

}
