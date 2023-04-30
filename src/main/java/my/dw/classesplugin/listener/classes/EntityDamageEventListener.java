package my.dw.classesplugin.listener.classes;

import my.dw.classesplugin.model.abilities.ListenedAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EntityDamageEventListener implements Listener {

    private final List<ListenedAbility> entityDamageEventAbilities;

    public EntityDamageEventListener() {
        entityDamageEventAbilities = AbilityUtils.LISTENED_ABILITIES.stream()
            .filter(a -> a.getListenerEventType() == ListenedAbility.ListenerEventType.ENTITY_DAMAGE_EVENT)
            .collect(Collectors.toList());
    }

    @EventHandler
    public void onEntityDamageEvent(final EntityDamageEvent event) {
        for (ListenedAbility a: entityDamageEventAbilities) {
            if (a.isValidConditionForAbilityEvent(event)) {
                a.handleAbilityEvent(event);
            }
        }
    }

}
