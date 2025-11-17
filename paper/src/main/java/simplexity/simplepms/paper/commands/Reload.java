package simplexity.simplepms.paper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import simplexity.simplepms.paper.config.ConfigHandler;
import simplexity.simplepms.paper.config.LocaleMessage;
import simplexity.simplepms.paper.logic.Constants;
import simplexity.simplepms.paper.saving.DatabaseHandler;

@SuppressWarnings("UnstableApiUsage")
public class Reload {
    public static LiteralCommandNode<CommandSourceStack> createCommand() {

        return Commands.literal("spmreload")
                .requires(css -> css.getSender().hasPermission(Constants.PLUGIN_RELOAD))
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    ConfigHandler.getInstance().loadConfigValues();
                    DatabaseHandler.getInstance().reloadDatabase();
                    sender.sendRichMessage(LocaleMessage.RELOADED.getMessage());
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
