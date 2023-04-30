package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;

public interface InitializedAbility {

    void initialize(final Player player);

    void terminate(final Player player);

}
