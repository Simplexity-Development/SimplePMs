package simplexity.simplepms.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Called when a private message is sent
 */
public class PrivateMessageEvent extends Event implements Cancellable {
    private final CommandSender initiator;
    private final CommandSender recipient;
    private final String messageContent;
    private final List<Player> spyingPlayers;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public PrivateMessageEvent(CommandSender initiator, CommandSender recipient, String messageContent, List<Player> spyingPlayers) {
        this.initiator = initiator;
        this.recipient = recipient;
        this.messageContent = messageContent;
        this.spyingPlayers = spyingPlayers;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the handlerList for this event
     * @return HandlerList
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the CommandSender who sent the message
     * @return CommandSender
     */
    public CommandSender getInitiator() {
        return initiator;
    }

    /**
     * Gets the CommandSender who is to receive the message
     * @return CommandSender
     */
    public CommandSender getRecipient() {
        return recipient;
    }

    /**
     * Gets the content of the message being sent
     * @return String
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * Gets the list of players who currently have SocialSpy toggled on
     * @return {@code Set<Player>}
     */
    public List<Player> getSpyingPlayers() {
        return spyingPlayers;
    }

    /**
     * Checks whether this event has been cancelled
     * @return boolean
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be cancelled
     * @param cancel boolean
     */
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
