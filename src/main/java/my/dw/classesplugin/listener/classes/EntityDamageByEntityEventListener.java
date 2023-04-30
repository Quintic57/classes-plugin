package my.dw.classesplugin.listener.classes;

import my.dw.classesplugin.model.abilities.ListenedAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EntityDamageByEntityEventListener implements Listener {

    private final List<ListenedAbility> entityDamageByEntityAbilities;

    public EntityDamageByEntityEventListener() {
        entityDamageByEntityAbilities = AbilityUtils.LISTENED_ABILITIES.stream()
            .filter(a -> a.getListenerEventType() == ListenedAbility.ListenerEventType.ENTITY_DAMAGE_BY_ENTITY_EVENT)
            .collect(Collectors.toList());
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent event) {
        for (ListenedAbility a: entityDamageByEntityAbilities) {
            if (a.isValidConditionForAbilityEvent(event)) {
                a.handleAbilityEvent(event);
            }
        }
    }

}
