package simplexity.simplepms.paper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplepms.paper.commands.util.Exceptions;
import simplexity.simplepms.paper.config.LocaleMessage;
import simplexity.simplepms.paper.logic.Constants;
import simplexity.simplepms.paper.logic.UnblockHandler;
import simplexity.simplepms.paper.saving.Cache;
import com.simplexity.simplepms.common.database.objects.PlayerBlock;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class Unblock {

    public static LiteralCommandNode<CommandSourceStack> createCommand() {

        return Commands.literal("unblock")
                .requires(Unblock::canExecute)
                .then(Commands.argument("target", StringArgumentType.word())
                        .suggests(Unblock::suggestBlockedUsers)
                        .executes(Unblock::execute)).build();
    }

    private static boolean canExecute(CommandSourceStack css) {
        if (!(css.getSender() instanceof Player)) return false;
        return css.getSender().hasPermission(Constants.MESSAGE_BLOCK);
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSender sender = ctx.getSource().getSender();
        if (!(sender instanceof Player playerSender)) throw Exceptions.ERROR_MUST_BE_PLAYER.create();
        String target = ctx.getArgument("target", String.class);
        UnblockHandler.removeBlockedPlayer(playerSender, target);
        sendSuccessMessage(playerSender, target);
        return Command.SINGLE_SUCCESS;
    }

    private static void sendSuccessMessage(Player sender, String name) {
        sender.sendRichMessage(
                LocaleMessage.NO_LONGER_BLOCKING.getMessage(),
                Placeholder.parsed("name", name)
        );
    }

    private static CompletableFuture<Suggestions> suggestBlockedUsers(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        CommandSender sender = ctx.getSource().getSender();
        if (!(sender instanceof Player playerSender)) return builder.buildFuture();
        List<PlayerBlock> blockedPlayers = Cache.getBlockList(playerSender.getUniqueId());
        if (blockedPlayers.isEmpty()) return builder.buildFuture();
        for (PlayerBlock block : blockedPlayers) {
            if (block == null) continue;
            String blockReason = block.getBlockReason();
            if (blockReason == null || blockReason.isEmpty()) {
                builder.suggest(block.getBlockedPlayerName());
            } else {
                builder.suggest(block.getBlockedPlayerName(),
                        MessageComponentSerializer.message().serialize(Component.text(blockReason)));
            }
        }
        return builder.buildFuture();
    }
}
