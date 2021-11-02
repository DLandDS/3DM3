package id.forgeforce.tridiemtri;

import org.bukkit.plugin.java.JavaPlugin;

public final class Tridiemtri extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        ItemManager.init();
        getCommand("tridiemtri").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new ItemEvents(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
