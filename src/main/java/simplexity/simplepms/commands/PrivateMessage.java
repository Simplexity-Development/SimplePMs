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
import simplexity.simplepms.commands.arguments.TargetArgument;
import simplexity.simplepms.commands.util.MessageChecks;
import simplexity.simplepms.logic.Constants;
import simplexity.simplepms.logic.PMHandler;
import simplexity.simplepms.commands.arguments.Target;

@SuppressWarnings("UnstableApiUsage")
public class PrivateMessage {


    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("msg")
                .requires(PrivateMessage::canExecute)
                .then(targetArg()
                        .then(messageArg())).build();
    }

    public static LiteralCommandNode<CommandSourceStack> createTellAlias() {
        return Commands.literal("tell")
                .requires(PrivateMessage::canExecute)
                .then(targetArg()
                        .then(messageArg())).build();
    }

    public static LiteralCommandNode<CommandSourceStack> createWhisperAlias() {
        return Commands.literal("w")
                .requires(PrivateMessage::canExecute)
                .then(targetArg()
                        .then(messageArg())).build();
    }

    private static RequiredArgumentBuilder<CommandSourceStack, Target> targetArg(){
        TargetArgument targetArg = new TargetArgument();
        return Commands.argument("target", targetArg)
                .suggests(targetArg::suggestOnlinePlayers);
    }

    private static RequiredArgumentBuilder<CommandSourceStack, String> messageArg() {
        return Commands.argument("message", StringArgumentType.greedyString())
                .executes(PrivateMessage::execute);
    }

    private static boolean canExecute(CommandSourceStack css) {
        return css.getSender().hasPermission(Constants.MESSAGE_SEND);
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSender sender = ctx.getSource().getSender();
        Target argTarget = ctx.getArgument("target", Target.class);
        CommandSender target = argTarget.sender();
        MessageChecks.userChecks(sender, target, argTarget.providedName());
        String message = ctx.getArgument("message", String.class);
        PMHandler.handlePrivateMessage(sender, target, message);
        return Command.SINGLE_SUCCESS;
    }

}
