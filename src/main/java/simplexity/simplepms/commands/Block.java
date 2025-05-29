package simplexity.simplepms.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.commands.arguments.OfflinePlayerArgument;
import simplexity.simplepms.config.LocaleMessage;
import simplexity.simplepms.logic.BlockHandler;
import simplexity.simplepms.logic.Constants;

@SuppressWarnings("UnstableApiUsage")
public class Block {

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        OfflinePlayerArgument offlinePlayerArg = new OfflinePlayerArgument();

        return Commands.literal("block")
                .requires(Block::canExecute)
                .then(Commands.argument("target", offlinePlayerArg)
                        .suggests(offlinePlayerArg::suggestOnlinePlayers)
                        .executes(Block::execute)
                        .then(Commands.argument("reason", StringArgumentType.greedyString())
                                .executes(Block::executeWithReason))).build();

    }

    private static boolean canExecute(CommandSourceStack css) {
        if (!(css.getSender() instanceof Player)) return false;
        return css.getSender().hasPermission(Constants.MESSAGE_BLOCK);
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSender sender = ctx.getSource().getSender();
        if (!(sender instanceof Player playerSender)) throw Exceptions.ERROR_MUST_BE_PLAYER.create();
        OfflinePlayer target = ctx.getArgument("target", OfflinePlayer.class);
        BlockHandler.addBlockedPlayer(playerSender, target, null);
        sendSuccessMessage(playerSender, target);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeWithReason(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSender sender = ctx.getSource().getSender();
        if (!(sender instanceof Player playerSender)) throw Exceptions.ERROR_MUST_BE_PLAYER.create();
        OfflinePlayer target = ctx.getArgument("target", OfflinePlayer.class);
        String reason = ctx.getArgument("reason", String.class);
        BlockHandler.addBlockedPlayer(playerSender, target, reason);
        sendSuccessMessage(playerSender, target);
        return Command.SINGLE_SUCCESS;
    }

    private static void sendSuccessMessage(@NotNull Player sender, @NotNull OfflinePlayer blockedPlayer) {
        String blockedPlayerName = blockedPlayer.getName();
        if (blockedPlayerName == null) blockedPlayerName = "[NO NAME FOUND]"; // Attempting to make intellij shut up
        sender.sendRichMessage(
                LocaleMessage.BLOCKED_PLAYER.getMessage(),
                Placeholder.parsed("name", blockedPlayerName)
        );
    }

}
