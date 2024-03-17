package dk.anderz.fiske.manager;

import dk.anderz.fiske.Fiske;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static dk.anderz.fiske.fish.FishRegion.isPlayerInByenRegion;
import static dk.anderz.fiske.fish.FishRegion.isPlayerInValidRegion;
import static dk.anderz.fiske.fish.FishReward.giveReward;

public class FishingEventManager {

    private final Random random = new Random();

    public void handleFishingRewards(Player player, Item caught) {
        List<Map<?, ?>> rewards = getRandomRewardList(player);
        int randomReward = random.nextInt(rewards.size());
        Map<?,?> reward = rewards.get(randomReward);
        giveReward(player, reward);
        caught.remove();
    }

    private List<Map<?, ?>> getRandomRewardList(Player player) {
        double randomChance = random.nextDouble() * 100;
        if (isPlayerInValidRegion(player)) {
            Bukkit.broadcastMessage("§b"+randomChance);
            if (randomChance <= Fiske.configYML.getDouble("Fish.chances.normal.legendary")) {
                return Fiske.rewards.getConfig().getMapList("rewards.legendary");
            }
            if (randomChance <= Fiske.configYML.getDouble("Fish.chances.normal.epic")) {
                return Fiske.rewards.getConfig().getMapList("rewards.epic");
            }
            if (randomChance <= Fiske.configYML.getDouble("Fish.chances.normal.rare")) {
                return Fiske.rewards.getConfig().getMapList("rewards.rare");
            }
            if (randomChance <= Fiske.configYML.getDouble("Fish.chances.normal.uncommon")) {
                return Fiske.rewards.getConfig().getMapList("rewards.uncommon");
            }
            return Fiske.rewards.getConfig().getMapList("rewards.common"); // 63.38% (remaining)
        }
        if (isPlayerInByenRegion(player)) {
            if (randomChance <= Fiske.configYML.getDouble("Fish.chances.by.legendary")) {
                return Fiske.rewards.getConfig().getMapList("rewards.byen.legendary");
            }
            if (randomChance <= Fiske.configYML.getDouble("Fish.chances.by.epic")) {
                return Fiske.rewards.getConfig().getMapList("rewards.byen.epic");
            }
            if (randomChance <= Fiske.configYML.getDouble("Fish.chances.by.rare")) {
                return Fiske.rewards.getConfig().getMapList("rewards.byen.rare");
            }
            if (randomChance <= Fiske.configYML.getDouble("Fish.chances.by.uncommon")) {
                return Fiske.rewards.getConfig().getMapList("rewards.byen.uncommon");
            }
            return Fiske.rewards.getConfig().getMapList("rewards.byen.common"); // 53.3% (remaining)
        }
        return Fiske.rewards.getConfig().getMapList("rewards.common");
    }

}
