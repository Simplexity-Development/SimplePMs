package adhdmc.simplepms.commands;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.utils.Message;
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
import java.util.List;
import java.util.stream.Collectors;

public class PrivateMessage implements CommandExecutor, TabCompleter {
    private final NamespacedKey lastMessaged = Util.lastMessaged;
    private final String console = Util.console;

    MiniMessage miniMessage = SimplePMs.getMiniMessage();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(miniMessage.deserialize(Message.ERROR_NO_RECIPIENT_PROVIDED.getMessage(),
                    Placeholder.parsed("prefix", Message.PLUGIN_PREFIX.getMessage())));
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(miniMessage.deserialize(Message.ERROR_BLANK_MESSAGE.getMessage(),
                    Placeholder.parsed("prefix", Message.PLUGIN_PREFIX.getMessage())));
            return true;
        }
        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null) {
            sender.sendMessage(miniMessage.deserialize(Message.ERROR_RECIPIENT_OFFLINE.getMessage(),
                    Placeholder.parsed("prefix", Message.PLUGIN_PREFIX.getMessage()),
                    Placeholder.parsed("receiver", args[0])));
            return true;
        }
        // TODO: Implement ignore-list checker.
        if (false) {
            sender.sendMessage(miniMessage.deserialize(Message.ERROR_RECIPIENT_BLOCKED.getMessage(),
                    Placeholder.parsed("prefix", Message.PLUGIN_PREFIX.getMessage())));
            return true;
        }
        Component senderName;
        if (sender instanceof Player player){
            player.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, recipient.getName());
            recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, player.getName());
            senderName = player.displayName();
        } else {
            recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, console);
            senderName = miniMessage.deserialize(Message.CONSOLE_FORMAT.getMessage());
        }
        String message = String.join(" ", Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
        // TODO: Implement message event in place of this.
        sender.sendMessage(miniMessage.deserialize(Message.SENDING_FORMAT.getMessage(),
                Placeholder.component("receiver", recipient.displayName()),
                Placeholder.parsed("message", message)));
        recipient.sendMessage(miniMessage.deserialize(Message.RECEIVING_FORMAT.getMessage(),
                Placeholder.component("sender", senderName),
                Placeholder.parsed("message", message)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return new ArrayList<>();
        return null;
    }
}
