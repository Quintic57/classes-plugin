package my.dw.classesplugin.listener.classes;

import my.dw.classesplugin.model.abilities.ListenedAbility;
import my.dw.classesplugin.utils.AbilityUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EntityToggleGlideEventListener implements Listener {

    private final List<ListenedAbility> entityToggleGlideEventAbilities;

    public EntityToggleGlideEventListener() {
        entityToggleGlideEventAbilities = AbilityUtils.LISTENED_ABILITIES.stream()
            .filter(a -> a.getListenerEventType() == ListenedAbility.ListenerEventType.ENTITY_TOGGLE_GLIDE_EVENT)
            .collect(Collectors.toList());
    }

    @EventHandler
    public void onEntityToggleGlideEvent(final EntityToggleGlideEvent event) {
        for (ListenedAbility a: entityToggleGlideEventAbilities) {
            if (a.isValidConditionForAbilityEvent(event)) {
                a.handleAbilityEvent(event);
            }
        }
    }

}
