package my.dw.classesplugin.model.abilities;

import org.bukkit.event.Event;

public interface ListenedAbility {

    enum ListenerEventType {
        ENTITY_DAMAGE_EVENT, ENTITY_DAMAGE_BY_ENTITY_EVENT, ENTITY_TOGGLE_GLIDE_EVENT;
    }

    ListenerEventType getListenerEventType();

    boolean isValidConditionForAbilityEvent(final Event event);

    void handleAbilityEvent(final Event event);

}
