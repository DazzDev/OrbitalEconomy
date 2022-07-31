package io.github.dazz.orbitaleconomy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

    private final OrbitalEconomy plugin;

    public Utils(OrbitalEconomy plugin) {
        this.plugin = plugin;
    }

    public String getCfgValue(String key, String def) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(key, def));
    }

    public void printInvalidSenderMessage() {
        plugin.getLogger().info(getCfgValue("InvalidSenderMessage", "This command is for players only."));
    }

    public void sendNoPermissionMsg(Player p) {
        p.sendMessage(getCfgValue("NoPermissionMessage", "&cYou don't have permission to do that."));
    }
}