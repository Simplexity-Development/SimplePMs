package adhdmc.simplepms.commands;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.utils.SPMMessage;
import adhdmc.simplepms.utils.SPMPerm;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {
    MiniMessage miniMessage = SimplePMs.getMiniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(SPMPerm.RELOAD_CONFIG.getPerm())) {
            sender.sendMessage(miniMessage.deserialize(SPMMessage.ERROR_NO_PERMISSION.getMessage(),
                    Placeholder.parsed("plugin_prefix", SPMMessage.PLUGIN_PREFIX.getMessage())));
            return false;
        }
        SimplePMs.getInstance().reloadConfig();
        SPMMessage.reloadMessages();
        sender.sendMessage(miniMessage.deserialize(SPMMessage.CONFIG_RELOADED.getMessage(),
                Placeholder.parsed("plugin_prefix", SPMMessage.PLUGIN_PREFIX.getMessage())));
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}