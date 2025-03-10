package simplexity.simplepms.logic;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.Message;

public class Messaging {

    private static final String CONSOLE_SPY = "message.admin.console-spy";
    private static final String SOCIAL_SPY_BYPASS = "message.bypass.social-spy";
    private static final String COMMAND_SPY_BYPASS = "message.bypass.command-spy";

    public static void sendMessage(CommandSender initiator, CommandSender target, String messageContent) {
        handleMessageSend(initiator, target, messageContent);
        handleMessageReceive(initiator, target, messageContent);
        handleSocialSpy(initiator, target, messageContent);
        PreProcessing.lastMessaged.put(initiator, target);
        PreProcessing.lastMessaged.put(target, initiator);
    }

    private static void handleMessageSend(CommandSender initiator, CommandSender target, String messageContent) {
        initiator.sendMessage(Util.getInstance().parseMessage(
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
        target.sendMessage(Util.getInstance().parseMessage(
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
        if (initiator.hasPermission(COMMAND_SPY_BYPASS)) return;
        Component parsedMessage = Util.getInstance().parseMessage(
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

    public static void handleSocialSpy(CommandSender initiator, CommandSender target, String messageContent) {
        boolean consoleSpy = false;
        Player initiatorPlayer = Util.getInstance().getPlayerFromCommandSender(initiator);
        Player targetPlayer = Util.getInstance().getPlayerFromCommandSender(target);
        if (initiatorPlayer == null || targetPlayer == null) consoleSpy = true;
        if (!consoleSpy) {
            if (initiatorPlayer.hasPermission(SOCIAL_SPY_BYPASS) || targetPlayer.hasPermission(SOCIAL_SPY_BYPASS)) return;
            sendSocialSpy(initiator, target, messageContent);
        } else {
            sendConsoleSpy(initiator, target, messageContent);
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
            playSpySound(spyingPlayer);
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


}
