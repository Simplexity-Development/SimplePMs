package simplexity.simplepms.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplepms.config.LocaleHandler;

import java.util.Arrays;
import java.util.List;

public class PrivateMessage implements TabExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendRichMessage(LocaleHandler.Message.NO_RECIPIENT_PROVIDED.getMessage());
            return false;
        }
        if (args.length < 2) {
            sender.sendRichMessage(LocaleHandler.Message.BLANK_MESSAGE.getMessage());
            return false;
        }
        CommandSender target = MessageHandling.getInstance().getTarget(sender, args);
        if (target == null) {
            sender.sendRichMessage(LocaleHandler.Message.RECIPIENT_NOT_EXIST.getMessage(),
                    Placeholder.unparsed("name", args[0]));
            return false;
        }
        if (!MessageHandling.getInstance().canMessageTarget(sender, target)) {
            return false;
        }
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        MessageHandling.getInstance().callPMEvent(sender, target, message);
        return true;
    }




    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return List.of();
        return null;
    }
}
