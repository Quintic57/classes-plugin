package my.dw.classesplugin.listener;

import my.dw.classesplugin.model.abilities.Ability;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.Objects;

import static my.dw.classesplugin.model.Class.generateItemTriggerToAbilityMap;

public class ClassAbilityListener implements Listener {

    private final Map<String, Ability> itemTriggerToAbility;

    public ClassAbilityListener() {
        this.itemTriggerToAbility = generateItemTriggerToAbilityMap();
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            handlePlayerAbilityEvent(event);
        }
    }

    private void handlePlayerAbilityEvent(PlayerInteractEvent event) {
        if (Objects.isNull(event.getItem()) || Objects.isNull(event.getItem().getItemMeta())) {
            return;
        }

//        final String itemDisplayName = event.getItem().getItemMeta().getDisplayName();
//        if (!itemTriggerToAbility.containsKey(itemDisplayName)) {
//            return;
//        }

        // TODO: The itemTrigger should be based off itemMeta, not off of the base item name
        itemTriggerToAbility.get(event.getItem().getType().toString()).handleAbility(event.getPlayer());
    }

}
