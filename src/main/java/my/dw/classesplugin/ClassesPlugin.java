package my.dw.classesplugin;

import my.dw.classesplugin.listener.ClassAbilityListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClassesPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ClassAbilityListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
