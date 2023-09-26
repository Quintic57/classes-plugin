package my.dw.classesplugin;

import my.dw.classesplugin.command.EquipClassCommand;
import my.dw.classesplugin.command.SelectClassCommand;
import my.dw.classesplugin.command.UnequipClassCommand;
import my.dw.classesplugin.listener.ActiveAbilityListener;
import my.dw.classesplugin.listener.ArrowAbilityOnHitListener;
import my.dw.classesplugin.listener.ArrowAbilityOnLaunchListener;
import my.dw.classesplugin.listener.GuiInventoryListener;
import my.dw.classesplugin.listener.PlayerDeathEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClassesPlugin extends JavaPlugin {

    private static ClassesPlugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

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
