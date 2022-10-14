package me.dave.portaltrading;

import me.dave.portaltrading.barterloot.BarterLootGen;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PortalEvents implements Listener {
    private final PortalTrading plugin = PortalTrading.getInstance();
    private final BarterLootGen barterLootGen = new BarterLootGen();

    @EventHandler
    public void onEntityEnterPortal(EntityPortalEnterEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Item inputEntity)) return;
        Location location = event.getLocation();
        Block block = location.getBlock();
        if (block.getType() != Material.NETHER_PORTAL) return;
        inputEntity.setPortalCooldown(Integer.MAX_VALUE);
        if (inputEntity.getThrower() == null) return;

        ItemStack inputItem = inputEntity.getItemStack();
        Material oldMaterial = inputItem.getType();

        boolean hasTrade = PortalTrading.configManager.hasTrade(oldMaterial);
        boolean barterEnabled = PortalTrading.configManager.hasBarterTradesEnabled();
        boolean isGoldIngot = oldMaterial == Material.GOLD_INGOT;
        if (!hasTrade && !(barterEnabled && isGoldIngot)) return;

        World world = inputEntity.getWorld();
        int stackSize = inputItem.getAmount();
        Vector reverseVelocity = inputEntity.getVelocity().clone().multiply(-1);
        inputEntity.remove();

        int tickOffset = 0;
        int ticksPerItem = PortalTrading.configManager.getTicksPerItem();
        for (int i = 0; i < stackSize; i++) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                ItemStack outputItem;
                if (oldMaterial == Material.GOLD_INGOT && PortalTrading.configManager.hasBarterTradesEnabled()) {
                    outputItem = barterLootGen.generate();
                } else {
                    outputItem = PortalTrading.configManager.getTradeOutput(inputItem);
                }

                if (outputItem == null) return;
                Item outputEntity = world.dropItem(location.clone().add(0.5, 0.5, 0.5), outputItem);
                outputEntity.setPortalCooldown(Integer.MAX_VALUE);
                outputEntity.setVelocity(reverseVelocity.clone());
                world.playSound(location, Sound.BLOCK_END_PORTAL_FRAME_FILL, 0.6f, 2f);
            }, tickOffset);
            tickOffset += ticksPerItem;
        }
    }
}
