package io.github.dazz.orbitaleconomy.commands;

import io.github.dazz.orbitaleconomy.Database;
import io.github.dazz.orbitaleconomy.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class Earn implements CommandExecutor {

    private final Database database;
    private final Utils utils;

    public Earn(Database database, Utils utils) {
        this.database = database;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            utils.printInvalidSenderMessage();
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(utils.getCfgValue("EarnPermission", "orbitaleco.earn"))) {
            utils.sendNoPermissionMsg(player);
            return true;
        }
        int amount = ThreadLocalRandom.current().nextInt(1, 11);
        database.updateBalance(player, database.getBalance(player) + amount);
        String message = utils.getCfgValue("EarnMessage", "&aYou just earned %amount% dollar(s).");
        message = message.replace("%amount%", amount + ".00");
        player.sendMessage(message);
        return true;
    }
}