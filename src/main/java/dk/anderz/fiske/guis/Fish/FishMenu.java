package dk.anderz.fiske.guis.Fish;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dk.anderz.fiske.Fiske;
import dk.anderz.fiske.configuration.Guis;
import dk.anderz.fiske.enums.RewardType;
import dk.anderz.fiske.manager.GuiManager.RewardMenu;
import dk.anderz.fiske.utils.SkullBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Locale;

public class FishMenu {
    public static void mainMenu(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text(Guis.getColored("fiske.mainMenu.name")[0]))
                .rows(5)
                .disableAllInteractions()
                .create();
        int in = 20;
        gui.getFiller().fillTop(ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3)).setName("§f").asGuiItem());
        gui.getFiller().fillBottom(ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11)).setName("§f").asGuiItem());
        ItemStack back = new ItemStack(Material.INK_SACK,1, (short) 1);
        gui.setItem(36, ItemBuilder.from(back).name(Component.text(Guis.getColored("fiske.luk-knap")[0])).asGuiItem(event -> {
            event.getWhoClicked().closeInventory();
            player.playSound(player.getLocation(), Sound.CLICK, 1, 10);
        }));
        for (RewardType rewardType : RewardType.values()) {
            String chance = (NumberFormat.getInstance(Locale.GERMAN).format(Fiske.configYML.getDouble("Fish.chances.normal." + rewardType.getKey())));
            gui.setItem(in, ItemBuilder.from(SkullBuilder.getItemHead("mainMenu." + rewardType.getKey()))
                    .setName(rewardType.getName())
                    .setLore(Guis.getColored("fiske.mainMenu.lore", "\\{rewardType\\}", rewardType.getName(), "\\{chance\\}", chance))
                    .asGuiItem(event -> {
                        player.playSound(player.getLocation(), Sound.CLICK, 1, 10);
                        RewardMenu.openRewardMenu(player, rewardType);
                    }));
            in++;
        }
        player.playSound(player.getLocation(), Sound.CLICK, 1, 10);
        gui.open(player);
    }
}

