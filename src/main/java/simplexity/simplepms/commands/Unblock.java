package simplexity.simplepms.commands;

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
import simplexity.simplepms.config.LocaleMessage;
import simplexity.simplepms.logic.Constants;
import simplexity.simplepms.logic.UnblockHandler;
import simplexity.simplepms.objects.PlayerBlock;
import simplexity.simplepms.saving.Cache;

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
            builder.suggest(block.blockedPlayerName(),
                    MessageComponentSerializer.message().serialize(Component.text(block.blockReason())));
        }
        return builder.buildFuture();
    }



    /*
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage(Message.ERROR_NOT_A_PLAYER.getMessage());
            return false;
        }
        if (args.length == 0) {
            player.sendRichMessage(Message.ERROR_NO_USER_PROVIDED.getMessage());
            return false;
        }
        String blockString = args[0];
        List<PlayerBlock> playerBlockList = Cache.getBlockList(player.getUniqueId());
        PlayerBlock blockedPlayer = getBlockedPlayer(blockString, playerBlockList);
        if (blockedPlayer == null) {
            player.sendRichMessage(Message.PLAYER_NOT_BLOCKED.getMessage());
            return false;
        }
        Cache.removeBlockedUser(player.getUniqueId(), blockedPlayer.blockedPlayerUUID());
        player.sendRichMessage(Message.NO_LONGER_BLOCKING.getMessage(),
                Placeholder.parsed("name", blockString));
        return true;
    }

    private PlayerBlock getBlockedPlayer(String playerBlocked, List<PlayerBlock> playerBlockList) {
        for (PlayerBlock playerBlock : playerBlockList) {
            if (playerBlock.blockedPlayerName().equals(playerBlocked)) {
                return playerBlock;
            }
        }
        return null;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return null;
        }
        ArrayList<String> blocked = new ArrayList<>();
        for (PlayerBlock block : Cache.getBlockList(player.getUniqueId())) {
            blocked.add(block.blockedPlayerName());
        }
        return blocked;
    }

     */
}
