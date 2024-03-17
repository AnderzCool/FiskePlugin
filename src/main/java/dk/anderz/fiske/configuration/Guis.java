package dk.anderz.fiske.configuration;

import dk.anderz.fiske.Fiske;
import dk.anderz.fiske.utils.ColorUtils;
import dk.anderz.fiske.utils.SkullBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Guis {
    private static Map<String, String[]> messages;

    public static void loadALl() {
        messages = new HashMap<>();
        for (String path : Fiske.guiYML.getKeys(true)) {
            if (!Fiske.guiYML.isConfigurationSection(path)) {
                if (Fiske.guiYML.getStringList(path) != null && Fiske.guiYML.isList(path)) {
                    List<String> stringList = ColorUtils.getColored(Fiske.guiYML.getStringList(path));
                    messages.put(path, stringList.toArray(new String[0]));
                    continue;
                }
                if (Fiske.guiYML.getString(path) != null) {
                    List<String> stringList = Collections.singletonList(ColorUtils.getColored(Fiske.guiYML.getString(path)));
                    messages.put(path, stringList.toArray(new String[0]));
                }
            }
        }
    }

    public static ItemStack getItemHead(String path) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullBuilder.itemWithBase64(item, get(path)[0]);
        return item;
    }

    public static String[] get(String path) {
        return messages.getOrDefault(path, new String[]{});
    }

    public static String[] getColored(String path) {
        return ColorUtils.getColored(get(path));
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

    public static String @NotNull [] get2(String path, String... replacements) {
        if (messages.containsKey(path)) {
            String[] messages = get(path);
            List<String> messageList = new ArrayList<>();
            for (String message : messages) {
                // Use replaceLore method instead of the inner loop
                String[] replacedLines = replaceLore(new String[]{message}, replacements);
                messageList.addAll(Arrays.asList(replacedLines));
            }
            return messageList.toArray(new String[0]);
        }
        return new String[]{};
    }

    public static String[] replaceLore(String[] lore, String... replacements) {
        List<String> newLore = new ArrayList<>(lore.length);
        for (String line : lore) {
            for (int i = 0; i < replacements.length; i += 2) {
                line = StringUtils.replace(line, replacements[i], replacements[i + 1]);
            }
            newLore.add(line);
        }
        return newLore.toArray(new String[0]);
    }

    public static String[] getColored(String path, String... replacements) {
        return ColorUtils.getColored(get(path, replacements));
    }
}