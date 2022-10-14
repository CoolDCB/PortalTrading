package me.dave.portaltrading.barterloot;

import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.concurrent.Callable;

public class BarterLootReward implements Callable<ItemStack> {
    Callable<ItemStack> reward;
    int minAmount;
    int maxAmount;

    public BarterLootReward(Callable<ItemStack> reward, int minAmount, int maxAmount) {
        this.reward = reward;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public ItemStack call() throws Exception {
        ItemStack itemStack = reward.call().clone();
        int randomNum = new Random().nextInt((maxAmount - minAmount) + 1) + minAmount;
        itemStack.setAmount(randomNum);
        return itemStack;
    }
}
