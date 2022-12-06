package adhdmc.simplepms.commands;

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
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("[Placeholder] You need to tell me who to send it to.");
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage("[Placeholder] You need to tell the player something.");
            return true;
        }
        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null) {
            sender.sendMessage("[Placeholder] This person is not online right now.");
            return true;
        }
        // TODO: Implement ignore-list checker.
        if (false) {
            sender.sendMessage("[Placeholder] This person is not available right now.");
            return true;
        }
        String message = String.join(" ", Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
        // TODO: Implement message event in place of this.
        sender.sendMessage("[To " + recipient.getName() + "] " + message);
        recipient.sendMessage("[From " + sender.getName() + "] " + message);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return new ArrayList<>();
        return null;
    }
}
