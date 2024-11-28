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

import java.util.List;

public class MessageHandling {
    private static MessageHandling instance;
    private static final String RECEIVE_PERMISSION = "message.basic.receive";
    private static final String ADMIN_OVERRIDE = "message.admin.override";


    private MessageHandling() {
    }

    public static MessageHandling getInstance() {
        if (instance == null) instance = new MessageHandling();
        return instance;
    }

    public CommandSender getTarget(CommandSender sender, String[] args) {
        String targetString = args[0];
        if (ConfigHandler.getInstance().getValidNamesForConsole().contains(targetString)) {
            return SimplePMs.getInstance().getServer().getConsoleSender();
        }
        return Util.getPlayer(targetString);
    }

    public boolean canMessageTarget(CommandSender sender, CommandSender recipient) {
        if (sender.hasPermission(ADMIN_OVERRIDE)) {
            return true;
        }
        if (!(recipient instanceof Player target)) {
            return ConfigHandler.getInstance().canPlayersSendToConsole();
        }
        if (!(sender instanceof Player initiator)) {
            sender.sendRichMessage("Somehow, you do not have the permission message.admin.override and are also not a player. This should not happen.");
            return false;
        }
        if (!initiator.canSee(target)) {
            initiator.sendRichMessage(LocaleHandler.Message.RECIPIENT_NOT_EXIST.getMessage(),
                    Placeholder.unparsed("name", target.getName()));
            return false;
        }
        if (messagesDisabled(initiator)) {
            initiator.sendRichMessage(LocaleHandler.Message.YOUR_MESSAGES_CURRENTLY_DISABLED.getMessage());
            return false;
        }
        if (messagesDisabled(target) || !target.hasPermission(RECEIVE_PERMISSION) || userBlocked(target, initiator)) {
            initiator.sendRichMessage(LocaleHandler.Message.TARGET_CANNOT_RECIEVE_MESSAGE.getMessage());
            return false;
        }
        if (userBlocked(initiator, target)) {
            initiator.sendRichMessage(LocaleHandler.Message.CANNOT_MESSAGE_SOMEONE_YOU_BLOCKED.getMessage());
            return false;
        }
        return true;
    }

    public boolean messagesDisabled(Player player) {
        PlayerSettings playerSettings = SQLHandler.playerSettings.get(player.getUniqueId());
        if (playerSettings == null) {
            return false;
        }
        return !playerSettings.messagesEnabled();
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

    public PrivateMessageEvent callPMEvent(CommandSender initiator, CommandSender target, String messageContent){
        PrivateMessageEvent messageEvent = new PrivateMessageEvent(initiator, target, messageContent, SimplePMs.getSpyingPlayers());
        SimplePMs.getInstance().getServer().getPluginManager().callEvent(messageEvent);
        if (messageEvent.isCancelled()) {
            return null;
        }
        return messageEvent;
    }

    public void sendMessage(CommandSender sender, CommandSender target, String message) {

    }

}
