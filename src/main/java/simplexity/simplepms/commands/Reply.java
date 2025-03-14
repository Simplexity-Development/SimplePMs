package simplexity.simplepms.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplepms.config.Message;
import simplexity.simplepms.logic.PreProcessing;

import java.util.List;

public class Reply implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendRichMessage(Message.BLANK_MESSAGE.getMessage());
            return false;
        }
        CommandSender recipient = PreProcessing.lastMessaged.get(sender);
        if (recipient == null) {
            sender.sendRichMessage(Message.CANNOT_REPLY.getMessage());
            return false;
        }
        if (PreProcessing.getInstance().messagingBlocked(sender, recipient, recipient.getName())) {
            return false;
        }
        String message = String.join(" ", args);
        PreProcessing.getInstance().callPMEvent(sender, recipient, message);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
