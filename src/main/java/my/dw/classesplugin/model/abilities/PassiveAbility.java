package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;

public abstract class PassiveAbility implements Ability, Removable {

    protected final String name;

    public PassiveAbility(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
