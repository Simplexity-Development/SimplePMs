package simplexity.simplepms.logic;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.Message;
import simplexity.simplepms.events.PrivateMessageEvent;

import java.util.HashMap;

public class PMHandler {
    public static final HashMap<CommandSender, CommandSender> lastMessaged = new HashMap<>();

    public static void handlePrivateMessage(CommandSender initiator, CommandSender target, String messageContent) throws CommandSyntaxException {
        PrivateMessageEvent messageEvent = callPMEvent(initiator, target, messageContent);
        handleMessageSend(initiator, target, messageContent);
        handleMessageReceive(initiator, target, messageContent);
        handleSocialSpy(initiator, target, messageContent);
        lastMessaged.put(initiator, target);
        lastMessaged.put(target, initiator);
    }

    private static void handleMessageSend(CommandSender initiator, CommandSender target, String messageContent) {
        initiator.sendMessage(MessageUtils.getInstance().parseMessage(
                Message.FORMAT_SENT.getMessage(),
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
                Message.FORMAT_RECEIVED.getMessage(),
                initiator, target, messageContent, false));
        if (!ConfigHandler.getInstance().receivingMessagePlaysSound()) return;
        if (!(target instanceof Player player)) return;
        player.playSound(player,
                ConfigHandler.getInstance().getReceiveSound(),
                ConfigHandler.getInstance().getReceiveVolume(),
                ConfigHandler.getInstance().getReceivePitch());
    }


    public static void sendCommandSpy(CommandSender initiator, String command, String messageContent) {
        if (initiator.hasPermission(Constants.BYPASS_COMMAND_SPY)) return;
        Component parsedMessage = MessageUtils.getInstance().parseMessage(
                Message.FORMAT_COMMAND_SPY.getMessage(), initiator,
                command, messageContent, true);
        for (Player spyingPlayer : SimplePMs.getSpyingPlayers()) {
            if (initiator.equals(spyingPlayer)) continue;
            spyingPlayer.sendMessage(parsedMessage);
            playSpySound(spyingPlayer);
        }
        if (ConfigHandler.getInstance().doesConsoleHaveCommandSpy()) {
            sendConsoleMessage(parsedMessage);
        }
    }

    private static void handleSocialSpy(CommandSender initiator, CommandSender target, String messageContent) {
        boolean consoleSpy = false;
        Player initiatorPlayer = MessageUtils.getInstance().getPlayerFromCommandSender(initiator);
        Player targetPlayer = MessageUtils.getInstance().getPlayerFromCommandSender(target);
        if (initiatorPlayer == null || targetPlayer == null) consoleSpy = true;
        if (!consoleSpy) {
            if (initiatorPlayer.hasPermission(Constants.BYPASS_SOCIAL_SPY) || targetPlayer.hasPermission(Constants.BYPASS_SOCIAL_SPY))
                return;
            sendSocialSpy(initiator, target, messageContent);
        } else {
            sendConsoleSpy(initiator, target, messageContent);
        }
    }

    private static void sendConsoleSpy(CommandSender initiator, CommandSender target, String messageContent) {
        Component parsedMessage = MessageUtils.getInstance().parseMessage(
                Message.FORMAT_SOCIAL_SPY.getMessage(),
                initiator, target, messageContent,
                true);
        for (Player spyingPlayer : SimplePMs.getSpyingPlayers()) {
            if (initiator.equals(spyingPlayer) || target.equals(spyingPlayer)) continue;
            if (!spyingPlayer.hasPermission(Constants.ADMIN_CONSOLE_SPY)) continue;
            spyingPlayer.sendMessage(parsedMessage);
            playSpySound(spyingPlayer);
        }
        if (ConfigHandler.getInstance().doesConsoleHaveSocialSpy()) {
            sendConsoleMessage(parsedMessage);
        }
    }

    private static void sendSocialSpy(CommandSender initiator, CommandSender target, String messageContent) {
        Component parsedMessage = MessageUtils.getInstance().parseMessage(
                Message.FORMAT_SOCIAL_SPY.getMessage(),
                initiator, target, messageContent,
                true);
        for (Player spyingPlayer : SimplePMs.getSpyingPlayers()) {
            if (initiator.equals(spyingPlayer) || target.equals(spyingPlayer)) continue;
            spyingPlayer.sendMessage(parsedMessage);
            playSpySound(spyingPlayer);
        }
        if (ConfigHandler.getInstance().doesConsoleHaveSocialSpy()) {
            sendConsoleMessage(parsedMessage);
        }
    }

    private static void sendConsoleMessage(Component message) {
        SimplePMs.getPMConsoleSender().sendMessage(message);
    }

    private static void playSpySound(Player spyingPlayer) {
        if (!ConfigHandler.getInstance().messagePlaysSoundForSpy()) return;
        spyingPlayer.playSound(spyingPlayer,
                ConfigHandler.getInstance().getSpySound(),
                ConfigHandler.getInstance().getSpyVolume(),
                ConfigHandler.getInstance().getSpyPitch());
    }

    private static PrivateMessageEvent callPMEvent(CommandSender initiator, CommandSender target, String messageContent) {
        PrivateMessageEvent messageEvent = new PrivateMessageEvent(initiator, target, messageContent, SimplePMs.getSpyingPlayers());
        SimplePMs.getInstance().getServer().getPluginManager().callEvent(messageEvent);
        if (messageEvent.isCancelled()) return null;
        return messageEvent;
    }

}
