package adhdmc.simplepms.commands;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.handling.MessageHandling;
import adhdmc.simplepms.utils.SPMKey;
import adhdmc.simplepms.utils.Message;
import adhdmc.simplepms.handling.Resolvers;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ReplyCommand implements TabExecutor {
    private final NamespacedKey lastMessaged = SPMKey.LAST_MESSAGED.getKey();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player playerInitiator)) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_PLAYER_COMMAND.getMessage()));
            return false;
        }
        PersistentDataContainer playerPDC = playerInitiator.getPersistentDataContainer();
        String lastMessagedPlayer = playerPDC.getOrDefault(lastMessaged, PersistentDataType.STRING, "");
        String message = String.join(" ", Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
        if (lastMessagedPlayer.equalsIgnoreCase(Message.PDC_CONSOLE.getMessage())) {
            MessageHandling.getInstance().playerSenderConsoleReceiver(playerInitiator, message);
            return true;
        }
        Player recipient = Bukkit.getServer().getPlayer(lastMessagedPlayer);
        if (recipient == null) {
            playerInitiator.sendMessage(Resolvers.getInstance().parsePluginPrefixAndString(Message.ERROR_RECIPIENT_OFFLINE.getMessage(), "receiver", lastMessagedPlayer));
            return false;
        }
        if (!recipient.isOnline()) {
            playerInitiator.sendMessage(Resolvers.getInstance().parsePluginPrefixAndString(Message.ERROR_RECIPIENT_OFFLINE.getMessage(), "receiver", lastMessagedPlayer));
        }
        MessageHandling.getInstance().playerSenderAndReceiver(playerInitiator, recipient, message);
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
