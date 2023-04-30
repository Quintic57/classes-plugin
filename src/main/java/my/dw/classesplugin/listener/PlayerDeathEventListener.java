package my.dw.classesplugin.listener;

import my.dw.classesplugin.model.Class;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathEventListener implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(final PlayerDeathEvent event) {
        final Player player = event.getEntity();

        if (!player.getMetadata(Class.CLASS_METADATA_KEY).isEmpty()) {
            final String className = player.getMetadata(Class.CLASS_METADATA_KEY).get(0).asString();
            Class.valueOf(className).unequipClass(player);
        }
        player.getInventory().clear();
    }

}
