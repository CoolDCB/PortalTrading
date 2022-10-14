package me.dave.portaltrading;

import me.dave.portaltrading.barterloot.BarterLootGen;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PortalEvents implements Listener {
    private final BarterLootGen barterLootGen = new BarterLootGen();

    @EventHandler
    public void onEntityEnterPortal(EntityPortalEnterEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Item inputEntity)) return;
        Location location = event.getLocation();
        Block block = location.getBlock();
        if (block.getType() != Material.NETHER_PORTAL) return;
        ItemStack inputItem = inputEntity.getItemStack();

        if (inputItem.getType() == Material.GOLD_INGOT && PortalTrading.configManager.hasBarterTradesEnabled()) {
            exchangeItem(inputEntity, barterLootGen.generate());
            return;
        }

        ItemStack outputItem = PortalTrading.configManager.getTradeOutput(inputItem);
        if (outputItem == null) {
            inputEntity.setPortalCooldown(Integer.MAX_VALUE);
            return;
        }
        exchangeItem(inputEntity, outputItem);
    }

    public void exchangeItem(Item oldItem, ItemStack newItem) {
        Location location = oldItem.getLocation();
        World world = oldItem.getWorld();
        Vector inputVelocity = oldItem.getVelocity();
        oldItem.remove();
        Item outputEntity = world.dropItem(location.add(0.5, 0.5, 0.5), newItem);
        outputEntity.setPortalCooldown(Integer.MAX_VALUE);
        outputEntity.setVelocity(inputVelocity.multiply(-1));
    }
}
