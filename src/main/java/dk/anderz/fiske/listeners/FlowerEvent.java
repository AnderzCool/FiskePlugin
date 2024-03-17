package dk.anderz.fiske.listeners;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.anderz.fiske.Fiske;
import dk.anderz.fiske.utils.ActionBar;
import dk.anderz.fiske.utils.Cooldown;
import dk.anderz.fiske.utils.Econ;
import dk.anderz.fiske.utils.TimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FlowerEvent implements Listener {

    private final HashMap<UUID, Integer> pickedFlowers = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null || !isPlayerInFlowerRegion(player))
            return;

        String prefix = Fiske.configYML.getString("Flower.prefix");
        String toolName = Fiske.configYML.getString("Flower.toolName");
        int flowerChance = Fiske.configYML.getInt("Flower.chance");

        if (!event.hasItem() || !event.getItem().hasItemMeta() || !toolName.equals(event.getItem().getItemMeta().getDisplayName()))
            return;

        Block clickedBlock = event.getClickedBlock();
        UUID playerUUID = player.getUniqueId();

        Location flowerLocation = clickedBlock.getLocation();
        Material clickedMaterial = clickedBlock.getType();

        if (clickedMaterial != Material.RED_ROSE && clickedMaterial != Material.YELLOW_FLOWER)
            return;

        if (pickedFlowers.containsKey(playerUUID)) {
            player.sendMessage(prefix + "\n §7Du kan kun plukke én blomst ad gangen.");
            return;
        }

        if (Cooldown.getCooldown(flowerLocation.toString()) > 0) {
            int cooldownInSeconds = Math.toIntExact(Cooldown.getCooldown(flowerLocation.toString()) / 1000);
            player.sendMessage(prefix + "\n §7Du skal vente §f" + TimeFormatter.secToMin(cooldownInSeconds) + "§7, før du kan plukke denne blomst.");
            return;
        }

        Cooldown.setCooldown(flowerLocation.toString(), 600);
        int checkDistanceId = new BukkitRunnable() {
            final Location loc = player.getLocation();
            int secondsLeft = 10;

            @Override
            public void run() {
                if (!player.isOnline() || player.getLocation().distance(loc) > 0.5) {
                    player.sendMessage(prefix + "\n §7Du fejlede plukningen.");
                    Cooldown.setCooldown(flowerLocation.toString(), 10);
                    cancel();
                    pickedFlowers.remove(playerUUID);
                    return;
                }

                if (secondsLeft <= 0) {
                    Random random = new Random();
                    int randomNumber = random.nextInt(1001);
                    if (randomNumber <= flowerChance) {
                        short[] flowerDataValues = {0, 1, 2, 3, 4, 5, 6, 7, 8};
                        int randomIndex = random.nextInt(flowerDataValues.length);
                        short randomFlowerDataValue = flowerDataValues[randomIndex];
                        ItemStack flower = new ItemStack(Material.RED_ROSE, 1, randomFlowerDataValue);
                        player.getInventory().addItem(flower);

                        Bukkit.getScheduler().runTask(Fiske.instance, () -> {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "logs add item-RED_ROSE:" + randomFlowerDataValue + " 1");
                        });
                        System.out.println("Added flower (RED_ROSE) with id: " + randomFlowerDataValue + " to logs. (/logs add item-RED_ROSE:" + randomFlowerDataValue + " 1)");

                        Bukkit.broadcastMessage(prefix + "\n §7Spilleren §e" + player.getName() + " §7har lige plukket en blomst!");
                    } else {
                        int money = random.nextInt(5000) + 1500;
                        player.sendMessage(prefix + "\n §7Ingen blomst denne gang, dog fik du: \n §8» §a$§f" + money);
                        Econ.addMoney(player, (double) money);
                    }
                    cancel();
                    pickedFlowers.remove(playerUUID);
                    return;
                }

                secondsLeft--;
                ActionBar.sendActionbar(player, "§6§l[!] §7Plukker blomst: §f" + String.valueOf(Math.floor(100.0 - (secondsLeft * 100.0 / 39))).replace(".0", "") + "%");
            }
        }.runTaskTimer(Fiske.instance, 5L, 5L).getTaskId();
        pickedFlowers.put(playerUUID, checkDistanceId);
    }

    private boolean isPlayerInFlowerRegion(Player player) {
        Set<ProtectedRegion> regions = Fiske.getRegionAtLocation(player.getLocation());

        if (regions == null)
            return false;

        List<String> flowerRegions = Fiske.configYML.getStringList("Flower.regions");

        for (ProtectedRegion region : regions) {
            if (flowerRegions.contains(region.getId()))
                return true;
        }

        return false;
    }

}
