package simplexity.simplepms.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.Message;
import simplexity.simplepms.objects.PlayerBlock;
import simplexity.simplepms.saving.SqlHandler;

import java.util.List;
import java.util.UUID;

public class Blocklist implements CommandExecutor {

    private final MiniMessage miniMessage = SimplePMs.getMiniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.ONLY_PLAYER.getMessage());
            return false;
        }
        UUID uuid = player.getUniqueId();
        List<PlayerBlock> blockList = SqlHandler.getInstance().getBlockList(uuid);
        if (blockList.isEmpty()) {
            player.sendRichMessage(Message.BLOCKLIST_EMPTY.getMessage());
            return true;
        }
        Component message = miniMessage.deserialize(Message.BLOCKLIST_HEADER.getMessage());
        for (PlayerBlock block : blockList) {
            message = message.appendNewline();
            message = message.append(miniMessage.deserialize(
                    Message.BLOCKLIST_NAME.getMessage(),
                    Placeholder.parsed("name", block.blockedPlayerName())
            ));
            if (block.blockReason() == null || block.blockReason().isEmpty()) continue;
            message = message.append(miniMessage.deserialize(Message.BLOCKLIST_REASON.getMessage(),
                    Placeholder.parsed("reason", block.blockReason())));
        }
        sender.sendMessage(message);
        return true;
    }
}
