package my.dw.classesplugin.utils;

import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class AbilityUtils {

    public final static Map<ItemMeta, ActiveAbility> itemTriggerToActiveAbilityMap;
    public final static Map<String, ActiveAbility> abilityNameToActiveAbilityMap;

    static {
        itemTriggerToActiveAbilityMap = Arrays.stream(Class.values())
                .flatMap(c -> c.getActiveAbilities().stream())
                .collect(Collectors.toMap(a -> a.getItemTrigger().getItemMeta(), a -> a));
        abilityNameToActiveAbilityMap = Arrays.stream(Class.values())
                .flatMap(c -> c.getActiveAbilities().stream())
                .collect(Collectors.toMap(ActiveAbility::getName, a -> a));
    }

}
