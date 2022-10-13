package me.dave.portaltrading.events;

import me.dave.portaltrading.PortalTrading;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PortalEvents implements Listener {

    @EventHandler
    public void onEntityEnterPortal(EntityPortalEnterEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Item inputEntity)) return;
        Location location = event.getLocation();
        Block block = location.getBlock();
        if (block.getType() != Material.NETHER_PORTAL) return;
        ItemStack outputItem = PortalTrading.configManager.getTradeOutput(inputEntity.getItemStack());
        if (outputItem == null) {
            inputEntity.setPortalCooldown(Integer.MAX_VALUE);
            return;
        }
        World world = location.getWorld();
        if (world == null) return;
        Vector inputVelocity = inputEntity.getVelocity();
        inputEntity.remove();
        Item outputEntity = world.dropItem(location.add(0.5, 0.5, 0.5), outputItem);
        outputEntity.setPortalCooldown(Integer.MAX_VALUE);
        outputEntity.setVelocity(inputVelocity.multiply(-1));
    }

//    @EventHandler
//    public void onEntityTeleport(EntityTeleportEvent event) {
//        Location fromLoc = event.getFrom();
//        Location toLoc = event.getTo();
//        if (fromLoc.getBlock().getType() != Material.NETHER_PORTAL || toLoc == null) return;
//        World toWorld = toLoc.getWorld();
//        if (toWorld == null || toWorld.getEnvironment() != Environment.NETHER) return;
//
//    }
}
