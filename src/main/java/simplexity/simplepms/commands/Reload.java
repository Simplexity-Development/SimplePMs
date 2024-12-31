package simplexity.simplepms.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.Message;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ConfigHandler.getInstance().loadConfigValues();
        sender.sendRichMessage(Message.RELOADED.getMessage());
        return true;
    }


}
