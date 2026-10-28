package de.niktr.rockets.listeners;

import de.niktr.rockets.NiktrickRocketsPlugin;
import de.niktr.rockets.util.RocketItemFactory;
import de.niktr.rockets.util.XRocketVersion;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RocketListener implements Listener {

    private final NiktrickRocketsPlugin plugin;

    public RocketListener(NiktrickRocketsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack hand = XRocketVersion.getItemInHand(player);
        int tier = RocketItemFactory.getRocketTier(hand);

        if (tier >= 1 && tier <= 6) {
            event.setCancelled(true);
            plugin.getRocketManager().applyRocketBoost(player, null, tier);

            if (player.getGameMode() != GameMode.CREATIVE) {
                if (hand.getAmount() > 1) {
                    hand.setAmount(hand.getAmount() - 1);
                    XRocketVersion.setItemInHand(player, hand);
                } else {
                    XRocketVersion.setItemInHand(player, null);
                }
            }
        }
    }
}
