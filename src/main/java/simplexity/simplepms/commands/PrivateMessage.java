package simplexity.simplepms.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.LocaleHandler;
import simplexity.simplepms.events.PrivateMessageEvent;
import simplexity.simplepms.handling.MessageHandling;
import simplexity.simplepms.objects.PlayerBlock;
import simplexity.simplepms.objects.PlayerSettings;
import simplexity.simplepms.saving.SQLHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PrivateMessage implements TabExecutor {

    private static final String RECEIVE_PERMISSION = "message.basic.receive";
    private static final String ADMIN_OVERRIDE = "message.admin.override";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendRichMessage(LocaleHandler.Message.NO_RECIPIENT_PROVIDED.getMessage());
            return false;
        }
        if (args.length < 2) {
            sender.sendRichMessage(LocaleHandler.Message.BLANK_MESSAGE.getMessage());
            return false;
        }
        Player receivingPlayer = Util.getPlayer(args[0]);
        if (receivingPlayer == null) {
            sender.sendRichMessage(LocaleHandler.Message.RECIPIENT_NOT_EXIST.getMessage(),
                    Placeholder.unparsed("name", args[0]));
            return false;
        }
        if (!canMessageTarget(sender, receivingPlayer)) {
            return false;
        }
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        PrivateMessageEvent test = new PrivateMessageEvent(sender, receivingPlayer, message, SimplePMs.getSpyingPlayers());
        Bukkit.getServer().getPluginManager().callEvent(test);
        if (test.isCancelled()) {
            return false;
        }
        sender.sendRichMessage(LocaleHandler.Message.MESSAGE_SENT.getMessage(),
                Placeholder.parsed("target", receivingPlayer.getName()),
                Placeholder.parsed("message", message));
        receivingPlayer.sendRichMessage(LocaleHandler.Message.MESSAGE_RECEIVED.getMessage(),
                Placeholder.parsed("initiator", sender.getName()),
                Placeholder.parsed("message", message));
        return true;

    }

    private boolean canMessageTarget(CommandSender sender, Player target) {
        if (!(sender instanceof Player initiator)) {
            handleConsoleSender(sender, target);
            return false;
        }
        if (initiator.hasPermission(ADMIN_OVERRIDE)) {
            return true;
        }
        if (!initiator.canSee(target)) {
            initiator.sendRichMessage(LocaleHandler.Message.RECIPIENT_NOT_EXIST.getMessage(),
                    Placeholder.unparsed("name", target.getName()));
            return false;
        }
        if (messagesDisabled(initiator)) {
            initiator.sendRichMessage(LocaleHandler.Message.YOUR_MESSAGES_CURRENTLY_DISABLED.getMessage());
            return false;
        }
        if (messagesDisabled(target) || !target.hasPermission(RECEIVE_PERMISSION) || userBlocked(target, initiator)) {
            initiator.sendRichMessage(LocaleHandler.Message.TARGET_CANNOT_RECIEVE_MESSAGE.getMessage());
            return false;
        }
        if (userBlocked(initiator, target)) {
            initiator.sendRichMessage(LocaleHandler.Message.CANNOT_MESSAGE_SOMEONE_YOU_BLOCKED.getMessage());
            return false;
        }
        return true;
    }

    private boolean messagesDisabled(Player player) {
        PlayerSettings playerSettings = SQLHandler.playerSettings.get(player.getUniqueId());
        if (playerSettings == null) {
            return false;
        }
        return !playerSettings.messagesEnabled();
    }

    private boolean userBlocked(Player player1, Player player2) {
        List<PlayerBlock> playerBlocks = SQLHandler.blockList.get(player1.getUniqueId());
        if (playerBlocks == null) {
            return false;
        }
        for (PlayerBlock playerBlock : playerBlocks) {
            if (playerBlock.blockedPlayerUUID().equals(player2.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    private void handleConsoleSender(CommandSender sender, Player target) {
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return List.of();
        return null;
    }
}
