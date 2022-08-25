package io.github.dazz.orbitaleconomy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.sql.*;

public class Database {

    private final OrbitalEconomy plugin;
    private final File dbFile;

    public Database(OrbitalEconomy plugin, File dbFile) {
        this.plugin = plugin;
        this.dbFile = dbFile;

        if (!setupSQL()) {
            plugin.getLogger().info("OrbitalEconomy was not able to setup the SQLite database, disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    public Connection conn = null;

    public boolean setupSQL() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getPath());
            Statement statement = conn.createStatement();
            statement.executeUpdate("create table if not exists economy (uuid string primary key, balance float)");
        } catch (SQLException e) {
            plugin.getLogger().info(e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                plugin.getLogger().info(e.getMessage());
            }
        }
        return true;
    }

    public void insertBalance(OfflinePlayer p) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getPath());
            PreparedStatement pstmt = conn.prepareStatement("insert into economy values(?,0)");
            pstmt.setString(1, p.getUniqueId().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().info(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                plugin.getLogger().info(e.getMessage());
            }
        }
    }

    public void updateBalance(OfflinePlayer p, float bal) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getPath());
            PreparedStatement pstmt = conn.prepareStatement("update economy set balance=round(?,2) where uuid=?");
            pstmt.setFloat(1, bal);
            pstmt.setString(2, p.getUniqueId().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().info(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                plugin.getLogger().info(e.getMessage());
            }
        }
    }

    public float getBalance(OfflinePlayer p) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getPath());
            PreparedStatement pstmt = conn.prepareStatement("select balance from economy where uuid=?");
            pstmt.setString(1, p.getUniqueId().toString());
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat("balance");
        } catch (SQLException e) {
            plugin.getLogger().info(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                plugin.getLogger().info(e.getMessage());
            }
        }
        return -1;
    }
}