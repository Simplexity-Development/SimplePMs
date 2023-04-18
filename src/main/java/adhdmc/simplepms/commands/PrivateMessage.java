package adhdmc.simplepms.commands;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.utils.SPMKey;
import adhdmc.simplepms.utils.Message;
import adhdmc.simplepms.utils.SPMPerm;
import adhdmc.simplepms.utils.Util;
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
    private final HashSet<Player> spyingPlayers = Util.getSpyingPlayers();
    private final NamespacedKey lastMessaged = SPMKey.LAST_MESSAGED.getKey();
    private final String console = Util.console;

    MiniMessage miniMessage = SimplePMs.getMiniMessage();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(miniMessage.deserialize(Message.ERROR_NO_RECIPIENT_PROVIDED.getMessage(),
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage())));
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(miniMessage.deserialize(Message.ERROR_BLANK_MESSAGE.getMessage(),
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage())));
            return true;
        }
        if (!sender.hasPermission(SPMPerm.SEND_MESSAGE.getPerm())) {
            sender.sendMessage(miniMessage.deserialize(Message.ERROR_NO_PERMISSION.getMessage(),
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage())));
            return false;
        }
        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null) {
            sender.sendMessage(miniMessage.deserialize(Message.ERROR_RECIPIENT_OFFLINE.getMessage(),
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                    Placeholder.parsed("receiver", args[0])));
            return true;
        }
        // TODO: Implement ignore-list checker.
        if (false) {
            sender.sendMessage(miniMessage.deserialize(Message.ERROR_RECIPIENT_BLOCKED.getMessage(),
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage())));
            return true;
        }
        Component senderName;
        Component senderSpyName;
        Component recieverSpyName = miniMessage.deserialize("<gray>" + recipient.getName());
        if (sender instanceof Player player){
            player.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, recipient.getName());
            recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, player.getName());
            senderName = player.displayName();
            senderSpyName = miniMessage.deserialize("<gray>" + player.getName());
        } else {
            recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, console);
            senderName = miniMessage.deserialize(Message.CONSOLE_FORMAT.getMessage());
            senderSpyName = miniMessage.deserialize(Message.CONSOLE_FORMAT_SPY.getMessage());
        }
        String message = String.join(" ", Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
        // TODO: Implement message event in place of this.
        for (Player spy : spyingPlayers) {
            if (!spy.isOnline()) continue;
            if (spy.equals(sender) || spy.equals(recipient)) continue;
            spy.sendMessage(miniMessage.deserialize(Message.SPY_FORMAT.getMessage(),
                    Placeholder.component("sender", senderSpyName),
                    Placeholder.component("receiver", recieverSpyName),
                    Placeholder.unparsed("message", message)));
        }
        sender.sendMessage(miniMessage.deserialize(Message.SENDING_FORMAT.getMessage(),
                Placeholder.component("receiver", recipient.displayName()),
                Placeholder.unparsed("message", message)));
        recipient.sendMessage(miniMessage.deserialize(Message.RECEIVING_FORMAT.getMessage(),
                Placeholder.component("sender", senderName),
                Placeholder.unparsed("message", message)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return new ArrayList<>();
        return null;
    }
}
