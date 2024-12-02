package simplexity.simplepms.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.commands.MessageHandling;
import simplexity.simplepms.commands.Util;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.LocaleHandler;

import java.util.Collections;
import java.util.Set;

/**
 * Called when a private message is sent
 */
public class PrivateMessageEvent extends Event implements Cancellable {
    private static final String CONSOLE_SPY = "message.admin.console-spy";
    private final CommandSender initiator;
    private final CommandSender recipient;
    private final String messageContent;
    private final Set<Player> spyingPlayers;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public PrivateMessageEvent(CommandSender initiator, CommandSender recipient, String messageContent, Set<Player> spyingPlayers) {
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
     *
     * @return HandlerList
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the CommandSender who sent the message
     *
     * @return CommandSender
     */
    public CommandSender getInitiator() {
        return initiator;
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
     * Gets the content of the message being sent
     *
     * @return String
     */
    public String getMessageContent() {
        return messageContent;
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

    public void sendMessage(CommandSender initiator, CommandSender target, String messageContent) {
        initiator.sendMessage(Util.getInstance().parseMessage(
                LocaleHandler.Message.FORMAT_SENT.getMessage(),
                initiator, target, messageContent, false));
        target.sendMessage(Util.getInstance().parseMessage(
                LocaleHandler.Message.FORMAT_RECEIVED.getMessage(),
                initiator, target, messageContent, false));
        handleSocialSpy(initiator, target, messageContent);
        MessageHandling.lastMessaged.put(initiator, target);
        MessageHandling.lastMessaged.put(target, initiator);
    }

    public void handleSocialSpy(CommandSender initiator, CommandSender target, String messageContent) {
        Player initiatingPlayer = Util.getInstance().getPlayerFromCommandSender(initiator);
        Player targetPlayer = Util.getInstance().getPlayerFromCommandSender(target);
        boolean needConsoleSpy = initiatingPlayer == null || targetPlayer == null;
        for (Player spyingPlayer : SimplePMs.getSpyingPlayers()) {
            if (initiator.equals(spyingPlayer) || target.equals(spyingPlayer)) {
                continue;
            }
            if (needConsoleSpy && !spyingPlayer.hasPermission(CONSOLE_SPY)) {
                continue;
            }
            spyingPlayer.sendMessage(Util.getInstance().parseMessage(
                    LocaleHandler.Message.FORMAT_SOCIAL_SPY.getMessage(),
                    initiator, target, messageContent, true
            ));
        }
        if (ConfigHandler.getInstance().doesConsoleHaveSocialSpy()) {
            SimplePMs.getPMConsoleSender().sendMessage(Util.getInstance().
                    parseMessage(LocaleHandler.Message.FORMAT_SOCIAL_SPY.getMessage(),
                            initiator, target, messageContent, true
                    ));
        }
    }
}

