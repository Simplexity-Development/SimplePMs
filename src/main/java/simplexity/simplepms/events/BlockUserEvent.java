package simplexity.simplepms.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

@SuppressWarnings("unused")
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


    /**
     * Gets whether this event has been cancelled
     *
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
     * Gets the handlerList for this event
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
     * Gets the UUID of the player doing the blocking
     *
     * @return UUID
     */

    @NotNull
    public UUID getInitiatorUuid() {
        return initiatorUuid;
    }

    /**
     * Sets the UUID for which player will have their block list altered
     * Changing this UUID will change which user has a new player added to their blocked list
     * This method expects a Player UUID, using andy UUID that does not belong to a player will lead to
     * unexpected and unsupported behavior
     *
     * @param initiatorUuid UUID
     */
    public void setInitiatorUuid(@NotNull UUID initiatorUuid) {
        this.initiatorUuid = initiatorUuid;
    }

    /**
     * Gets the UUID of the player being blocked in this event
     *
     * @return UUID player Being blocked
     */
    @NotNull
    public UUID getBlockedPlayerUuid() {
        return blockedPlayerUuid;
    }

    /**
     * Sets the UUID of which player should be blocked
     * Changing this UUID will alter which user is being placed into the player's block list
     * This method expects a Player UUID, using any UUID that does not belong to a player will lead to
     * unexpected and unsupported behavior
     *
     * @param blockedPlayerUuid UUID
     */
    public void setBlockedPlayerUuid(@NotNull UUID blockedPlayerUuid) {
        this.blockedPlayerUuid = blockedPlayerUuid;
    }

    /**
     * Gets the name of the player being blocked
     * This name is not automatically updated when someone's username changes
     * it is only used as a player-readable identifier for remembering who they blocked and being able to unblock them
     * All verification is done through UUID
     *
     * @return String name
     */
    @NotNull
    public String getBlockedPlayerName() {
        return blockedPlayerName;
    }

    /**
     * Sets the name of the player being blocked, as of the current time.
     * This is solely for player-readable identification
     * This is used in the block list and in the unblock command, but UUID is used for actual verification.
     * Display names can be used here
     *
     * @param blockedPlayerName String name
     */
    public void setBlockedPlayerName(@NotNull String blockedPlayerName) {
        this.blockedPlayerName = blockedPlayerName;
    }

    /**
     * Gets the provided reason for blocking. May be null if no reason was provided.
     *
     * @return String block reason
     */
    @Nullable
    public String getBlockReason() {
        return blockReason;
    }

    /**
     * Sets the reason for blocking the other player.
     * This is displayed in the user's own blocklist, and when hovering over usernames when unblocking
     *
     * @param blockReason String
     */
    public void setBlockReason(@Nullable String blockReason) {
        this.blockReason = blockReason;
    }
}
