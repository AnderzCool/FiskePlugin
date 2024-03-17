package dk.anderz.fiske.fish;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static dk.anderz.fiske.fish.FishItemStack.createItemStack;

public class FishReward {
    public static void giveReward(Player player, Map<?, ?> reward) {
        ItemStack itemStack = createItemStack(reward);
        player.getInventory().addItem(itemStack);
        if (reward.containsValue("CUSTOM_SKULL")) {
            player.sendMessage("§8§l[ §b§lFISKE §f§lSYSTEM §8§l] \n §7Du fik et §5§lEPIC LOOT§7.");
        }
        if (reward.containsValue("RED_ROSE")) {
            player.sendMessage("§8§l[ §b§lFISKE §f§lSYSTEM §8§l] \n §7Du fik et §6§lLEGENDARY LOOT§7.");
        }
    }
}
