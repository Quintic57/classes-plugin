package my.dw.classesplugin.listener;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.utils.AbilityUtils;
import org.junit.jupiter.api.Test;

// TODO: Implement
public class ActiveAbilityListenerTest {

    final ActiveAbilityListener activeAbilityListener;

    public ActiveAbilityListenerTest() {
        this.activeAbilityListener = new ActiveAbilityListener();
    }

//    @Test
//    public void handleAbilityTest() {
//        final Player player = Player;
//        final PlayerEvent event = new PlayerInteractEvent();
//        this.classAbilityListener.onPlayerInteractEvent();
//    }

    @Test
    public void temp() {
        System.out.println(AbilityUtils.itemTriggerToActiveAbilityMap);
        System.out.println(AbilityUtils.activeAbilityNameToActiveAbilityMap);
        System.out.println(Class.ASSASSIN.getAbilities());
    }
}
