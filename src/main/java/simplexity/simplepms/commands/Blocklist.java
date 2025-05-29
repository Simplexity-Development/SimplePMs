package simplexity.simplepms.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.LocaleMessage;
import simplexity.simplepms.logic.Constants;
import simplexity.simplepms.saving.Cache;
import simplexity.simplepms.saving.objects.PlayerBlock;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class Blocklist {

    private static final MiniMessage miniMessage = SimplePMs.getMiniMessage();

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("blocklist")
                .requires(Blocklist::canExecute)
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    UUID uuid = player.getUniqueId();
                    List<PlayerBlock> blockList = Cache.getBlockList(uuid);
                    if (blockList == null || blockList.isEmpty()) {
                        player.sendRichMessage(LocaleMessage.BLOCKLIST_EMPTY.getMessage());
                        return Command.SINGLE_SUCCESS;
                    }
                    Component message = miniMessage.deserialize(LocaleMessage.BLOCKLIST_HEADER.getMessage());
                    for (PlayerBlock block : blockList) {
                        message = message.appendNewline();
                        message = message.append(miniMessage.deserialize(
                                LocaleMessage.BLOCKLIST_NAME.getMessage(),
                                Placeholder.parsed("name", block.blockedPlayerName())
                        ));
                        if (block.blockReason() == null || block.blockReason().isEmpty()) continue;
                        message = message.append(miniMessage.deserialize(LocaleMessage.BLOCKLIST_REASON.getMessage(),
                                Placeholder.parsed("reason", block.blockReason())));
                    }
                    player.sendMessage(message);
                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    private static boolean canExecute(CommandSourceStack css) {
        if (!(css.getSender() instanceof Player)) return false;
        return css.getSender().hasPermission(Constants.MESSAGE_BLOCK);
    }
}
