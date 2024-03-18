package dk.anderz.fiske.fish;

import dk.anderz.fiske.configuration.Messages;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static dk.anderz.fiske.fish.FishItemStack.createItemStack;

public class FishReward {
    public static void giveReward(Player player, Map<?, ?> reward) {
        ItemStack itemStack = createItemStack(reward);
        player.getInventory().addItem(itemStack);
        if (reward.containsValue("CUSTOM_SKULL")) {
            Messages.send(player, "fiske.rewards.epic");
        }
        if (reward.containsValue("RED_ROSE")) {
            Messages.send(player, "fiske.rewards.legendary");
        }
    }
}
