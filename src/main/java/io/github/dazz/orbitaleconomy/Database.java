package io.github.dazz.orbitaleconomy;

import org.bukkit.OfflinePlayer;

import java.io.File;
import java.sql.*;

public class Database {

    private final OrbitalEconomy plugin;

    public Database(OrbitalEconomy plugin) {
        this.plugin = plugin;
    }

    private File dbFile = null;
    public Connection conn = null;

    public void setupSQL() {
        dbFile = new File(plugin.getDataFolder(), "database.db");
        dbFile.getParentFile().mkdir();
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getPath());
            Statement statement = conn.createStatement();
            statement.executeUpdate("create table if not exists economy (uuid string primary key, balance float)");
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