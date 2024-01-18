package me.dave.portaltrading.listener;

import me.dave.portaltrading.PortalTrading;
import me.dave.portaltrading.barterloot.BarterLootGen;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class ItemListener implements Listener {
    private final PortalTrading plugin = PortalTrading.getInstance();

    @EventHandler
    public void onEntityEnterPortal(EntityPortalEnterEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Item inputEntity)) return;
        Location location = event.getLocation();
        Block block = location.getBlock();
        if (block.getType() != Material.NETHER_PORTAL || inputEntity.getThrower() == null || inputEntity.hasMetadata("PortalTrading")) return;
        inputEntity.setPortalCooldown(Integer.MAX_VALUE);

        ItemStack inputItem = inputEntity.getItemStack();
        Material oldMaterial = inputItem.getType();

        boolean hasTrade = PortalTrading.configManager.hasTrade(oldMaterial);
        boolean barterEnabled = PortalTrading.configManager.hasBarterTradesEnabled();
        boolean isGoldIngot = oldMaterial == Material.GOLD_INGOT;
        if (!hasTrade && !(barterEnabled && isGoldIngot)) return;

        World world = inputEntity.getWorld();
        int stackSize = inputItem.getAmount();

        Vector inputVelocity = inputEntity.getVelocity().clone();
        Orientable blockData = (Orientable) block.getState().getBlockData();
        double x = 0;
        double z = 0;
        Axis portalAxis = blockData.getAxis();
        switch(portalAxis) {
            case X -> {
                if (inputVelocity.getZ() > 0) z = -0.2;
                else z = 0.2;
            }
            case Z -> {
                if (inputVelocity.getX() > 0) x = -0.2;
                else x = 0.2;
            }
        }
        Vector outputVelocity = new Vector(x, -0.1, z);

        inputEntity.remove();

        Location midpoint = location.clone().add(0.5, 0.5, 0.5);

        Block corner1 = location.getBlock();
        int i = 0;
        boolean corner1Found = false;
        while (!corner1Found && i < 3) {
            Block checkBlock = corner1;
            switch(portalAxis) {
                case X -> checkBlock = corner1.getLocation().add(-1, 0, 0).getBlock();
                case Z -> checkBlock = corner1.getLocation().add(0, 0, -1).getBlock();
            }
            if (checkBlock.getType() == Material.NETHER_PORTAL) corner1 = checkBlock;
            else {
                int j = 0;
                while (!corner1Found && j < 3) {
                    checkBlock = corner1.getLocation().add(0, -1, 0).getBlock();
                    if (checkBlock.getType() == Material.NETHER_PORTAL) corner1 = checkBlock;
                    else corner1Found = true;
                    j++;
                }
            }
            i++;
        }

        Block corner2 = location.getBlock();
        i = 0;
        boolean corner2Found = false;
        while (!corner2Found && i < 3) {
            Block checkBlock = corner2;
            switch(portalAxis) {
                case X -> checkBlock = corner2.getLocation().add(1, 0, 0).getBlock();
                case Z -> checkBlock = corner2.getLocation().add(0, 0, 1).getBlock();
            }
            if (checkBlock.getType() == Material.NETHER_PORTAL) corner2 = checkBlock;
            else {
                int j = 0;
                while (!corner2Found && j < 3) {
                    checkBlock = corner2.getLocation().add(0, 1, 0).getBlock();
                    if (checkBlock.getType() == Material.NETHER_PORTAL) corner2 = checkBlock;
                    else corner2Found = true;
                    j++;
                }
            }
            i++;
        }

        if (corner1Found && corner2Found) midpoint = findMidpoint(corner1.getLocation(), corner2.getLocation());

        int tickOffset = 0;
        int ticksPerItem = PortalTrading.configManager.getTicksPerItem();
        for (int k = 0; k < stackSize; k++) {
            Location finalMidpoint = midpoint.clone();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (block.getType() != Material.NETHER_PORTAL) return;

                ItemStack outputItem;
                if (oldMaterial == Material.GOLD_INGOT && PortalTrading.configManager.hasBarterTradesEnabled()) {
                    outputItem = BarterLootGen.generate();
                } else {
                    outputItem = PortalTrading.configManager.getTradeOutput(inputItem);
                }

                if (outputItem == null) return;
                Item outputEntity = world.dropItem(finalMidpoint, outputItem);
                outputEntity.setPortalCooldown(Integer.MAX_VALUE);
                outputEntity.setVelocity(outputVelocity.clone());
                outputEntity.setMetadata("PortalTrading", new FixedMetadataValue(plugin, "output-item"));
                world.playSound(location, Sound.BLOCK_END_PORTAL_FRAME_FILL, 0.6f, 2f);
            }, tickOffset);
            tickOffset += ticksPerItem;
        }
    }

    public Location findMidpoint(Location location1, Location location2) {
        double midpointX = (location1.getX() + location2.getX()) / 2;
        double midpointY = (location1.getY() + location2.getY()) / 2;
        double midpointZ = (location1.getZ() + location2.getZ()) / 2;

        return new Location(location1.getWorld(), midpointX, midpointY, midpointZ).add(0.5, 0.5, 0.5);
    }
}
