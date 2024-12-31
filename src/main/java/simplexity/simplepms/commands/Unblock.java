package simplexity.simplepms.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplepms.config.Message;
import simplexity.simplepms.objects.PlayerBlock;
import simplexity.simplepms.saving.SqlHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Unblock implements TabExecutor {
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
        String blockString = args[0];
        List<PlayerBlock> playerBlockList = SqlHandler.getInstance().getBlockList(player.getUniqueId());
        UUID blockedPlayerUUID = blockedPlayerUUID(blockString, playerBlockList);
        if (blockedPlayerUUID == null) {
            player.sendRichMessage(Message.PLAYER_NOT_BLOCKED.getMessage());
            return false;
        }
        SqlHandler.getInstance().removeBlockedPlayer(player.getUniqueId(), blockedPlayerUUID);
        player.sendRichMessage(Message.NO_LONGER_BLOCKING.getMessage(),
                Placeholder.parsed("name", blockString));
        return true;
    }

    private UUID blockedPlayerUUID(String playerBlocked, List<PlayerBlock> playerBlockList) {
        for (PlayerBlock playerBlock : playerBlockList) {
            if (playerBlock.blockedPlayerName().equals(playerBlocked)) {
                return playerBlock.blockedPlayerUUID();
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
        for (PlayerBlock block : SqlHandler.getInstance().getBlockList(player.getUniqueId())) {
            blocked.add(block.blockedPlayerName());
        }
        return blocked;
    }
}
