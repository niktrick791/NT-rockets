package de.niktr.rockets;

import de.niktr.rockets.commands.RocketCommand;
import de.niktr.rockets.listeners.RocketListener;
import de.niktr.rockets.managers.RocketManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NiktrickRocketsPlugin extends JavaPlugin {

    private static NiktrickRocketsPlugin instance;
    private RocketManager rocketManager;

    public static NiktrickRocketsPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.rocketManager = new RocketManager(this);
        this.rocketManager.registerRecipes();

        getServer().getPluginManager().registerEvents(new RocketListener(this), this);

        RocketCommand cmd = new RocketCommand(this);
        var command = getCommand("rocket");
        if (command != null) {
            command.setExecutor(cmd);
            command.setTabCompleter(cmd);
        }

        getLogger().info("niktrick-rockets v" + getPluginMeta().getVersion() + " successfully enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("niktrick-rockets disabled.");
    }

    public RocketManager getRocketManager() {
        return rocketManager;
    }
}
