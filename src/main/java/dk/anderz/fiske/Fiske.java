package dk.anderz.fiske;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.anderz.fiske.commands.subs.ReloadSub;
import dk.anderz.fiske.configuration.Guis;
import dk.anderz.fiske.configuration.Messages;
import dk.anderz.fiske.listeners.FishingEvent;
import dk.anderz.fiske.listeners.NpcClick;
import dk.anderz.fiske.utils.ColorUtils;
import dk.anderz.fiske.utils.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Set;


public final class Fiske extends JavaPlugin {
    public static Fiske instance;
    public static Economy econ = null;
    private static ConsoleCommandSender log;
    public static Config config, messages, gui, rewards;
    public static FileConfiguration configYML, messagesYML, guiYML, rewardsYML;
    public static WorldGuardPlugin worldGuardPlugin;

    @Override
    public void onEnable() {
        instance = this;
        log = getServer().getConsoleSender();
        long timestampBeforeLoad = System.currentTimeMillis();
        log.sendMessage(ColorUtils.getColored("&8&m---------------------------------&r", "", "  &2Enabling &a"+ instance.getName() + " &fv" + getDescription().getVersion()));

        //Configs
        log.sendMessage(ColorUtils.getColored("", "  &2Loading Configs"));
        reload();

        //Vault Hook
        setupEconomy();
        log.sendMessage(ColorUtils.getColored("", "  &2Loading Vault Hook"));

        //WorldGuard
        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            getWorldGuard();
        }
        log.sendMessage(ColorUtils.getColored("", "  &2Loading WorldGuard Hook"));
        //Managers
        log.sendMessage(ColorUtils.getColored("", "  &2Loading Managers"));
        //Commands
        getCommand("fiske").setExecutor(new ReloadSub());
        log.sendMessage(ColorUtils.getColored("", "  &2Hooking into Commands"));

        //Listeners
        getServer().getPluginManager().registerEvents(new FishingEvent(), this);
        getServer().getPluginManager().registerEvents(new NpcClick(), this);

        log.sendMessage(ColorUtils.getColored("", "  &2Hooking into Listeners"));

        log.sendMessage(ColorUtils.getColored("", "  &f" + instance.getName() + " has been enabled!", "    &aVersion: &f" +
                        getDescription().getVersion(), "    &aAuthors: &f" +
                        getDescription().getAuthors(), "",
                "  &2Took &a" + ( System.currentTimeMillis() - timestampBeforeLoad) + " millis &2to load!", "", "&8&m---------------------------------&r"));
    }

    @Override
    public void onDisable() {
        long timestampBeforeLoad = System.currentTimeMillis();

        log.sendMessage(ColorUtils.getColored("&8&m---------------------------------&r", "", " " + instance.getName()  + " Disabled!", "    &cVersion: &f" + getDescription().getVersion(), "    &cAuthors: &f" + getDescription().getAuthors(), "  &2Took &a" + ( System.currentTimeMillis() - timestampBeforeLoad) + " millis &2to save!", "", "&8&m---------------------------------&r"));
    }

    private void initialiseConfigs() {
        saveDefaultConfig();
        if (!(new File(getDataFolder(), "config.yml")).exists())saveResource("config.yml", false);
        config = new Config(this, null, "config.yml");
        configYML = config.getConfig();

        config.checkNonNullValues(configYML);

        if (!(new File(getDataFolder(), "messages.yml")).exists())saveResource("messages.yml", false);
        messages = new Config(this, null, "messages.yml");
        messagesYML = messages.getConfig();

        messages.checkNonNullValues(messagesYML);


        if (!(new File(getDataFolder(), "gui.yml")).exists())saveResource("gui.yml", false);
        gui = new Config(this, null, "gui.yml");
        guiYML = gui.getConfig();

        gui.checkNonNullValues(guiYML);

        if (!(new File(getDataFolder(), "rewards.yml")).exists())saveResource("rewards.yml", false);
        rewards = new Config(this, null, "rewards.yml");
        rewardsYML = rewards.getConfig();

        rewards.checkNonNullValues(rewardsYML);

    }

    public void reload() {
        initialiseConfigs();
        Messages.loadALl();
        Guis.loadALl();
        log.sendMessage(ColorUtils.getColored("   &a - LOADED ALL CONFIGS"));
    }

    private WorldGuardPlugin getWorldGuard() {
        worldGuardPlugin = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        if(worldGuardPlugin == null ){
            return null;
        }
        return worldGuardPlugin;
    }

    public static Set<ProtectedRegion> getRegionAtLocation(Location location) {
        if (worldGuardPlugin == null) {
            System.out.println("WorldGuard isn't enabled.");
            return null;
        }
        return worldGuardPlugin.getRegionManager(location.getWorld()).getApplicableRegions(location).getRegions();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
