package simplexity.simplepms.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.config.Message;
import simplexity.simplepms.logic.Util;
import simplexity.simplepms.objects.PlayerBlock;
import simplexity.simplepms.saving.Cache;

import java.util.Arrays;

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
        String blockReason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        PlayerBlock playerBlock = new PlayerBlock(
                player.getUniqueId(),
                playerToBlock.getName(),
                playerToBlock.getUniqueId(),
                blockReason);
        Cache.addBlockedUser(player.getUniqueId(), playerBlock);
        player.sendRichMessage(Message.BLOCKED_PLAYER.getMessage(),
                Placeholder.parsed("name", playerToBlockString));
        return true;
    }
}
