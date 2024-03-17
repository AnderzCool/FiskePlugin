package dk.anderz.fiske.listeners;

import dk.anderz.fiske.Fiske;
import dk.anderz.fiske.guis.Fish.FishByenMenu;
import dk.anderz.fiske.guis.Fish.FishMenu;
import dk.anderz.fiske.guis.Flower.FlowerMenu;
import lol.pyr.znpcsplus.api.event.NpcInteractEvent;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class NpcClick implements Listener {
    @EventHandler
    public void NPCRightClickEvent(NpcInteractEvent e) {
        if (e.getClickType() == InteractionType.RIGHT_CLICK) {
            String id = e.getEntry().getId();
            if (Objects.equals(id, Fiske.configYML.getString("NPC.FishPrizeMenu"))) {
                FishMenu.mainMenu(e.getPlayer());
            } else if (Objects.equals(id, Fiske.configYML.getString("NPC.FishPrizeMenuByen"))) {
                FishByenMenu.mainMenu(e.getPlayer());
            } else if (Objects.equals(id, Fiske.configYML.getString("NPC.BlomsterMenu")) || Objects.equals(id, Fiske.configYML.getString("NPC.BlomsterByenMenu"))) {
                FlowerMenu.flowerMenu(e.getPlayer());
            }
        }
    }
}
