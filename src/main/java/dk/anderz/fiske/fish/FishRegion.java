package dk.anderz.fiske.fish;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.anderz.fiske.Fiske;
import org.bukkit.entity.Player;

import java.util.Set;

public class FishRegion {
    public static boolean isPlayerInValidRegion(Player player) {
        Set<ProtectedRegion> regions = Fiske.getRegionAtLocation(player.getLocation());
        if (regions == null) {
            return false;
        }
        String fiskeRegion = Fiske.configYML.getString("fiske-region.normal");
        for (ProtectedRegion region : regions) {
            if (region.getId().contains(fiskeRegion)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isPlayerInByenRegion(Player player) {
        Set<ProtectedRegion> regions = Fiske.getRegionAtLocation(player.getLocation());
        if (regions == null) {
            return false;
        }
        String fiskeRegion = Fiske.configYML.getString("fiske-region.by");
        for (ProtectedRegion region : regions) {
            if (region.getId().contains(fiskeRegion)) {
                return true;
            }
        }
        return false;
    }
}
