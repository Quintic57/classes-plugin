package my.dw.classesplugin.model.abilities;

import org.bukkit.event.Event;

public interface ListenedAbility<T extends Event> {

    boolean isValidConditionForAbilityEvent(final T event);

    void handleAbilityEvent(final T event);

    void initializeListener();

}
