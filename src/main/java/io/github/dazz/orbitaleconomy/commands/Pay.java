package io.github.dazz.orbitaleconomy.commands;

import io.github.dazz.orbitaleconomy.Database;
import io.github.dazz.orbitaleconomy.Utils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Objects;

public class Pay implements CommandExecutor {

    private final Database database;
    private final Utils utils;

    public Pay(Database database, Utils utils) {
        this.database = database;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(utils.getCfgValue("PayPermission", "orbitaleco.pay"))) {
                if (args.length == 2) {
                    for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                        if (Objects.equals(offlinePlayer.getName(), args[0]) && offlinePlayer != p) {
                            if (NumberUtils.isCreatable(args[1])) {
                                float amount = Float.parseFloat(args[1]);
                                if (amount < Float.MAX_VALUE && new BigDecimal(Float.toString(amount)).scale() < 3) {
                                    if (database.getBalance(p) >= amount && amount > 0) {
                                        database.updateBalance(offlinePlayer, database.getBalance(offlinePlayer) + amount);
                                        database.updateBalance(p, database.getBalance(p) - amount);
                                        p.sendMessage(utils.getCfgValue("PayMessage", "&aYou paid %amount% dollar(s) to %target%.").replace("%amount%", String.format("%.2f", amount)).replace("%target%", offlinePlayer.getName()));
                                        if (offlinePlayer.isOnline())
                                            offlinePlayer.getPlayer().sendMessage(utils.getCfgValue("ReceiveMessage", "&a%sender% has paid you %amount% dollar(s).").replace("%amount%", String.format("%.2f", amount)).replace("%sender%", p.getName()));
                                        return true;
                                    }
                                }
                            }
                            p.sendMessage(utils.getCfgValue("InvalidNumberMessage", "&cYou don't have that many dollars or the number you input is invalid."));
                            return true;
                        }
                    }
                    p.sendMessage(utils.getCfgValue("InvalidPlayerMessage", "&cYou can't pay dollars to that player."));
                    return true;
                }
                p.sendMessage(utils.getCfgValue("PayCorrectUsageMessage", "&cCorrect usage: /pay (user) (amount)."));
                return true;
            }
            utils.sendNoPermissionMsg(p);
            return true;
        }
        utils.printInvalidSenderMessage();
        return true;
    }
}