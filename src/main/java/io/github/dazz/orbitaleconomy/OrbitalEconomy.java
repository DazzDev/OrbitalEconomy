package io.github.dazz.orbitaleconomy;

import io.github.dazz.orbitaleconomy.commands.Balance;
import io.github.dazz.orbitaleconomy.commands.Earn;
import io.github.dazz.orbitaleconomy.commands.Pay;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class OrbitalEconomy extends JavaPlugin {

    private Database database;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        Utils utils = new Utils(this.getConfig(), this.getLogger());
        database = new Database(this, new File(getDataFolder(), "database.db"));

        Bukkit.getPluginManager().registerEvents(new JoinEvent(database), this);
        getCommand("bal").setExecutor(new Balance(database, utils));
        getCommand("earn").setExecutor(new Earn(database, utils));
        getCommand("pay").setExecutor(new Pay(database, utils));

        getLogger().info("OrbitalEconomy enabled!");
    }

    @Override
    public void onDisable() {
        try {
            if (database.conn != null) database.conn.close();
        } catch (SQLException e) {
            getLogger().info(e.getMessage());
        }
        getLogger().info("OrbitalEconomy disabled!");
    }
}