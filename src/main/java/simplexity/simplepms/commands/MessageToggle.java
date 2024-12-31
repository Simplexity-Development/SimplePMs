package simplexity.simplepms.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.config.Message;
import simplexity.simplepms.objects.PlayerSettings;
import simplexity.simplepms.saving.SqlHandler;

import java.util.UUID;

public class MessageToggle implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage(Message.ONLY_PLAYER.getMessage());
            return false;
        }
        UUID uuid = player.getUniqueId();
        PlayerSettings playerSettings = SqlHandler.getInstance().getSettings(uuid);
        if (playerSettings.messagesDisabled()) {
            SqlHandler.getInstance().setMessagesDisabled(uuid, false);
            player.sendRichMessage(Message.MESSAGES_ENABLED.getMessage());
            return true;
        }
        SqlHandler.getInstance().setMessagesDisabled(uuid, true);
        player.sendRichMessage(Message.MESSAGES_DISABLED.getMessage());
        return true;
    }
}
