package me.dave.portaltrading.listener;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.portaltrading.PortalTrading;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.UUID;

public class PlayerListener implements Listener {
    private final HashSet<UUID> playerCooldowns = new HashSet<>();

    @EventHandler
    public void onPlayerEnterPortal(EntityPortalEnterEvent event) {
        if (event.getEntity() instanceof Player player && !playerCooldowns.contains(player.getUniqueId())) {
            UUID playerUUID = player.getUniqueId();

            playerCooldowns.add(playerUUID);
            Bukkit.getScheduler().runTaskLaterAsynchronously(PortalTrading.getInstance(), () -> playerCooldowns.remove(playerUUID), 30);

            Vector newVelocity = player.getVelocity().multiply(-5);
            double x = newVelocity.getX() >= 0 ? Math.max(newVelocity.getX(), 1) : Math.min(newVelocity.getX(), -1);
            double z = newVelocity.getZ() >= 0 ? Math.max(newVelocity.getZ(), 1) : Math.min(newVelocity.getZ(), -1);

            ChatColorHandler.sendTitle(player, null, "&#eb6e6eThe Portal isn't strong enough!", 0, 100, 10);
            ChatColorHandler.sendMessage(player, "&#eb6e6eThe Portal isn't strong enough! Try throwing items in and see if the piglins will trade");
            player.setVelocity(new Vector(x, 0.3, z));
        }
    }
}
