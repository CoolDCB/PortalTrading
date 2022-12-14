package me.dave.portaltrading;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PortalTrading extends JavaPlugin {
    private static PortalTrading plugin;
    public static ConfigManager configManager;

    @Override
    public void onEnable() {
        plugin = this;
        configManager = new ConfigManager();
        Listener[] listeners = new Listener[] { new PortalEvents() };
        registerEvents(listeners);
        getCommand("portaltrading").setExecutor(new PortalCmd());
    }

    public static PortalTrading getInstance() { return plugin; }

    public void registerEvents(Listener[] listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
