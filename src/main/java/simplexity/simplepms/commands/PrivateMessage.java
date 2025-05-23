package simplexity.simplepms.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplepms.config.Message;
import simplexity.simplepms.logic.PreProcessing;

import java.util.Arrays;
import java.util.List;

public class PrivateMessage implements TabExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendRichMessage(Message.NO_RECIPIENT_PROVIDED.getMessage());
            return false;
        }
        if (args.length < 2) {
            sender.sendRichMessage(Message.BLANK_MESSAGE.getMessage());
            return false;
        }
        CommandSender target = PreProcessing.getInstance().getTarget(args);
        if (target == null) {
            sender.sendRichMessage(Message.RECIPIENT_NOT_EXIST.getMessage(),
                    Placeholder.unparsed("name", args[0]));
            return false;
        }
        if (PreProcessing.getInstance().messagingBlocked(sender, target, args[0])) {
            return false;
        }
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        PreProcessing.getInstance().callPMEvent(sender, target, message);
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return List.of();
        return null;
    }
}
