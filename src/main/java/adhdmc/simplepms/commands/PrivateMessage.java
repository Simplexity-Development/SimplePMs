package adhdmc.simplepms.commands;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.events.PrivateMessageEvent;
import adhdmc.simplepms.utils.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PrivateMessage implements CommandExecutor, TabCompleter {
    private static PrivateMessage instance;

    public static PrivateMessage getInstance() {
        return instance;
    }

    HashSet<Player> spyingPlayers = SimplePMs.getSpyingPlayers();
    NamespacedKey lastMessaged = SPMKey.LAST_MESSAGED.getKey();

    MiniMessage miniMessage = SimplePMs.getMiniMessage();
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
        if (args[0].equalsIgnoreCase(Message.PDC_CONSOLE.getMessage())) {
            consoleReciever(sender);
            return true;
        }
        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefixAndString(Message.ERROR_RECIPIENT_OFFLINE.getMessage(), "receiver", args[0]));
            return false;
        }
        String message = String.join(" ", Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
        if (sender instanceof Player player){
            playerSender(player, recipient, message);
        } else {
            consoleSender(sender, recipient, message);
        }
        // TODO: Implement message event in place of this.
        return true;
    }

    public void playerSender(Player initiator, Player recipient, String messageContent) {
        // Calls private message event so other plugins can interact with this
        PrivateMessageEvent event = new PrivateMessageEvent(initiator, recipient, messageContent, spyingPlayers);
        Bukkit.getServer().getPluginManager().callEvent(event);
        initiator.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, recipient.getName());
        recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, initiator.getName());
        if (Toggle.DISPLAYNAME.isEnabled()) {
            initiator.sendMessage(Resolvers.getInstance().parseMessagePlayerDisplayName(Message.SENDING_FORMAT.getMessage(), initiator, recipient, messageContent));
            recipient.sendMessage(Resolvers.getInstance().parseMessagePlayerDisplayName(Message.RECEIVING_FORMAT.getMessage(), initiator, recipient, messageContent));
            for (Player spy : spyingPlayers) {
                if (!spy.isOnline()) continue;
                if (spy.equals(initiator) || spy.equals(recipient)) continue;
                spy.sendMessage(Resolvers.getInstance().parseMessagePlayerDisplayName(Message.SPY_FORMAT.getMessage(), initiator, recipient, messageContent));
            }
        } else {
            initiator.sendMessage(Resolvers.getInstance().parseMessagePlayerUsername(Message.SENDING_FORMAT.getMessage(), initiator, recipient, messageContent));
            recipient.sendMessage(Resolvers.getInstance().parseMessagePlayerUsername(Message.RECEIVING_FORMAT.getMessage(), initiator, recipient, messageContent));
            for (Player spy : spyingPlayers) {
                if (!spy.isOnline()) continue;
                if (spy.equals(initiator) || spy.equals(recipient)) continue;
                spy.sendMessage(Resolvers.getInstance().parseMessagePlayerUsername(Message.SPY_FORMAT.getMessage(), initiator, recipient, messageContent));
            }
        }
    }

    public void consoleSender(CommandSender initiator, Player recipient, String messageContent) {
        PrivateMessageEvent event = new PrivateMessageEvent(initiator, recipient, messageContent, spyingPlayers);
        Bukkit.getServer().getPluginManager().callEvent(event);
        Component initiatorComponent = miniMessage.deserialize(Message.CONSOLE_FORMAT.getMessage());
        recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, Message.PDC_CONSOLE.getMessage());
        if (Toggle.DISPLAYNAME.isEnabled()){
            recipient.sendMessage(Resolvers.getInstance().parseMessageConsolePlayerDisplayName(Message.RECEIVING_FORMAT.getMessage(), initiatorComponent, recipient, messageContent));
            initiator.sendMessage(Resolvers.getInstance().parseMessageConsolePlayerDisplayName(Message.SENDING_FORMAT.getMessage(), initiatorComponent, recipient, messageContent));
            for (Player spy : spyingPlayers) {
                if (!spy.isOnline()) continue;
                if (spy.equals(initiator) || spy.equals(recipient)) continue;
                if (!spy.hasPermission(Perms.CONSOLE_MESSAGE_SPY.getPerm())) continue;
                spy.sendMessage(Resolvers.getInstance().parseMessageConsolePlayerDisplayName(Message.SPY_FORMAT.getMessage(), initiatorComponent, recipient, messageContent));
            }
        } else {
            recipient.sendMessage(Resolvers.getInstance().parseMessageConsolePlayerUsername(Message.RECEIVING_FORMAT.getMessage(), initiatorComponent, recipient, messageContent));
            initiator.sendMessage(Resolvers.getInstance().parseMessageConsolePlayerUsername(Message.SENDING_FORMAT.getMessage(), initiatorComponent, recipient, messageContent));
            for (Player spy : spyingPlayers) {
                if (!spy.isOnline()) continue;
                if (spy.equals(initiator) || spy.equals(recipient)) continue;
                if (!spy.hasPermission(Perms.CONSOLE_MESSAGE_SPY.getPerm())) continue;
                spy.sendMessage(Resolvers.getInstance().parseMessageConsolePlayerUsername(Message.SPY_FORMAT.getMessage(), initiatorComponent, recipient, messageContent));
            }
        }
    }


    public void consoleReciever(CommandSender initiator) {

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return new ArrayList<>();
        return null;
    }
}
