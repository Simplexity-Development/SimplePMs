package simplexity.simplepms.logic;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.Message;
import simplexity.simplepms.events.PrivateMessageEvent;

public class Messaging {

    private static final String CONSOLE_SPY = "message.admin.console-spy";
    private static final String SOCIAL_SPY_BYPASS = "message.bypass.social-spy";
    private static final String COMMAND_SPY_BYPASS = "message.bypass.command-spy";

    public static void sendMessage(PrivateMessageEvent privateMessageEvent, CommandSender initiator, CommandSender target, String messageContent) {
        initiator.sendMessage(Util.getInstance().parseMessage(
                Message.FORMAT_SENT.getMessage(),
                initiator, target, messageContent, false));
        target.sendMessage(Util.getInstance().parseMessage(
                Message.FORMAT_RECEIVED.getMessage(),
                initiator, target, messageContent, false));
        handleSocialSpy(initiator, target, messageContent);
        PreProcessing.lastMessaged.put(initiator, target);
        PreProcessing.lastMessaged.put(target, initiator);
    }

    public static void sendCommandSpy(CommandSender initiator, String command, String messageContent) {
        if (initiator.hasPermission(COMMAND_SPY_BYPASS)) return;
        Component parsedMessage = Util.getInstance().parseMessage(
                Message.FORMAT_COMMAND_SPY.getMessage(), initiator,
                command, messageContent, true);
        for (Player spyingPlayer : SimplePMs.getSpyingPlayers()) {
            if (initiator.equals(spyingPlayer)) continue;
            spyingPlayer.sendMessage(parsedMessage);
        }
        if (ConfigHandler.getInstance().doesConsoleHaveCommandSpy()) {
            sendConsoleMessage(parsedMessage);
        }
    }

    public static void handleSocialSpy(CommandSender initiator, CommandSender target, String messageContent) {
        boolean consoleSpy = false;
        Player initiatorPlayer = Util.getInstance().getPlayerFromCommandSender(initiator);
        Player targetPlayer = Util.getInstance().getPlayerFromCommandSender(target);
        if (initiatorPlayer == null || targetPlayer == null) consoleSpy = true;
        if (consoleSpy) {
            sendConsoleSpy(initiator, target, messageContent);
        } else {
            sendSocialSpy(initiator, target, messageContent);
        }
    }

    private static void sendConsoleSpy(CommandSender initiator, CommandSender target, String messageContent) {
        Component parsedMessage = Util.getInstance().parseMessage(
                Message.FORMAT_SOCIAL_SPY.getMessage(),
                initiator, target, messageContent,
                true);
        for (Player spyingPlayer : SimplePMs.getSpyingPlayers()) {
            if (initiator.equals(spyingPlayer) || target.equals(spyingPlayer)) continue;
            if (!spyingPlayer.hasPermission(CONSOLE_SPY)) continue;
            spyingPlayer.sendMessage(parsedMessage);
        }
        if (ConfigHandler.getInstance().doesConsoleHaveSocialSpy()) {
            sendConsoleMessage(parsedMessage);
        }
    }

    private static void sendSocialSpy(CommandSender initiator, CommandSender target, String messageContent) {
        Component parsedMessage = Util.getInstance().parseMessage(
                Message.FORMAT_SOCIAL_SPY.getMessage(),
                initiator, target, messageContent,
                true);
        for (Player spyingPlayer : SimplePMs.getSpyingPlayers()) {
            if (initiator.equals(spyingPlayer) || target.equals(spyingPlayer)) continue;
            spyingPlayer.sendMessage(parsedMessage);
        }
        if (ConfigHandler.getInstance().doesConsoleHaveSocialSpy()) {
            sendConsoleMessage(parsedMessage);
        }
    }

    private static void sendConsoleMessage(Component message){
        SimplePMs.getPMConsoleSender().sendMessage(message);
    }


}
