package it.feargames.networkcoins.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class AbstractCommandExecutor implements CommandExecutor, TabExecutor {
    @NonNull
    protected final String command;
    protected List<String> aliases;
    protected String description;
    protected String usage;
    protected String permission;
    protected String permissionMessage;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        return null;
    }

    public void register(@NonNull Plugin plugin) {
        try {
            PluginCommand pluginCommand = (PluginCommand) ConstructorUtils.invokeConstructor(PluginCommand.class,
                    new Object[]{getCommand(), plugin},
                    new Class[]{String.class, Plugin.class});
            if (getAliases() != null) {
                pluginCommand.setAliases(getAliases());
            }
            if (getDescription() != null) {
                pluginCommand.setDescription(getDescription());
            }
            if (getUsage() != null) {
                pluginCommand.setUsage(getUsage());
            }
            if (getPermission() != null) {
                pluginCommand.setPermission(getPermission());
            }
            if (getPermissionMessage() != null) {
                pluginCommand.setPermissionMessage(getPermissionMessage());
            }
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
            CommandMap commandMap = (CommandMap) FieldUtils.readDeclaredField(plugin.getServer(), "commandMap", true);
            commandMap.register(plugin.getName(), pluginCommand);
        } catch (Exception e) {
            throw new RuntimeException("Unable to register the " + getCommand() + " command!", e);
        }
    }
}
