package adhdmc.simplepms.commands;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.utils.SPMKey;
import adhdmc.simplepms.utils.Message;
import adhdmc.simplepms.utils.Resolvers;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
    private final HashSet<Player> spyingPlayers = SimplePMs.getSpyingPlayers();
    private final NamespacedKey lastMessaged = SPMKey.LAST_MESSAGED.getKey();
    private final Server server = SimplePMs.getInstance().getServer();
    private final MiniMessage miniMessage = SimplePMs.getMiniMessage();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Resolvers.getInstance().parsePluginPrefix(Message.ERROR_PLAYER_COMMAND.getMessage()));
            return false;
        }
        String message = String.join(" ", Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        String lastMessagedPlayer = playerPDC.getOrDefault(lastMessaged, PersistentDataType.STRING, "");
        if (lastMessagedPlayer.equalsIgnoreCase(Message.PDC_CONSOLE.getMessage())) {

        }
        Player recipient = Bukkit.getServer().getPlayer(lastMessagedPlayer);
        if (recipient == null) {
            player.sendMessage(Resolvers.getInstance().parsePluginPrefixAndString(Message.ERROR_RECIPIENT_OFFLINE.getMessage(), "receiver", lastMessagedPlayer));
            return false;
        }

        return true;

    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
