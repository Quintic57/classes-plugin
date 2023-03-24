package my.dw.classesplugin;

import my.dw.classesplugin.command.EquipClassCommand;
import my.dw.classesplugin.command.UnequipClassCommand;
import my.dw.classesplugin.listener.AssassinCloakAbilityListener;
import my.dw.classesplugin.listener.ActiveAbilityListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClassesPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ActiveAbilityListener(), this);
        getServer().getPluginManager().registerEvents(new AssassinCloakAbilityListener(), this);
        getCommand("unequipclass").setExecutor(new UnequipClassCommand(this));
        getCommand("equipclass").setExecutor(new EquipClassCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
