package simplexity.simplepms.listeners;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.commands.MessageHandling;
import simplexity.simplepms.commands.Util;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.events.PrivateMessageEvent;

public class PrivateMessageListener implements Listener {

    private static final String CONSOLE_SPY = "message.admin.console-spy";

    @EventHandler
    public void onPrivateMessage(PrivateMessageEvent messageEvent) {
        CommandSender initiator = messageEvent.getInitiator();
        CommandSender target = messageEvent.getRecipient();
        String messageContent = messageEvent.getMessageContent();
        initiator.sendMessage(Util.parseMessage(
                ConfigHandler.getInstance().getSentMessageFormat(),
                initiator, target, messageContent, false));
        target.sendMessage(Util.parseMessage(
                ConfigHandler.getInstance().getReceivedMessageFormat(),
                initiator, target, messageContent, false));
        handleSocialSpy(initiator, target, messageContent);
        MessageHandling.lastMessaged.put(initiator, target);
        MessageHandling.lastMessaged.put(target, initiator);
    }

    public void handleSocialSpy(CommandSender initiator, CommandSender target, String messageContent) {
        Player initiatingPlayer = Util.getPlayerFromCommandSender(initiator);
        Player targetPlayer = Util.getPlayerFromCommandSender(target);
        boolean needConsoleSpy = initiatingPlayer == null || targetPlayer == null;
        for (Player spyingPlayer : SimplePMs.getSpyingPlayers()) {
            if (initiator.equals(spyingPlayer) || target.equals(spyingPlayer)) {
                continue;
            }
            if (needConsoleSpy && !spyingPlayer.hasPermission(CONSOLE_SPY)) {
                continue;
            }
            spyingPlayer.sendMessage(Util.parseMessage(
                    ConfigHandler.getInstance().getSocialSpyFormat(),
                    initiator, target, messageContent, true
            ));
        }
        if (ConfigHandler.getInstance().doesConsoleHaveSocialSpy()) {
            SimplePMs.getPMConsoleSender().sendMessage(Util.parseMessage(
                    ConfigHandler.getInstance().getSocialSpyFormat(),
                    initiator, target, messageContent, true
            ));
        }
    }


}
