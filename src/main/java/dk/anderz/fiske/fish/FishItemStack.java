package dk.anderz.fiske.fish;

import dk.anderz.fiske.Fiske;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class FishItemStack {
    public static ItemStack createItemStack(Map<?, ?> reward) {
        ItemStack itemStack;
        String itemType = (String) reward.get("item-type");
        int amount = reward.containsKey("amount") ? (Integer) reward.get("amount") : 1;
        int data = reward.containsKey("data") ? (Integer) reward.get("data") : 0;

        if ("CUSTOM_SKULL".equals(itemType)) {
            String id = (String) reward.get("hdb-id");
            itemStack = new HeadDatabaseAPI().getItemHead(id);
            logItemAddition("head:"+id);
        } else {
            itemStack = new ItemStack(Material.valueOf(itemType), amount);
            itemStack.setDurability((short) data);
            logItemAddition(itemType + ":" + data);
        }

        if (reward.containsKey("enchants")) {
            applyEnchantments(itemStack, (Map<?, ?>) reward.get("enchants"));
        }
        return itemStack;
    }
    private static void logItemAddition(String itemId) {
        Bukkit.getScheduler().runTask(Fiske.instance, () -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "logs add " + itemId + " 1");
            System.out.println("Added " + itemId + " to logs. (/logs add " + itemId + " 1)");
        });
    }
    private static void applyEnchantments(ItemStack itemStack, Map<?, ?> enchantments) {
        for (Map.Entry<?, ?> entry : enchantments.entrySet()) {
            String[] enchantSplit = entry.getValue().toString().split(" ");
            String enchantName = enchantSplit[0];
            int enchantLevel = Integer.parseInt(enchantSplit[1]);
            Enchantment enchant = Enchantment.getByName(enchantName);
            itemStack.addUnsafeEnchantment(enchant, enchantLevel);
        }
    }
}
