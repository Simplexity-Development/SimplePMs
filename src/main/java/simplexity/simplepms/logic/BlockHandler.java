package simplexity.simplepms.logic;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.events.BlockUserEvent;
import simplexity.simplepms.saving.Cache;
import simplexity.simplepms.saving.objects.PlayerBlock;

import javax.annotation.Nullable;

public class BlockHandler {

    public static void addBlockedPlayer(@NotNull Player blockingPlayer, @NotNull OfflinePlayer playerToBlock, @Nullable String reason) {
        BlockUserEvent blockUserEvent = callBlockEvent(blockingPlayer, playerToBlock, reason);
        if (blockUserEvent.isCancelled()) return;
        PlayerBlock playerBlock = new PlayerBlock(
                blockUserEvent.getInitiatorUuid(),
                blockUserEvent.getBlockedPlayerName(),
                blockUserEvent.getBlockedPlayerUuid(),
                blockUserEvent.getBlockReason());
        Cache.addBlockedUser(blockingPlayer.getUniqueId(), playerBlock);
    }

    private static BlockUserEvent callBlockEvent(@NotNull Player blockingPlayer, @NotNull OfflinePlayer playerToBlock, @Nullable String reason) {
        String blockedPlayerName = playerToBlock.getName();
        if (blockedPlayerName == null) blockedPlayerName = "[NO NAME FOUND]";
        BlockUserEvent blockUserEvent = new BlockUserEvent(blockingPlayer.getUniqueId(), playerToBlock.getUniqueId(), blockedPlayerName, reason);
        SimplePMs.getInstance().getServer().getPluginManager().callEvent(blockUserEvent);
        return blockUserEvent;
    }


}
