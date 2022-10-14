package me.dave.portaltrading;

import me.dave.portaltrading.PortalTrading;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ConfigManager {
    private final PortalTrading plugin = PortalTrading.getInstance();
    private String prefix;
    private boolean hasBarterTradesEnabled;
    private final HashMap<Material, ItemStack> tradeMap = new HashMap<>();

    public ConfigManager() {
        plugin.saveDefaultConfig();
        reloadConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        tradeMap.clear();

        prefix = config.getString("prefix", "");
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        hasBarterTradesEnabled = config.getBoolean("barter-trades");

        ConfigurationSection tradesSection = config.getConfigurationSection("trades");
        if (tradesSection == null) return;
        for (String input : tradesSection.getKeys(false)) {
            ConfigurationSection inputSection = tradesSection.getConfigurationSection(input);
            if (inputSection == null) continue;
            Material inputMaterial = Material.valueOf(input);
            ItemStack outputItem = new ItemStack(Material.valueOf(inputSection.getString("material", "AIR")), inputSection.getInt("amount", 1));
            tradeMap.put(inputMaterial, outputItem);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean hasBarterTradesEnabled() {
        return hasBarterTradesEnabled;
    }

    public ItemStack getTradeOutput(ItemStack itemStack) {
        Material material = itemStack.getType();
        if (!tradeMap.containsKey(material)) return null;
        ItemStack outputItemStack = tradeMap.get(material).clone();
        int outputAmount = outputItemStack.getAmount() * itemStack.getAmount();
        outputItemStack.setAmount(outputAmount);
        return outputItemStack;
    }
}
