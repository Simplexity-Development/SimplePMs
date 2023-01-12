package adhdmc.simplepms.commands;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.utils.SPMKey;
import adhdmc.simplepms.utils.SPMMessage;
import adhdmc.simplepms.utils.Util;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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

public class ReplyCommand implements CommandExecutor, TabCompleter {
    private final HashSet<Player> spyingPlayers = Util.getSpyingPlayers();
    private final NamespacedKey lastMessaged = SPMKey.LAST_MESSAGED.getKey();
    private final String console = Util.console;
    private final Server server = SimplePMs.getInstance().getServer();
    private final MiniMessage miniMessage = SimplePMs.getMiniMessage();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(miniMessage.deserialize(SPMMessage.ERROR_PLAYER_COMMAND.getMessage(),
                    Placeholder.parsed("plugin_prefix", SPMMessage.PLUGIN_PREFIX.getMessage())));
            return true;
        }
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        String lastUserMessaged = playerPDC.get(lastMessaged, PersistentDataType.STRING);
        if (lastUserMessaged == null) {
            player.sendRichMessage(SPMMessage.NO_USER_TO_REPLY.getMessage());
            return false;
        }
        Player recipient = server.getPlayer(lastUserMessaged);
        if ((recipient == null) && !lastUserMessaged.equals(console)) {
           player.sendRichMessage(SPMMessage.NO_USER_TO_REPLY.getMessage());
           return false;
        }
        Component recipientName;
        Component senderSpyName;
        Component recieverSpyName = miniMessage.deserialize("<gray>" + recipient.getName());
        if (lastUserMessaged.equals(console)){
            recipientName = miniMessage.deserialize(SPMMessage.CONSOLE_FORMAT.getMessage());
            playerPDC.set(lastMessaged, PersistentDataType.STRING, console);
            senderSpyName = miniMessage.deserialize(SPMMessage.CONSOLE_FORMAT_SPY.getMessage());
        } else {
            recipientName = recipient.displayName();
            playerPDC.set(lastMessaged, PersistentDataType.STRING, recipient.getName());
            recipient.getPersistentDataContainer().set(lastMessaged, PersistentDataType.STRING, player.getName());
            senderSpyName = miniMessage.deserialize("<gray>" + player.getName());
        }
        String message = String.join(" ", Arrays.stream(args).skip(0).collect(Collectors.joining(" ")));
        // TODO: Implement message event in place of this.
        for (Player spy : spyingPlayers) {
            if (!spy.isOnline()) continue;
            if (spy.equals(sender) || spy.equals(recipient)) continue;
            spy.sendMessage(miniMessage.deserialize(SPMMessage.SPY_FORMAT.getMessage(),
                    Placeholder.component("sender", senderSpyName),
                    Placeholder.component("receiver", recieverSpyName),
                    Placeholder.unparsed("message", message)));
        }
        sender.sendMessage(miniMessage.deserialize(SPMMessage.SENDING_FORMAT.getMessage(),
                Placeholder.component("receiver", recipientName),
                Placeholder.parsed("message", message)));
        if (recipient == null) {
            Audience console = server.getConsoleSender();
            console.sendMessage(miniMessage.deserialize(SPMMessage.RECEIVING_FORMAT.getMessage(),
                    Placeholder.component("sender", player.displayName()),
                    Placeholder.unparsed("message", message)));
            return true;
        }
        recipient.sendMessage(miniMessage.deserialize(SPMMessage.RECEIVING_FORMAT.getMessage(),
                Placeholder.component("sender", player.displayName()),
                Placeholder.unparsed("message", message)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
