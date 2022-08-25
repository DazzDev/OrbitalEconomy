package io.github.dazz.orbitaleconomy;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class Utils {

    private final FileConfiguration config;
    private final Logger logger;

    public Utils(FileConfiguration config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    public String getCfgValue(String key, String def) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(key, def));
    }

    public void printInvalidSenderMessage() {
        logger.info(getCfgValue("InvalidSenderMessage", "This command is for players only."));
    }

    public void sendNoPermissionMsg(Player player) {
        player.sendMessage(getCfgValue("NoPermissionMessage", "&cYou don't have permission to do that."));
    }
}