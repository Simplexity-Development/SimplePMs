package simplexity.simplepms.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplepms.config.LocaleHandler;

import java.util.List;

public class PrivateMessage implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Util.parseMessage(LocaleHandler.getInstance().getErrorNoRecipientProvided(), sender, null, null));
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(Util.parseMessage(LocaleHandler.getInstance().getErrorBlankMessage(), sender, null, null));
            return false;
        }
        String recipientName = args[0];
        Player providedPlayer = Util.getPlayer(recipientName);
        if (providedPlayer == null) {
            sender.sendMessage(Util.parseMessage(LocaleHandler.getInstance().getErrorRecipientOffline(), sender, null, null));
            return false;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        String fullMessage = message.toString().trim();
        if (fullMessage.isEmpty()) {
            sender.sendMessage(Util.parseMessage(LocaleHandler.getInstance().getErrorBlankMessage(), sender, null, null));
            return false;
        }
        providedPlayer.sendMessage(Util.parseMessage(LocaleHandler.getInstance().getMessageReceived(), sender, providedPlayer, fullMessage));
        sender.sendMessage(Util.parseMessage(LocaleHandler.getInstance().getMessageSent(), sender, providedPlayer, fullMessage));
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return List.of();
        return null;
    }
}
