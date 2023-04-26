package adhdmc.simplepms.commands;

import adhdmc.simplepms.handling.MessageHandling;
import adhdmc.simplepms.utils.Perm;
import adhdmc.simplepms.utils.SPMKey;
import adhdmc.simplepms.utils.Message;
import adhdmc.simplepms.handling.Resolvers;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReplyCommand implements TabExecutor {
    private final NamespacedKey lastMessaged = SPMKey.LAST_MESSAGED.getKey();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_BLANK_MESSAGE.getMessage()));
            return false;
        }
        if (!(sender instanceof Player playerInitiator)) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_PLAYER_COMMAND.getMessage()));
            return false;
        }
        PersistentDataContainer playerPDC = playerInitiator.getPersistentDataContainer();
        String lastMessagedPlayer = playerPDC.getOrDefault(lastMessaged, PersistentDataType.STRING, "");
        String message = String.join(" ", args);
        if (lastMessagedPlayer.equalsIgnoreCase(Message.PDC_CONSOLE.getMessage())) {
            MessageHandling.getInstance().playerSenderConsoleReceiver(playerInitiator, message);
            return true;
        }
        Player recipient = Bukkit.getServer().getPlayer(lastMessagedPlayer);
        if (recipient == null) {
            playerInitiator.sendMessage(Resolvers.getInstance().parsePluginPrefixAndString(Message.NO_USER_TO_REPLY.getMessage(), "receiver", lastMessagedPlayer));
            return false;
        }
        if (!recipient.isOnline()) {
            playerInitiator.sendMessage(Resolvers.getInstance().parsePluginPrefixAndString(Message.ERROR_RECIPIENT_OFFLINE.getMessage(), "receiver", lastMessagedPlayer));
        }
        if (!recipient.hasPermission(Perm.RECEIVE_MESSAGE.getPerm()) && !sender.hasPermission(Perm.RECEIVE_OVERRIDE.getPerm())) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_RECIPIENT_NO_PERMS.getMessage()));
            return false;
        }
        MessageHandling.getInstance().playerSenderAndReceiver(playerInitiator, recipient, message);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return PrivateMessage.blankList;
    }
}
