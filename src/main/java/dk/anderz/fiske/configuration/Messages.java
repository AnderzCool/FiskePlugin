package dk.anderz.fiske.configuration;

import dk.anderz.fiske.Fiske;
import dk.anderz.fiske.utils.ColorUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Messages {
    private static Map<String, String[]> messages;

    public static void loadALl() {
        messages = new HashMap<>();
        for (String path : Fiske.messagesYML.getKeys(true)) {
            if (!Fiske.messagesYML.isConfigurationSection(path)) {
                if (Fiske.messagesYML.getStringList(path) != null && Fiske.messagesYML.isList(path)) {
                    List<String> stringList = ColorUtils.getColored(Fiske.messagesYML.getStringList(path));
                    messages.put(path, stringList.toArray(new String[0]));
                    continue;
                }
                if (Fiske.messagesYML.getString(path) != null) {
                    List<String> stringList = Collections.singletonList(ColorUtils.getColored(Fiske.messagesYML.getString(path)));
                    messages.put(path, stringList.toArray(new String[0]));
                }
            }
        }
        if (messages.containsKey("prefix")) {
            String prefix = messages.get("prefix")[0];
            for (String[] values : messages.values()) {
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].replace("{prefix}", prefix);
                }
            }
        }
    }

    public static String[] get(String path) {
        return messages.getOrDefault(path, new String[]{});
    }

    public static String[] get(String path, String... replacements) {
        if (messages.containsKey(path)) {
            String[] messages = get(path);
            List<String> messageList = new ArrayList<>();
            for (String message : messages) {
                for (int i = 0; i < replacements.length; i += 2) {
                    message = message.replaceAll(replacements[i], replacements[i + 1]);
                }
                messageList.add(message);
            }
            return messageList.toArray(new String[0]);
        }
        return new String[]{};
    }
    private static void sendMessages(CommandSender player, String path, String... replacements) {
        String[] messages = get(path);
        for (String message : messages) {
            for (int i = 0; i < replacements.length; i += 2) {
                message = message.replaceAll(replacements[i], replacements[i + 1]);
            }
            player.sendMessage(message);
        }
    }

    public static void send(CommandSender player, String path, String... replacements) {
        sendMessages(player, path, replacements);
    }

    public static void send(Player player, String path, String... replacements) {
        sendMessages(player, path, replacements);
    }
}
