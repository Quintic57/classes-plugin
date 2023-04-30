package my.dw.classesplugin.model.abilities;

public abstract class PassiveAbility implements Ability, InitializedAbility {

    protected final String name;

    public PassiveAbility(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
