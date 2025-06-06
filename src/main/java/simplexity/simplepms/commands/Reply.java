package simplexity.simplepms.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import simplexity.simplepms.commands.util.Exceptions;
import simplexity.simplepms.commands.util.MessageChecks;
import simplexity.simplepms.logic.Constants;
import simplexity.simplepms.logic.PMHandler;

@SuppressWarnings("UnstableApiUsage")
public class Reply {

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("reply")
                .requires(Reply::canExecute)
                .then(messageArg()).build();
    }

    public static LiteralCommandNode<CommandSourceStack> createAlias() {
        return Commands.literal("r")
                .requires(Reply::canExecute)
                .then(messageArg()).build();
    }

    private static boolean canExecute(CommandSourceStack css) {
        return css.getSender().hasPermission(Constants.MESSAGE_SEND);
    }

    private static RequiredArgumentBuilder<CommandSourceStack, String> messageArg() {
        return Commands.argument("message", StringArgumentType.greedyString())
                .executes(Reply::execute);
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSender sender = ctx.getSource().getSender();
        CommandSender target = PMHandler.lastMessaged.get(sender);
        if (target == null) throw Exceptions.ERROR_NOBODY_TO_REPLY_TO.create();
        MessageChecks.userChecks(sender, target, target.getName());
        String message = ctx.getArgument("message", String.class);
        PMHandler.handlePrivateMessage(sender, target, message);
        return Command.SINGLE_SUCCESS;
    }

}
