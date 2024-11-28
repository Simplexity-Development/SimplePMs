package simplexity.simplepms.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplepms.config.LocaleHandler;

import java.util.List;

public class ReplyCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendRichMessage(LocaleHandler.Message.BLANK_MESSAGE.getMessage());
            return false;
        }
        CommandSender recipient = MessageHandling.lastMessaged.get(sender);
        if (recipient == null) {
            sender.sendRichMessage(LocaleHandler.Message.CANNOT_REPLY.getMessage());
            return false;
        }
        if (!MessageHandling.getInstance().canMessageTarget(sender, recipient)) {
            return false;
        }
        String message = String.join(" ", args);
        MessageHandling.getInstance().callPMEvent(sender, recipient, message);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
