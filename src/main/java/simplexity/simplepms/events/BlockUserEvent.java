package simplexity.simplepms.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class BlockUserEvent extends Event implements Cancellable {
    private boolean cancelled;
    private UUID initiatorUuid;
    private UUID blockedPlayerUuid;
    private String blockedPlayerName;
    private String blockReason;

    private static final HandlerList handlers = new HandlerList();

    public BlockUserEvent(@NotNull UUID initiatorUuid, @NotNull UUID blockedPlayerUuid, @NotNull String blockedPlayerName, @Nullable String blockReason) {
        this.initiatorUuid = initiatorUuid;
        this.blockedPlayerUuid = blockedPlayerUuid;
        this.blockedPlayerName = blockedPlayerName;
        this.blockReason = blockReason;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the handlerList for this event
     *
     * @return HandlerList
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public UUID getInitiatorUuid() {
        return initiatorUuid;
    }

    public void setInitiatorUuid(@NotNull UUID initiatorUuid) {
        this.initiatorUuid = initiatorUuid;
    }

    @NotNull
    public UUID getBlockedPlayerUuid() {
        return blockedPlayerUuid;
    }

    public void setBlockedPlayerUuid(@NotNull UUID blockedPlayerUuid) {
        this.blockedPlayerUuid = blockedPlayerUuid;
    }

    @NotNull
    public String getBlockedPlayerName() {
        return blockedPlayerName;
    }

    public void setBlockedPlayerName(@NotNull String blockedPlayerName) {
        this.blockedPlayerName = blockedPlayerName;
    }

    @Nullable
    public String getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(@Nullable String blockReason) {
        this.blockReason = blockReason;
    }
}
