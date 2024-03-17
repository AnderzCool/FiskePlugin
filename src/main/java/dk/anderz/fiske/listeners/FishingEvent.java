package dk.anderz.fiske.listeners;

import dk.anderz.fiske.manager.FishingEventManager;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import static dk.anderz.fiske.fish.FishRegion.isPlayerInValidRegion;

public class FishingEvent implements Listener {

    private final FishingEventManager fishingEventManager = new FishingEventManager();

    @EventHandler
    public void onFishEvent(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            if (!isPlayerInValidRegion(player)) {
                event.setCancelled(true);
                return;
            }
            Item caught = (Item) event.getCaught();
            fishingEventManager.handleFishingRewards(player, caught);
        }
    }
}


