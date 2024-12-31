package simplexity.simplepms.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.config.Message;
import simplexity.simplepms.logic.Util;
import simplexity.simplepms.saving.SqlHandler;

import java.util.Arrays;
import java.util.UUID;

public class Block implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage(Message.ONLY_PLAYER.getMessage());
            return false;
        }
        if (args.length == 0) {
            player.sendRichMessage(Message.NO_USER_PROVIDED.getMessage());
            return false;
        }
        String playerToBlockString = args[0];
        Player playerToBlock = Util.getInstance().getPlayer(playerToBlockString);
        if (playerToBlock == null) {
            sender.sendRichMessage(Message.RECIPIENT_NOT_EXIST.getMessage(),
                    Placeholder.parsed("name", playerToBlockString));
            return false;
        }
        UUID uuid = player.getUniqueId();
        UUID blockPlayerUUID = playerToBlock.getUniqueId();
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        SqlHandler.getInstance().addBlockedPlayer(uuid, blockPlayerUUID, reason);
        player.sendRichMessage(Message.BLOCKED_PLAYER.getMessage(),
                Placeholder.parsed("name", playerToBlockString));
        return true;
    }
}
