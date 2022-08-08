package io.github.dazz.orbitaleconomy.commands;

import io.github.dazz.orbitaleconomy.Database;
import io.github.dazz.orbitaleconomy.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class Earn implements CommandExecutor {

    private final Database database;
    private final Utils utils;

    public Earn(Database database, Utils utils) {
        this.database = database;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            utils.printInvalidSenderMessage();
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission(utils.getCfgValue("EarnPermission", "orbitaleco.earn"))) {
            utils.sendNoPermissionMsg(p);
            return true;
        }
        int amount = ThreadLocalRandom.current().nextInt(1, 11);
        database.updateBalance(p, database.getBalance(p) + amount);
        p.sendMessage(utils.getCfgValue("EarnMessage", "&aYou just earned %amount% dollar(s).").replace("%amount%", amount + ".00"));
        return true;
    }
}