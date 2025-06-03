package simplexity.simplepms.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.LocaleMessage;
import simplexity.simplepms.logic.Constants;
import simplexity.simplepms.saving.SqlHandler;

@SuppressWarnings("UnstableApiUsage")
public class Reload {
    public static LiteralCommandNode<CommandSourceStack> createCommand() {

        return Commands.literal("spmreload")
                .requires(css -> css.getSender().hasPermission(Constants.RELOAD))
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    ConfigHandler.getInstance().loadConfigValues();
                    SqlHandler.getInstance().reloadDatabase();
                    sender.sendRichMessage(LocaleMessage.RELOADED.getMessage());
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
