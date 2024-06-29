package my.dw.classesplugin;

import my.dw.classesplugin.command.EquipClassCommand;
import my.dw.classesplugin.command.SelectClassCommand;
import my.dw.classesplugin.command.UnequipClassCommand;
import my.dw.classesplugin.listener.ActiveAbilityListener;
import my.dw.classesplugin.listener.ArrowAbilityOnHitListener;
import my.dw.classesplugin.listener.ArrowAbilityOnLaunchListener;
import my.dw.classesplugin.listener.GuiInventoryListener;
import my.dw.classesplugin.listener.PlayerDeathEventListener;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.model.abilities.Ability;
import my.dw.classesplugin.model.abilities.ActiveAbility;
import my.dw.classesplugin.model.abilities.ArrowAbility;
import my.dw.classesplugin.model.abilities.ListenedAbility;
import my.dw.classesplugin.utils.ItemStackKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class ClassesPlugin extends JavaPlugin {

    public static Map<ItemStackKey, ActiveAbility> ITEM_TRIGGER_TO_ACTIVE_ABILITY;
    public static Map<ItemStackKey, ArrowAbility> ARROW_TRIGGER_TO_ARROW_ABILITY;
    public static Map<String, ArrowAbility> ABILITY_NAME_TO_ARROW_ABILITY;
    public static Map<Ability, String> ABILITY_TO_CLASS_NAME;
    private static ClassesPlugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        ITEM_TRIGGER_TO_ACTIVE_ABILITY = Arrays.stream(Class.values())
            .flatMap(c -> c.getActiveAbilities().stream())
            .collect(Collectors.toMap(a -> new ItemStackKey(a.getItemTrigger()), a -> a));
        ARROW_TRIGGER_TO_ARROW_ABILITY = Arrays.stream(Class.values())
            .flatMap(c -> c.getArrowAbilities().stream())
            .collect(Collectors.toMap(a -> new ItemStackKey(a.getArrowTrigger()), a -> a));
        ABILITY_NAME_TO_ARROW_ABILITY = Arrays.stream(Class.values())
            .flatMap(c -> c.getArrowAbilities().stream())
            .collect(Collectors.toMap(ArrowAbility::getName, a -> a));
        ABILITY_TO_CLASS_NAME = new HashMap<>();
        Arrays.stream(Class.values())
            .forEach(c -> c.getAbilities().forEach(a -> ABILITY_TO_CLASS_NAME.put(a, c.name())));

        // @PostConstruct for Listened Abilities
        Arrays.stream(Class.values())
            .flatMap(c -> c.getAbilities().stream())
            .filter(a -> a instanceof ListenedAbility<?>)
            .map(a -> (ListenedAbility<?>) a)
            .forEach(ListenedAbility::initializeListener);

        getServer().getPluginManager().registerEvents(new GuiInventoryListener(), this);
        getCommand("selectclass").setExecutor(new SelectClassCommand());
        getServer().getPluginManager().registerEvents(new PlayerDeathEventListener(), this);

        getServer().getPluginManager().registerEvents(new ActiveAbilityListener(), this);
        getServer().getPluginManager().registerEvents(new ArrowAbilityOnLaunchListener(), this);
        getServer().getPluginManager().registerEvents(new ArrowAbilityOnHitListener(), this);
        getCommand("unequipclass").setExecutor(new UnequipClassCommand());
        getCommand("equipclass").setExecutor(new EquipClassCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ClassesPlugin getPlugin() {
        return plugin;
    }

}
