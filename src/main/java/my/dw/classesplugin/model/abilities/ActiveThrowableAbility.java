package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class ActiveThrowableAbility extends ActiveAbility {

    public ActiveThrowableAbility(final String name,
                                  final ItemStack itemTrigger,
                                  final int duration,
                                  final int cooldown,
                                  final Map<UUID, Instant> playerCooldowns) {
        super(name, itemTrigger, duration, cooldown, playerCooldowns);
    }

    @Override
    public boolean handleAbility(final Player player) {
        if (this.itemTrigger.equals(player.getInventory().getItemInMainHand())) {
            player.getInventory().getItemInMainHand().setAmount(2);
        } else if (this.itemTrigger.equals(player.getInventory().getItemInOffHand())){
            player.getInventory().getItemInOffHand().setAmount(2);
        } else {
            return false;
        }

        return true;
    }

}
