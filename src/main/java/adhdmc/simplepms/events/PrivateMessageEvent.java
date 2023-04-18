package adhdmc.simplepms.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class PrivateMessageEvent extends Event {
    public PrivateMessageEvent(CommandSender initiator, CommandSender recipient, String messageContent, HashSet<Player> spyingPlayers) {
        this.initiator = initiator;
        this.recipient = recipient;
        this.messageContent = messageContent;
        this.spyingPlayers = spyingPlayers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }

    private CommandSender initiator;
    private CommandSender recipient;
    private String messageContent;
    private HashSet<Player> spyingPlayers;

    public CommandSender getInitiator() {
        return initiator;
    }

    public CommandSender getRecipient() {
        return recipient;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public HashSet<Player> getSpyingPlayers() {
        return spyingPlayers;
    }
}
