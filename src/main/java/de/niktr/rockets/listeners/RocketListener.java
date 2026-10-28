package de.niktr.rockets.listeners;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import de.niktr.rockets.NiktrickRocketsPlugin;
import de.niktr.rockets.util.RocketItemFactory;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class RocketListener implements Listener {

    private final NiktrickRocketsPlugin plugin;

    public RocketListener(NiktrickRocketsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onElytraBoost(PlayerElytraBoostEvent event) {
        ItemStack item = event.getItemStack();
        int tier = RocketItemFactory.getRocketTier(item);
        if (tier >= 1 && tier <= 6) {
            Player player = event.getPlayer();
            Firework firework = event.getFirework();
            plugin.getRocketManager().applyRocketBoost(player, firework, tier);
        }
    }
}
