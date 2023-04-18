package adhdmc.simplepms.commands;

import adhdmc.simplepms.handling.MessageHandling;
import adhdmc.simplepms.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PrivateMessage implements CommandExecutor, TabCompleter {
    private static PrivateMessage instance;

    public static PrivateMessage getInstance() {
        return instance;
    }

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
        if (!sender.hasPermission(Perms.SEND_MESSAGE.getPerm())) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_NO_PERMISSION.getMessage()));
            return false;
        }
        String message = String.join(" ", Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
        if (args[0].equalsIgnoreCase(Message.PDC_CONSOLE.getMessage())) {
            MessageHandling.getInstance().consoleReceiver(sender, message);
            return true;
        }
        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefixAndString(Message.ERROR_RECIPIENT_OFFLINE.getMessage(), "receiver", args[0]));
            return false;
        }
        if (sender instanceof Player player){
            MessageHandling.getInstance().playerSenderAndReceiver(player, recipient, message);
        } else {
            MessageHandling.getInstance().consoleSender(sender, recipient, message);
        }
        return true;
    }



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return new ArrayList<>();
        return null;
    }
}
