package dk.anderz.fiske.manager.GuiManager;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dk.anderz.fiske.Fiske;
import dk.anderz.fiske.configuration.Guis;
import dk.anderz.fiske.enums.RewardByenType;
import dk.anderz.fiske.guis.Fish.FishByenMenu;
import dk.anderz.fiske.guis.Fish.FishMenu;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RewardByenMenu {
    public static void openRewardMenu(Player player, RewardByenType rewardType) {
        Gui gui = Gui.gui()
                .title(Component.text(Guis.getColored("fiske.prefix")[0] + StringUtils.capitalize(rewardType.getKey())))
                .rows(5)
                .disableAllInteractions()
                .create();

        int in = 9;

        List<Map<?, ?>> items = Fiske.rewards.getConfig().getMapList("rewards." + rewardType.getKey());
        for (Map<?,?> item : items) {

            ItemStack itemStack = null;

            int amount = 1;
            if (item.containsKey("amount")) {
                amount = (Integer) item.get("amount");
            }
            if (item.containsValue("CUSTOM_SKULL")) {
                String id = item.get("hdb-id").toString();
                HeadDatabaseAPI api = new HeadDatabaseAPI();
                itemStack = api.getItemHead(id);
                String itemName = null;
                if (item.containsKey("name")) {
                    itemName = (String) item.get("name");
                }
                if (itemName != null) {
                    ItemMeta meta = itemStack.getItemMeta(); // Moved this line inside the condition
                    meta.setDisplayName(itemName); // Set display name only for custom skulls
                    itemStack.setItemMeta(meta);
                }
            } else {
                Material itemType = Material.getMaterial((String) item.get("item-type"));
                short itemData;
                if (item.containsKey("data")) {
                    itemData = ((Integer) item.get("data")).shortValue();
                } else {
                    itemData = 0;
                }
                itemStack = new ItemStack(itemType, amount, itemData);
            }
            if (item.containsKey("enchants")) {
                Map<?, ?> enchants = (Map<?, ?>) item.get("enchants");
                for (Map.Entry<?, ?> entry : enchants.entrySet()) {
                    String[] enchant_split = entry.getValue().toString().split(" ");
                    String enchantName = enchant_split[0];
                    int enchantLevel = Integer.parseInt(enchant_split[1]);
                    Enchantment enchant = Enchantment.getByName(enchantName);
                    itemStack.addUnsafeEnchantment(enchant, enchantLevel);
                }
            }
            ItemMeta meta = itemStack.getItemMeta();
            if (item.containsKey("lore")) {
                List<?> loreList = (List<?>) item.get("lore");
                List<String> lore = new ArrayList<>();
                for (Object obj : loreList) {
                    lore.add(obj.toString());
                }
                meta.setLore(lore);
            }
            itemStack.setItemMeta(meta);
            gui.setItem(in, ItemBuilder.from(new ItemStack(itemStack)).asGuiItem());
            in++;
        }
        gui.getFiller().fillTop(ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3)).name(Component.text("§f")).asGuiItem());
        gui.getFiller().fillBottom(ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11)).name(Component.text("§f")).asGuiItem());

        ItemStack back = new ItemStack(Material.INK_SACK,1, (short) 1);
        gui.setItem(36, ItemBuilder.from(back).name(Component.text(Guis.getColored("fiske.tilbage-knap")[0])).asGuiItem(event -> {
            FishByenMenu.mainMenu(player);
            player.playSound(player.getLocation(), Sound.CLICK, 1, 10);
        }));
        gui.open(player);
    }
}
