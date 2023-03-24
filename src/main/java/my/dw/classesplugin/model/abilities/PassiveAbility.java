package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;

public abstract class PassiveAbility implements Ability {

    protected final String name;

    protected final boolean conditional;

    public PassiveAbility(final String name, final boolean conditional) {
        this.name = name;
        this.conditional = conditional;
    }

    public boolean isConditional() {
        return conditional;
    }

    public abstract void removeAbility(final Player player);

}
