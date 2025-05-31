package simplexity.simplepms.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("unused")
public class UnblockUserEvent extends Event implements Cancellable {
    private boolean cancelled;
    private UUID initiatorUuid;
    private UUID blockedPlayerUuid;
    private static final HandlerList handlers = new HandlerList();

    public UnblockUserEvent(UUID initiatorUuid, UUID blockedPlayerUuid) {
        this.initiatorUuid = initiatorUuid;
        this.blockedPlayerUuid = blockedPlayerUuid;
    }

    /**
     * Gets whether this event has been cancelled
     * @return boolean Cancelled
     */

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be cancelled
     *
     * @param cancel boolean
     */

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the handler list, idk bukkit requires 2 of these or I'm just doing it wrong
     *
     * @return HandlerList
     */

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

    /**
     * Gets the UUID of the player who requested to unblock someone
     *
     * @return UUID
     */
    public UUID getInitiatorUuid() {
        return initiatorUuid;
    }

    /**
     * Sets the UUID of the person who will request to unblock someone
     *
     * @param initiatorUuid UUID
     */

    public void setInitiatorUuid(UUID initiatorUuid) {
        this.initiatorUuid = initiatorUuid;
    }

    /**
     * Gets the UUID of the player who will be unblocked
     *
     * @return UUID
     */
    public UUID getBlockedPlayerUuid() {
        return blockedPlayerUuid;
    }

    /**
     * Sets the UUID of the player who will be unblocked
     *
     * @param blockedPlayerUuid UUID
     */
    public void setBlockedPlayerUuid(UUID blockedPlayerUuid) {
        this.blockedPlayerUuid = blockedPlayerUuid;
    }

}
