package simplexity.simplepms.logic;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.commands.util.Exceptions;
import simplexity.simplepms.events.UnblockUserEvent;
import simplexity.simplepms.saving.objects.PlayerBlock;
import simplexity.simplepms.saving.Cache;

import java.util.List;
import java.util.UUID;

public class UnblockHandler {

    public static void removeBlockedPlayer(@NotNull Player blockingPlayer, @NotNull String userToRemove) throws CommandSyntaxException {
        List<PlayerBlock> playerBlocks = Cache.getBlockList(blockingPlayer.getUniqueId());
        UUID playerToUnblockUuid = null;
        for (PlayerBlock block : playerBlocks) {
            if (block.getBlockedPlayerName().equalsIgnoreCase(userToRemove)) {
                playerToUnblockUuid = block.getBlockedPlayerUUID();
                break;
            }
        }
        if (playerToUnblockUuid == null) throw Exceptions.ERROR_NOT_BLOCKING_ANYONE_BY_THIS_NAME.create();
        UnblockUserEvent unblockEvent = callUnblockEvent(blockingPlayer.getUniqueId(), playerToUnblockUuid);
        if (unblockEvent.isCancelled()) return;
        Cache.removeBlockedUser(unblockEvent.getInitiatorUuid(), unblockEvent.getBlockedPlayerUuid());

    }

    private static UnblockUserEvent callUnblockEvent(@NotNull UUID initiatorUuid, @NotNull UUID playerToUnblockUuid) {
        UnblockUserEvent unblockEvent = new UnblockUserEvent(initiatorUuid, playerToUnblockUuid);
        Bukkit.getServer().getPluginManager().callEvent(unblockEvent);
        return unblockEvent;
    }
}
