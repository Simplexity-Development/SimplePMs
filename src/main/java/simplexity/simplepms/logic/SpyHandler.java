package simplexity.simplepms.logic;

import net.kyori.adventure.text.Component;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.LocaleMessage;
import simplexity.simplepms.events.PrivateMessageEvent;
import simplexity.simplepms.saving.Cache;

public class SpyHandler {

    private static final CommandSender console = SimplePMs.getPMConsoleSender();

    public static void sendCommandSpy(CommandSender initiator, String command, String messageContent) {
        Component parsedMessage = MessageUtils.getInstance().parseMessage(
                LocaleMessage.FORMAT_COMMAND_SPY.getMessage(), initiator,
                command, messageContent, true);
        if (ConfigHandler.getInstance().doesConsoleHaveCommandSpy()) {
            console.sendMessage(parsedMessage);
        }
        if (initiator.hasPermission(Constants.BYPASS_COMMAND_SPY)) return;
        for (Player spyingPlayer : Cache.getSpyingPlayers()) {
            if (initiator.equals(spyingPlayer)) continue;
            if (!spyingPlayer.hasPermission(Constants.ADMIN_SOCIAL_SPY)) continue;
            spyingPlayer.sendMessage(parsedMessage);
            playSpySound(spyingPlayer);
        }
    }

    public static void handleSocialSpy(PrivateMessageEvent messageEvent) {
        Player initiatorPlayer = (Player) messageEvent.getInitiator();
        Player targetPlayer = (Player) messageEvent.getRecipient();
        Component parsedMessage = MessageUtils.getInstance().parseMessage(
                LocaleMessage.FORMAT_SOCIAL_SPY.getMessage(),
                initiatorPlayer, targetPlayer, messageEvent.getMessageContent(),
                true);
        if (ConfigHandler.getInstance().doesConsoleHaveSocialSpy()) console.sendMessage(parsedMessage);
        if (initiatorPlayer.hasPermission(Constants.BYPASS_SOCIAL_SPY) ||
            targetPlayer.hasPermission(Constants.BYPASS_SOCIAL_SPY)) return;
        for (Player spyingPlayer : messageEvent.getSpyingPlayers()) {
            if (!spyingPlayer.hasPermission(Constants.ADMIN_SOCIAL_SPY)) continue;
            if (spyingPlayer.equals(initiatorPlayer) ||
                spyingPlayer.equals(targetPlayer)) continue;
            spyingPlayer.sendMessage(parsedMessage);
            playSpySound(spyingPlayer);
        }
    }

    public static void handleConsoleSpy(PrivateMessageEvent messageEvent) {
        Component parsedMessage = MessageUtils.getInstance().parseMessage(
                LocaleMessage.FORMAT_SOCIAL_SPY.getMessage(),
                messageEvent.getInitiator(), messageEvent.getRecipient(), messageEvent.getMessageContent(),
                true);
        for (Player spyingPlayer : messageEvent.getSpyingPlayers()) {
            if (!spyingPlayer.hasPermission(Constants.ADMIN_CONSOLE_SPY)) continue;
            if (spyingPlayer.equals(messageEvent.getInitiator()) ||
                spyingPlayer.equals(messageEvent.getRecipient())) continue;
            spyingPlayer.sendMessage(parsedMessage);
            playSpySound(spyingPlayer);
        }
    }

    private static void playSpySound(Player spyingPlayer) {
        if (!ConfigHandler.getInstance().messagePlaysSoundForSpy()) return;
        Sound sound = Registry.SOUNDS.get(ConfigHandler.getInstance().getSpySound());
        if (sound == null) return;
        spyingPlayer.playSound(spyingPlayer, sound,
                ConfigHandler.getInstance().getSpyVolume(),
                ConfigHandler.getInstance().getSpyPitch());
    }
}
