package dk.anderz.fiske.utils;

import dk.anderz.fiske.Fiske;
import org.bukkit.OfflinePlayer;

public class Econ {

    public static boolean addMoney(OfflinePlayer player, Double amount) {
        return Fiske.econ.depositPlayer(player, amount).transactionSuccess();
    }

    public static boolean removeMoney(String player, double amount) {
        return Fiske.econ.withdrawPlayer(player, amount).transactionSuccess();
    }

    private boolean addMoneyToPlayer(String playerName, double amount) {
        return Fiske.econ.depositPlayer(playerName, amount).transactionSuccess();
    }

    public static double getbalance(String playerName) {
        return Fiske.econ.getBalance(playerName);
    }
}
