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
    public void playerSenderAndReceiver(Player initiator, Player recipient, String messageContent) {
        // Calls private message event so other plugins can interact with this
        PrivateMessageEvent event = new PrivateMessageEvent(initiator, recipient, messageContent, spyingPlayers);
        Bukkit.getServer().getPluginManager().callEvent(event);
        initiator.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, recipient.getName());
        recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, initiator.getName());
        initiator.sendMessage(Resolvers.getInstance().parseMessagePlayerToPlayer(Message.SENDING_FORMAT.getMessage(), initiator, recipient, messageContent));
        recipient.sendMessage(Resolvers.getInstance().parseMessagePlayerToPlayer(Message.RECEIVING_FORMAT.getMessage(), initiator, recipient, messageContent));
        for (Player spy : spyingPlayers) {
            if (!spy.isOnline()) continue;
            if (spy.equals(initiator) || spy.equals(recipient)) continue;
            spy.sendMessage(Resolvers.getInstance().parseMessagePlayerToPlayer(Message.SPY_FORMAT.getMessage(), initiator, recipient, messageContent));
        }
    }

    public void consoleSenderPlayerReceiver(CommandSender initiator, Player recipient, String messageContent) {
        // Call Event
        PrivateMessageEvent event = new PrivateMessageEvent(initiator, recipient, messageContent, spyingPlayers);
        Bukkit.getServer().getPluginManager().callEvent(event);
        recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, Message.PDC_CONSOLE.getMessage());
        recipient.sendMessage(Resolvers.getInstance().parseMessageConsoleToPlayer(Message.RECEIVING_FORMAT.getMessage(), consoleChatComponent, recipient, messageContent));
        initiator.sendMessage(Resolvers.getInstance().parseMessageConsoleToPlayer(Message.SENDING_FORMAT.getMessage(), consoleChatComponent, recipient, messageContent));
        for (Player spy : spyingPlayers) {
            if (!spy.isOnline()) continue;
            if (spy.equals(initiator) || spy.equals(recipient)) continue;
            if (!spy.hasPermission(Perm.CONSOLE_MESSAGE_SPY.getPerm())) continue;
            spy.sendMessage(Resolvers.getInstance().parseMessageConsoleToPlayer(Message.SPY_FORMAT.getMessage(), consoleSpyComponent, recipient, messageContent));
        }
    }


    public void playerSenderConsoleReceiver(CommandSender initiator, String messageContent) {
        if (!(initiator instanceof Player initiatingPlayer)){
            initiator.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_PLAYER_COMMAND.getMessage()));
            return;
        }
        // Call Event
        PrivateMessageEvent event = new PrivateMessageEvent(initiator, Bukkit.getConsoleSender(), messageContent, spyingPlayers);
        Bukkit.getServer().getPluginManager().callEvent(event);
        initiatingPlayer.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, Message.PDC_CONSOLE.getMessage());
        initiatingPlayer.sendMessage(Resolvers.getInstance().parseMessagePlayerToConsole(Message.SENDING_FORMAT.getMessage(), initiatingPlayer, consoleChatComponent, messageContent));
        for (Player spy : spyingPlayers) {
            if (!spy.isOnline()) continue;
            if (spy.equals(initiator)) continue;
            if (!spy.hasPermission(Perm.CONSOLE_MESSAGE_SPY.getPerm())) continue;
            spy.sendMessage(Resolvers.getInstance().parseMessagePlayerToConsole(Message.SPY_FORMAT.getMessage(),initiatingPlayer, consoleSpyComponent, messageContent));
        }
    }
}
