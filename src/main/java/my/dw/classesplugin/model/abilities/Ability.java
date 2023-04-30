package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;

// TODO: Add ability descriptions
// TODO: Use HumanEntity::setCooldown() method to show CD for items
public interface Ability {

    boolean handleAbility(final Player player);

}
