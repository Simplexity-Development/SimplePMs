package simplexity.simplepms.logic;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.LocaleMessage;
import simplexity.simplepms.events.PrivateMessageEvent;
import simplexity.simplepms.saving.Cache;

import java.util.HashMap;

public class PMHandler {
    public static final HashMap<CommandSender, CommandSender> lastMessaged = new HashMap<>();

    public static void handlePrivateMessage(CommandSender initiator, CommandSender target, String messageContent) {
        PrivateMessageEvent messageEvent = callPMEvent(initiator, target, messageContent);
        if (messageEvent.isCancelled()) return;
        initiator = messageEvent.getInitiator();
        target = messageEvent.getRecipient();
        messageContent = messageEvent.getMessageContent();
        handleMessageSend(initiator, target, messageContent);
        handleMessageReceive(initiator, target, messageContent);
        lastMessaged.put(initiator, target);
        lastMessaged.put(target, initiator);
        if (!(initiator instanceof Player) || !(target instanceof Player)) {
            SpyHandler.handleConsoleSpy(messageEvent);
        } else {
            SpyHandler.handleSocialSpy(messageEvent);
        }
    }

    private static void handleMessageSend(CommandSender initiator, CommandSender target, String messageContent) {
        initiator.sendMessage(MessageUtils.getInstance().parseMessage(
                LocaleMessage.FORMAT_SENT.getMessage(),
                initiator, target, messageContent, false));
        if (!ConfigHandler.getInstance().sendingMessagePlaysSound()) return;
        if (!(initiator instanceof Player player)) return;
        player.playSound(player,
                ConfigHandler.getInstance().getSendSound(),
                ConfigHandler.getInstance().getSendVolume(),
                ConfigHandler.getInstance().getSendPitch());
    }

    private static void handleMessageReceive(CommandSender initiator, CommandSender target, String messageContent) {
        target.sendMessage(MessageUtils.getInstance().parseMessage(
                LocaleMessage.FORMAT_RECEIVED.getMessage(),
                initiator, target, messageContent, false));
        if (!ConfigHandler.getInstance().receivingMessagePlaysSound()) return;
        if (!(target instanceof Player player)) return;
        player.playSound(player,
                ConfigHandler.getInstance().getReceiveSound(),
                ConfigHandler.getInstance().getReceiveVolume(),
                ConfigHandler.getInstance().getReceivePitch());
    }




    private static PrivateMessageEvent callPMEvent(CommandSender initiator, CommandSender target, String messageContent) {
        PrivateMessageEvent messageEvent = new PrivateMessageEvent(initiator, target, messageContent, Cache.getSpyingPlayers());
        SimplePMs.getInstance().getServer().getPluginManager().callEvent(messageEvent);
        return messageEvent;
    }

}
