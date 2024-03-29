package me.dave.portaltrading.barterloot;

import me.dave.portaltrading.utilities.RandomCollection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Random;

public class BarterLootGen {
    private static final RandomCollection<Object> BARTER_LOOT = new RandomCollection<>()
            .add(new BarterLootReward(() -> {
                ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                int randomNum = new Random().nextInt((3 - 1) + 1) + 1;
                itemMeta.addStoredEnchant(Enchantment.SOUL_SPEED, randomNum, true);
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }, 1, 1), 5)
            .add(new BarterLootReward(() -> {
                int randomNum = new Random().nextInt((3 - 1) + 1) + 1;
                ItemStack itemStack = new ItemStack(Material.IRON_BOOTS);
                itemStack.addEnchantment(Enchantment.SOUL_SPEED, randomNum);
                return itemStack;
            }, 1, 1), 8)
            .add(new BarterLootReward(() -> {
                ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
                PotionMeta itemMeta = (PotionMeta) itemStack.getItemMeta();
                itemMeta.setBasePotionData(new PotionData(PotionType.FIRE_RESISTANCE, false, false));
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }, 1, 1), 8)
            .add(new BarterLootReward(() -> {
                ItemStack itemStack = new ItemStack(Material.POTION);
                PotionMeta itemMeta = (PotionMeta) itemStack.getItemMeta();
                itemMeta.setBasePotionData(new PotionData(PotionType.FIRE_RESISTANCE, false, false));
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }, 1, 1), 10)
            .add(new BarterLootReward(() -> {
                ItemStack itemStack = new ItemStack(Material.POTION);
                PotionMeta itemMeta = (PotionMeta) itemStack.getItemMeta();
                itemMeta.setBasePotionData(new PotionData(PotionType.WATER, false, false));
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }, 1, 1), 10)
            .add(new BarterLootReward(() -> new ItemStack(Material.IRON_NUGGET), 10,36), 10)
            .add(new BarterLootReward(() -> new ItemStack(Material.ENDER_PEARL), 2,4), 10)
            .add(new BarterLootReward(() -> new ItemStack(Material.STRING), 3,9), 20)
            .add(new BarterLootReward(() -> new ItemStack(Material.QUARTZ), 5,12), 20)
            .add(new BarterLootReward(() -> new ItemStack(Material.OBSIDIAN), 1,1), 40)
            .add(new BarterLootReward(() -> new ItemStack(Material.CRYING_OBSIDIAN), 1,3), 40)
            .add(new BarterLootReward(() -> new ItemStack(Material.FIRE_CHARGE), 1,1), 40)
            .add(new BarterLootReward(() -> new ItemStack(Material.LEATHER), 2,4), 40)
            .add(new BarterLootReward(() -> new ItemStack(Material.SOUL_SAND), 2,8), 40)
            .add(new BarterLootReward(() -> new ItemStack(Material.NETHER_BRICK), 2,8), 40)
            .add(new BarterLootReward(() -> new ItemStack(Material.SPECTRAL_ARROW), 6,12), 40)
            .add(new BarterLootReward(() -> new ItemStack(Material.GRAVEL), 8,16), 40)
            .add(new BarterLootReward(() -> new ItemStack(Material.BLACKSTONE), 8,16), 40);

    public static ItemStack generate() {
        BarterLootReward reward = (BarterLootReward) BARTER_LOOT.next();
        ItemStack itemStack;
        try {
            itemStack = reward.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return itemStack;
    }
}
