package adhdmc.simplepms.handling;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.events.PrivateMessageEvent;
import adhdmc.simplepms.utils.Message;
import adhdmc.simplepms.utils.Perm;
import adhdmc.simplepms.utils.SPMKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;

public class MessageHandling {
    private static MessageHandling instance;
    private MessageHandling(){}

    public static MessageHandling getInstance() {
        if (instance == null) instance = new MessageHandling();
        return instance;
    }
    HashSet<Player> spyingPlayers = SimplePMs.getSpyingPlayers();
    NamespacedKey lastMessaged = SPMKey.LAST_MESSAGED.getKey();
    Component consoleChatComponent = SimplePMs.getMiniMessage().deserialize(Message.CONSOLE_FORMAT.getMessage());
    Component consoleSpyComponent = SimplePMs.getMiniMessage().deserialize(Message.CONSOLE_FORMAT_SPY.getMessage());

    /**
     * Calls the message event and handles socialspy for a message between 2 players.
     * <br>Sends messageContent to Resolvers.
     * <br>Uses Message keys:
     * <ul><li>RECEIVING_FORMAT
     * <li>SENDING_FORMAT
     * <li>SPY_FORMAT</ul>
     * @param initiator Player
     * @param recipient Player
     * @param messageContent String
     */
    public void playerSenderAndReceiver(Player initiator, Player recipient, String messageContent) {
        // Calls private message event so other plugins can interact with this
        PrivateMessageEvent event = new PrivateMessageEvent(initiator, recipient, messageContent, spyingPlayers);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        initiator.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, recipient.getName());
        recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, initiator.getName());
        initiator.sendMessage(Resolvers.getInstance().parseMessagePlayerToPlayer(Message.SENDING_FORMAT.getMessage(), initiator, recipient, event.getMessageContent()));
        recipient.sendMessage(Resolvers.getInstance().parseMessagePlayerToPlayer(Message.RECEIVING_FORMAT.getMessage(), initiator, recipient, event.getMessageContent()));
        for (Player spy : spyingPlayers) {
            if (!spy.isOnline()) continue;
            if (spy.equals(initiator) || spy.equals(recipient)) continue;
            spy.sendMessage(Resolvers.getInstance().parseMessagePlayerToPlayer(Message.SPY_FORMAT.getMessage(), initiator, recipient, event.getMessageContent()));
        }
    }

    /**
     * Calls the message event and handles socialspy for a message sent by the console, and received by a player
     * <br>Sends messageContent to Resolvers.
     * <br>Uses Message keys:
     * <ul><li>RECEIVING_FORMAT
     * <li>SENDING_FORMAT
     * <li>CONSOLE_FORMAT
     * <li>SPY_FORMAT
     * <li>CONSOLE_FORMAT_SPY</ul>
     * @param initiator CommandSender
     * @param recipient Player
     * @param messageContent String
     */
    public void consoleSenderPlayerReceiver(CommandSender initiator, Player recipient, String messageContent) {
        // Call Event
        PrivateMessageEvent event = new PrivateMessageEvent(initiator, recipient, messageContent, spyingPlayers);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, Message.PDC_CONSOLE.getMessage());
        recipient.sendMessage(Resolvers.getInstance().parseMessageConsoleToPlayer(Message.RECEIVING_FORMAT.getMessage(), consoleChatComponent, recipient, event.getMessageContent()));
        initiator.sendMessage(Resolvers.getInstance().parseMessageConsoleToPlayer(Message.SENDING_FORMAT.getMessage(), consoleChatComponent, recipient, event.getMessageContent()));
        for (Player spy : spyingPlayers) {
            if (!spy.isOnline()) continue;
            if (spy.equals(initiator) || spy.equals(recipient)) continue;
            if (!spy.hasPermission(Perm.CONSOLE_MESSAGE_SPY.getPerm())) continue;
            spy.sendMessage(Resolvers.getInstance().parseMessageConsoleToPlayer(Message.SPY_FORMAT.getMessage(), consoleSpyComponent, recipient, event.getMessageContent()));
        }
    }


    /**
     * Calls the message event and handles socialspy for a message sent by a player to the console.
     * <br>Sends messageContent to Resolvers.
     * <br>Uses Message keys:
     * <ul><li>ERROR_PLAYER_COMMAND
     * <li>SENDING_FORMAT
     * <li>CONSOLE_FORMAT
     * <li>SPY_FORMAT
     * <li>CONSOLE_FORMAT_SPY</ul>
     * @param initiator CommandSender
     * @param messageContent String
     */

    public void playerSenderConsoleReceiver(CommandSender initiator, String messageContent) {
        if (!(initiator instanceof Player initiatingPlayer)){
            initiator.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_PLAYER_COMMAND.getMessage()));
            return;
        }
        // Call Event
        PrivateMessageEvent event = new PrivateMessageEvent(initiator, Bukkit.getConsoleSender(), messageContent, spyingPlayers);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        initiatingPlayer.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, Message.PDC_CONSOLE.getMessage());
        initiatingPlayer.sendMessage(Resolvers.getInstance().parseMessagePlayerToConsole(Message.SENDING_FORMAT.getMessage(), initiatingPlayer, consoleChatComponent, event.getMessageContent()));
        for (Player spy : spyingPlayers) {
            if (!spy.isOnline()) continue;
            if (spy.equals(initiator)) continue;
            if (!spy.hasPermission(Perm.CONSOLE_MESSAGE_SPY.getPerm())) continue;
            spy.sendMessage(Resolvers.getInstance().parseMessagePlayerToConsole(Message.SPY_FORMAT.getMessage(),initiatingPlayer, consoleSpyComponent, event.getMessageContent()));
        }
    }
}
