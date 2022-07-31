package io.github.dazz.orbitaleconomy.commands;

import io.github.dazz.orbitaleconomy.Database;
import io.github.dazz.orbitaleconomy.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Balance implements CommandExecutor {

    private final Database database;
    private final Utils utils;

    public Balance(Database database, Utils utils) {
        this.database = database;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(utils.getCfgValue("BalancePermission", "orbitaleco.balance"))) {
                p.sendMessage(utils.getCfgValue("BalanceMessage", "&4Your balance is %bal% dollar(s).").replace("%bal%", String.valueOf(database.getBalance(p))));
                return true;
            }
            utils.sendNoPermissionMsg(p);
            return true;
        }
        utils.printInvalidSenderMessage();
        return true;
    }
}