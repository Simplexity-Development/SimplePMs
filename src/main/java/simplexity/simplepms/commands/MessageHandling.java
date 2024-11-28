package simplexity.simplepms.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.LocaleHandler;
import simplexity.simplepms.events.PrivateMessageEvent;
import simplexity.simplepms.objects.PlayerBlock;
import simplexity.simplepms.objects.PlayerSettings;
import simplexity.simplepms.saving.SQLHandler;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class MessageHandling {
    private static MessageHandling instance;
    private static final String RECEIVE_PERMISSION = "message.basic.receive";
    private static final String ADMIN_OVERRIDE = "message.admin.override";
    private static final Logger logger = SimplePMs.getInstance().getLogger();
    public static final HashMap<CommandSender, CommandSender> lastMessaged = new HashMap<>();


    private MessageHandling() {
    }

    public static MessageHandling getInstance() {
        if (instance == null) instance = new MessageHandling();
        return instance;
    }

    public CommandSender getTarget(String[] args) {
        String targetString = args[0];
        if (ConfigHandler.getInstance().getValidNamesForConsole().contains(targetString)) {
            return SimplePMs.getPMConsoleSender();
        }
        return Util.getPlayer(targetString);
    }

    public boolean messagingBlocked(CommandSender sender, CommandSender recipient) {
        if (sender.hasPermission(ADMIN_OVERRIDE)) {
            return false;
        }
        if (!(recipient instanceof Player target)) {
            return !canSendToNonPlayer(sender, recipient);
        }
        if (!(sender instanceof Player initiator)) {
            logger.info("[ERROR] There was an attempt to send a message from a non-player that is not the console. Info: ");
            logger.info("Sender: " + sender.getName() + " [" + sender + "]");
            logger.info("Recipient: " + recipient.getName() + " [" + recipient + "]");
            sender.sendRichMessage(LocaleHandler.Message.SOMETHING_WENT_WRONG.getMessage());
            return true;
        }
        if (!initiator.canSee(target) && !ConfigHandler.getInstance().canPlayersSendToHiddenPlayers()) {
            initiator.sendRichMessage(LocaleHandler.Message.RECIPIENT_NOT_EXIST.getMessage(),
                    Placeholder.unparsed("name", target.getName()));
            return true;
        }
        if (messagesDisabled(initiator)) {
            initiator.sendRichMessage(LocaleHandler.Message.YOUR_MESSAGES_CURRENTLY_DISABLED.getMessage());
            return true;
        }
        if (messagesDisabled(target) || !target.hasPermission(RECEIVE_PERMISSION) || userBlocked(target, initiator)) {
            initiator.sendRichMessage(LocaleHandler.Message.TARGET_CANNOT_RECIEVE_MESSAGE.getMessage());
            return true;
        }
        if (userBlocked(initiator, target)) {
            initiator.sendRichMessage(LocaleHandler.Message.CANNOT_MESSAGE_SOMEONE_YOU_BLOCKED.getMessage());
            return true;
        }
        return false;
    }

    private boolean canSendToNonPlayer(CommandSender sender, CommandSender recipient) {
        if (!recipient.equals(SimplePMs.getPMConsoleSender())) {
            logger.info("[ERROR] There was an attempt to send a message to a non-player that is not the console. Info: ");
            logger.info("Sender: " + sender.getName() + " [" + sender + "]");
            logger.info("Recipient: " + recipient.getName() + " [" + recipient + "]");
            sender.sendRichMessage(LocaleHandler.Message.SOMETHING_WENT_WRONG.getMessage());
            return true;
        }
        if (ConfigHandler.getInstance().canPlayersSendToConsole()) return true;
        sender.sendRichMessage(LocaleHandler.Message.CANNOT_MESSAGE_CONSOLE.getMessage());
        return false;
    }

    public boolean messagesDisabled(Player player) {
        PlayerSettings playerSettings = SQLHandler.playerSettings.get(player.getUniqueId());
        if (playerSettings == null) {
            return false;
        }
        return playerSettings.messagesDisabled();
    }

    public boolean userBlocked(Player player1, Player player2) {
        List<PlayerBlock> playerBlocks = SQLHandler.blockList.get(player1.getUniqueId());
        if (playerBlocks == null) {
            return false;
        }
        for (PlayerBlock playerBlock : playerBlocks) {
            if (playerBlock.blockedPlayerUUID().equals(player2.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public void callPMEvent(CommandSender initiator, CommandSender target, String messageContent) {
        PrivateMessageEvent messageEvent = new PrivateMessageEvent(initiator, target, messageContent, SimplePMs.getSpyingPlayers());
        SimplePMs.getInstance().getServer().getPluginManager().callEvent(messageEvent);
    }

}
