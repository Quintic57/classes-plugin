package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;

// TODO: Add ability descriptions
// TODO: Use HumanEntity::setCooldown() method to show CD for certain items
public interface Ability {

    void initialize(final Player player);

    void terminate(final Player player);

}
