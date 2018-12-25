package it.feargames.networkcoins;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigValues implements SettingsHolder {
    // General
    public static final Property<Integer> STARTING_BALANCE = newProperty("starting_balance", 0);
    public static final Property<Integer> MAXIMUM_BALANCE = newProperty("maximum_balance", Integer.MAX_VALUE);
    public static final Property<Integer> MINIMUM_BALANCE = newProperty("minimum_balance", Integer.MIN_VALUE);
    public static final Property<String> COMMAND_NAME = newProperty("command_name", "networkcoins");

    // Storage
    public static final Property<String> STORAGE_DRIVER = newProperty("storage.driver", "");
    public static final Property<String> STORAGE_JDBC_URL = newProperty("storage.jdbcUrl", "");
    public static final Property<String> STORAGE_USER = newProperty("storage.user", "root");
    public static final Property<String> STORAGE_PASSWORD = newProperty("storage.password", "");
    public static final Property<Integer> STORAGE_MAXIMUM_POOL_SIZE = newProperty("storage.maximumPoolSize", 4);
    public static final Property<Integer> STORAGE_MAXIMUM_CONNECTION_LIFETIME = newProperty("storage.maximumConnectionLifetime", 1800);

    // Locale
    public static final Property<String> LOCALE_PREFIX = newProperty("locale.prefix", "&7[&cNetworkCoins&7]");
    public static final Property<String> LOCALE_CURRENCY = newProperty("locale.currency", "Coins");
    public static final Property<String> LOCALE_NO_PERMISSION = newProperty("locale.no_permission", "%prefix &cYou do not have permission to execute this command!");
    public static final Property<String> LOCALE_COMMAND_USAGE = newProperty("locale.command_usage", "%prefix &cUsage: /<command> [args]");
    public static final Property<String> LOCALE_COMMAND_DESCRIPTION = newProperty("locale.command_description", "NetworkCoins root command");
    public static final Property<String> LOCALE_WRONG_NUMBER_FORMAT = newProperty("locale.wrong_number_format", "%prefix &cWrong number format!");
    public static final Property<String> LOCALE_LIMIT_ERROR = newProperty("locale.limit_error", "%prefix &cError: &a%target &conly has &a%coins %currency!");
    public static final Property<String> LOCALE_GET_COINS = newProperty("locale.get_coins", "%prefix &7You currently have &a%count &7%currency");
    public static final Property<String> LOCALE_GET_COINS_OTHER = newProperty("locale.get_coins_other", "%prefix &a%target &7currently has &a%count &7%currency");
    public static final Property<String> LOCALE_RESET_COINS_SENDER = newProperty("locale.reset_coins_giver", "%prefix &7You have reset &a%target’s &7%currency balance");
    public static final Property<String> LOCALE_RESET_COINS_TARGET = newProperty("locale.reset_coins_receiver", "%prefix &7Your %currency balance has been reset!");
    public static final Property<String> LOCALE_GIVE_COINS_SENDER = newProperty("locale.give_coins_giver", "%prefix &7You have given &a%coins &7%currency to &a%target");
    public static final Property<String> LOCALE_GIVE_COINS_TARGET = newProperty("locale.give_coins_receiver", "%prefix &a%coins &7%currency have been added to your account");
    public static final Property<String> LOCALE_TAKE_COINS_SENDER = newProperty("locale.take_coins_giver", "%prefix &7You have taken &a%coins &7%currency from &a%target");
    public static final Property<String> LOCALE_TAKE_COINS_TARGET = newProperty("locale.take_coins_receiver", "%prefix &a%coins &7%currency have been taken from your account");
}
