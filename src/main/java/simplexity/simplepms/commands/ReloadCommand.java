package simplexity.simplepms.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.LocaleConfig;
import simplexity.simplepms.utils.Message;
import simplexity.simplepms.utils.Perm;

public class ReloadCommand implements CommandExecutor {
    MiniMessage miniMessage = SimplePMs.getMiniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(Perm.RELOAD_CONFIG.getPerm())) {
            sender.sendMessage(miniMessage.deserialize(Message.ERROR_NO_PERMISSION.getMessage(),
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage())));
            return false;
        }
        LocaleConfig.getInstance().reloadLocale();
        sender.sendMessage(miniMessage.deserialize(Message.CONFIG_RELOADED.getMessage(),
                Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage())));
        return false;
    }


}
