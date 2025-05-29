package simplexity.simplepms.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UnblockUserEvent extends Event implements Cancellable {
    private boolean cancelled;
    private UUID initiatorUuid;
    private UUID blockedPlayerUuid;
    private static final HandlerList handlers = new HandlerList();

    public UnblockUserEvent(UUID initiatorUuid, UUID blockedPlayerUuid){
        this.initiatorUuid = initiatorUuid;
        this.blockedPlayerUuid = blockedPlayerUuid;
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

    public UUID getInitiatorUuid() {
        return initiatorUuid;
    }

    public void setInitiatorUuid(UUID initiatorUuid) {
        this.initiatorUuid = initiatorUuid;
    }

    public UUID getBlockedPlayerUuid() {
        return blockedPlayerUuid;
    }

    public void setBlockedPlayerUuid(UUID blockedPlayerUuid) {
        this.blockedPlayerUuid = blockedPlayerUuid;
    }

}
