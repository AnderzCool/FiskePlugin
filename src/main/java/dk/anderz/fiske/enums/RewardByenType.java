package dk.anderz.fiske.enums;

public enum RewardByenType {
    COMMON("common", "§7§lCOMMON LOOT"),
    UNCOMMON("uncommon", "§a§lUNCOMMON LOOT"),
    RARE("rare", "§9§lRARE LOOT"),
    EPIC("epic", "§5§lEPIC LOOT"),
    LEGENDARY("legendary", "§6§lLEGENDARY LOOT");

    private final String key;
    private final String name;

    RewardByenType(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}

