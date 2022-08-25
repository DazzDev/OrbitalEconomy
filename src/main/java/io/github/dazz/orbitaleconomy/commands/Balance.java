package io.github.dazz.orbitaleconomy.commands;

import io.github.dazz.orbitaleconomy.Database;
import io.github.dazz.orbitaleconomy.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Balance implements CommandExecutor {

    private final Database database;
    private final Utils utils;

    public Balance(Database database, Utils utils) {
        this.database = database;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            utils.printInvalidSenderMessage();
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission(utils.getCfgValue("BalancePermission", "orbitaleco.balance"))) {
            utils.sendNoPermissionMsg(p);
            return true;
        }
        String message = utils.getCfgValue("BalanceMessage", "&4Your balance is %bal% dollar(s).");
        message = message.replace("%bal%", String.valueOf(database.getBalance(p)));
        p.sendMessage(message);
        return true;
    }
}