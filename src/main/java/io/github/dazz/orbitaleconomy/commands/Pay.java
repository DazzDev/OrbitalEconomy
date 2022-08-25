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
import org.jetbrains.annotations.NotNull;

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            utils.printInvalidSenderMessage();
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(utils.getCfgValue("PayPermission", "orbitaleco.pay"))) {
            utils.sendNoPermissionMsg(player);
            return true;
        }
        if (args.length != 2) {
            player.sendMessage(utils.getCfgValue("PayCorrectUsageMessage", "&cCorrect usage: /pay (user) (amount)."));
            return true;
        }
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName() == null || !Objects.equals(offlinePlayer.getName(), args[0]) || offlinePlayer.equals(player)) {
                continue;
            }
            if (NumberUtils.isCreatable(args[1])) {
                float amount = Float.parseFloat(args[1]);
                if (amount < Float.MAX_VALUE && new BigDecimal(Float.toString(amount)).scale() < 3) {
                    if (database.getBalance(player) >= amount && amount > 0) {
                        database.updateBalance(offlinePlayer, database.getBalance(offlinePlayer) + amount);
                        database.updateBalance(player, database.getBalance(player) - amount);
                        String payMessage = utils.getCfgValue("PayMessage", "&aYou paid %amount% dollar(s) to %target%.");
                        payMessage = payMessage.replace("%amount%", String.format("%.2f", amount));
                        payMessage = payMessage.replace("%target%", offlinePlayer.getName());
                        player.sendMessage(payMessage);
                        if (offlinePlayer.getPlayer() != null) {
                            String receiveMessage = utils.getCfgValue("ReceiveMessage", "&a%sender% has paid you %amount% dollar(s).");
                            receiveMessage = receiveMessage.replace("%amount%", String.format("%.2f", amount));
                            receiveMessage = receiveMessage.replace("%sender%", player.getName());
                            offlinePlayer.getPlayer().sendMessage(receiveMessage);
                        }
                        return true;
                    }
                }
            }
            player.sendMessage(utils.getCfgValue("InvalidNumberMessage", "&cYou don't have that many dollars or the number you input is invalid."));
            return true;
        }
        player.sendMessage(utils.getCfgValue("InvalidPlayerMessage", "&cYou can't pay dollars to that player."));
        return true;
    }
}