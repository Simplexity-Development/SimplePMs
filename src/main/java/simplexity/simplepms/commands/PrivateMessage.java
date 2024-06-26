package simplexity.simplepms.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplepms.handling.MessageHandling;
import simplexity.simplepms.handling.Resolvers;
import simplexity.simplepms.utils.Message;
import simplexity.simplepms.utils.Perm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PrivateMessage implements TabExecutor {

    public static final ArrayList<String> blankList = new ArrayList<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_NO_RECIPIENT_PROVIDED.getMessage()));
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_BLANK_MESSAGE.getMessage()));
            return true;
        }
        if (!sender.hasPermission(Perm.SEND_MESSAGE.getPerm())) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_NO_PERMISSION.getMessage()));
            return false;
        }
        String message = String.join(" ", Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
        if (args[0].equalsIgnoreCase(Message.PDC_CONSOLE.getMessage())) {
            MessageHandling.getInstance().playerSenderConsoleReceiver(sender, message);
            return true;
        }
        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefixAndString(Message.ERROR_RECIPIENT_OFFLINE.getMessage(), "receiver", args[0]));
            return false;
        }
        if (!recipient.hasPermission(Perm.RECEIVE_MESSAGE.getPerm()) && !sender.hasPermission(Perm.RECEIVE_OVERRIDE.getPerm())) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_RECIPIENT_NO_PERMS.getMessage()));
            return false;
        }
        if (sender instanceof Player player){
            MessageHandling.getInstance().playerSenderAndReceiver(player, recipient, message);
        } else {
            MessageHandling.getInstance().consoleSenderPlayerReceiver(sender, recipient, message);
        }
        return true;
    }



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return blankList;
        return null;
    }
}
