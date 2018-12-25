
package it.feargames.networkcoins.command;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.Property;
import it.feargames.networkcoins.CoinManager;
import it.feargames.networkcoins.ConfigValues;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Function;

public class NetworkCoinsCommand extends AbstractCommandExecutor {
    private final SettingsManager configuration;
    private final CoinManager coinManager;

    public NetworkCoinsCommand(@NonNull SettingsManager configuration, @NonNull CoinManager coinManager) {
        super(configuration.getProperty(ConfigValues.COMMAND_NAME), null, "The plugin root command",
                null, "networkcoins.admin", null);
        this.configuration = configuration;
        this.coinManager = coinManager;

        // Lazy values
        this.usage = formatMessage(ConfigValues.LOCALE_COMMAND_USAGE, message -> message);
        this.usage = formatMessage(ConfigValues.LOCALE_COMMAND_DESCRIPTION, message -> message);
        this.permissionMessage = formatMessage(ConfigValues.LOCALE_NO_PERMISSION, message -> message);
    }

    private String formatMessage(Property<String> key, Function<String, String> handler) {
        return ChatColor.translateAlternateColorCodes('&', handler.apply(configuration.getProperty(key)
                .replace("%prefix", configuration.getProperty(ConfigValues.LOCALE_PREFIX))
                .replace("%currency", configuration.getProperty(ConfigValues.LOCALE_CURRENCY))));
    }

    private void sendMessage(CommandSender target, Property<String> key, Function<String, String> handler) {
        target.sendMessage(formatMessage(key, handler));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 1) {
            if (sender instanceof Player) {
                sendMessage(sender, ConfigValues.LOCALE_GET_COINS, message ->
                        message.replace("%count", String.valueOf(coinManager.getBalance(((Player) sender).getUniqueId()))));
                return true;
            }
            return false;
        }
        if (arguments.length == 1) {
            // TODO: locale
            if (arguments[0].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatColor.GRAY + "- = - = - = - = - = - = - " + ChatColor.RED + "NetworkCoins Help" + ChatColor.GRAY + " - = - = - = - = - = - = -");
                sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins" + ChatColor.GRAY + " - " + ChatColor.GREEN + "View your current %c count".replace("%c", configuration.getProperty(ConfigValues.LOCALE_CURRENCY)));
                sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins help" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Show the NetworkCoins help menu");
                sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins giveAmount (player) (amount)" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Give a player an amount of " + configuration.getProperty(ConfigValues.LOCALE_CURRENCY));
                sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins takeAmount (player) (amount)" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Take a amount of " + configuration.getProperty(ConfigValues.LOCALE_CURRENCY) + " from a player");
                sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins reset (player)" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Reset a players " + configuration.getProperty(ConfigValues.LOCALE_CURRENCY));
                sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins user (player)" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Get a players amount of " + configuration.getProperty(ConfigValues.LOCALE_CURRENCY));
            }
        }
        if (arguments.length == 2) {
            if (arguments[0].equalsIgnoreCase("reset")) {
                Player target = Bukkit.getPlayerExact(arguments[1]);
                if (target != null) {
                    coinManager.reset(target.getUniqueId());
                    sendMessage(sender, ConfigValues.LOCALE_RESET_COINS_SENDER, message ->
                            message.replace("%target", target.getName()));
                    sendMessage(target, ConfigValues.LOCALE_RESET_COINS_TARGET, message -> message);
                    return true;
                }
                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(arguments[1]);
                coinManager.reset(offlineTarget.getUniqueId());
                sendMessage(sender, ConfigValues.LOCALE_RESET_COINS_SENDER, message ->
                        message.replace("%target", offlineTarget.getName()));
                return true;
            }
        }
        if (arguments.length == 2) {
            if (arguments[0].equalsIgnoreCase("user")) {
                OfflinePlayer target = Bukkit.getServer().getPlayer(arguments[1]);
                if (target == null) {
                    target = Bukkit.getOfflinePlayer(arguments[1]);
                }
                final UUID uniqueId = target.getUniqueId();
                sendMessage(sender, ConfigValues.LOCALE_GET_COINS_OTHER, message ->
                        message.replace("%count", String.valueOf(coinManager.getBalance(uniqueId))));
            }
            return true;
        }
        if (arguments.length == 3) {
            if (arguments[0].equalsIgnoreCase("give")) {
                int coins;
                try {
                    coins = Integer.parseInt(arguments[2]);
                } catch (NumberFormatException e) {
                    sendMessage(sender, ConfigValues.LOCALE_WRONG_NUMBER_FORMAT, message -> message);
                    return true;
                }
                Player target = Bukkit.getPlayerExact(arguments[1]);
                if (target != null) {
                    coinManager.giveAmount(target.getUniqueId(), coins);
                    sendMessage(sender, ConfigValues.LOCALE_GIVE_COINS_SENDER, message ->
                            message.replace("%target", target.getName()).replace("%coins", String.valueOf(coins)));
                    sendMessage(target, ConfigValues.LOCALE_GIVE_COINS_TARGET, message ->
                            message.replace("%coins", String.valueOf(coins)));
                    return true;
                }
                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(arguments[1]);
                coinManager.giveAmount(offlineTarget.getUniqueId(), coins);
                sendMessage(sender, ConfigValues.LOCALE_GIVE_COINS_SENDER, message ->
                        message.replace("%target", arguments[2]).replace("%coins", String.valueOf(coins)));
                return true;
            }
            if (arguments[0].equalsIgnoreCase("take")) {
                int coins;
                try {
                    coins = Integer.parseInt(arguments[2]);
                } catch (NumberFormatException e) {
                    sendMessage(sender, ConfigValues.LOCALE_WRONG_NUMBER_FORMAT, message -> message);
                    return true;
                }
                Player target = Bukkit.getPlayerExact(arguments[1]);
                if (target != null) {
                    if (!coinManager.takeAmount(target.getUniqueId(), coins)) {
                        sendMessage(sender, ConfigValues.LOCALE_LIMIT_ERROR, message -> message);
                        return true;
                    }
                    sendMessage(sender, ConfigValues.LOCALE_TAKE_COINS_SENDER, message ->
                            message.replace("%target", target.getName()).replace("%coins", String.valueOf(coins)));
                    sendMessage(target, ConfigValues.LOCALE_TAKE_COINS_TARGET, message ->
                            message.replace("%coins", String.valueOf(coins)));
                    return true;
                }
                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(arguments[1]);
                if (!coinManager.takeAmount(offlineTarget.getUniqueId(), coins)) {
                    sendMessage(sender, ConfigValues.LOCALE_LIMIT_ERROR, message -> message);
                    return true;
                }
                sendMessage(sender, ConfigValues.LOCALE_TAKE_COINS_SENDER, message ->
                        message.replace("%target", arguments[2]).replace("%coins", String.valueOf(coins)));
                return true;
            }
        }
        return false;
    }
}
