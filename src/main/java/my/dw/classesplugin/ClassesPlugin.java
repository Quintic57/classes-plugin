package my.dw.classesplugin;

import my.dw.classesplugin.command.EquipClassCommand;
import my.dw.classesplugin.command.SelectClassCommand;
import my.dw.classesplugin.command.UnequipClassCommand;
import my.dw.classesplugin.listener.ActiveAbilityListener;
import my.dw.classesplugin.listener.ArrowAbilityOnHitListener;
import my.dw.classesplugin.listener.ArrowAbilityOnLaunchListener;
import my.dw.classesplugin.listener.GuiInventoryListener;
import my.dw.classesplugin.listener.PlayerDeathEventListener;
import my.dw.classesplugin.listener.classes.EntityDamageByEntityEventListener;
import my.dw.classesplugin.listener.classes.EntityDamageEventListener;
import my.dw.classesplugin.listener.classes.EntityToggleGlideEventListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

// TODO: Add death handler to remove class metadata on death, so a new class can be selected
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
        getServer().getPluginManager().registerEvents(new EntityDamageEventListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityEventListener(), this);
        getServer().getPluginManager().registerEvents(new EntityToggleGlideEventListener(), this);
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
