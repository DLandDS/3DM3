package id.forgeforce.tridiemtri;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tridiemtri extends JavaPlugin {

    static Plugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        ItemManager.init();
        getCommand("tridiemtri").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new ItemEvents(),this);

    }

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
