package simplexity.simplepms.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Called when a private message is sent
 */
@SuppressWarnings("unused")
public class PrivateMessageEvent extends Event implements Cancellable {

    private CommandSender initiator;
    private CommandSender recipient;
    private String messageContent;
    private final Set<Player> spyingPlayers;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public PrivateMessageEvent(@NotNull CommandSender initiator, @NotNull CommandSender recipient, @NotNull String messageContent, @NotNull Set<Player> spyingPlayers) {
        this.initiator = initiator;
        this.recipient = recipient;
        this.messageContent = messageContent;
        this.spyingPlayers = spyingPlayers;
    }

    /**
     * Gets the CommandSender who sent the message.
     *
     * @return CommandSender
     */
    public CommandSender getInitiator() {
        return initiator;
    }

    /**
     * Sets which CommandSender will start this message.
     * Expected types are either a Player or a ConsoleCommandSender
     * Using other types of CommandSender may lead to unexpected and unsupported behavior
     *
     * @param initiator CommandSender
     */

    public void setInitiator(CommandSender initiator) {
        this.initiator = initiator;
    }

    /**
     * Gets the CommandSender who is to receive the message
     *
     * @return CommandSender
     */
    public CommandSender getRecipient() {
        return recipient;
    }

    /**
     * Sets which CommandSender will receive this message.
     * Expected types are either a Player or a ConsoleCommandSender
     * Using other types of CommandSender may lead to unexpected and unsupported behavior
     *
     * @param recipient CommandSender
     */

    public void setRecipient(CommandSender recipient){
        this.recipient = recipient;
    }

    /**
     * Gets the message that is going to be sent from this event
     *
     * @return String message
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * Sets the message that will be sent from this event.
     * Note that this only affects the actual message content and not the way the message is formatted
     *
     * @param messageContent String
     */

    public void setMessageContent(String messageContent){
        this.messageContent = messageContent;
    }

    /**
     * Gets the list of players who currently have SocialSpy toggled on
     *
     * @return {@code Set<Player>}
     */
    public Set<Player> getSpyingPlayers() {
        return Collections.unmodifiableSet(spyingPlayers);
    }

    /**
     * Checks whether this event has been cancelled
     *
     * @return boolean
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be cancelled
     *
     * @param cancel boolean
     */
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
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
}

