package simplexity.simplepms.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.LocaleHandler;
import simplexity.simplepms.objects.PlayerSettings;
import simplexity.simplepms.saving.SQLHandler;

import java.util.UUID;

public class SocialSpy implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage(LocaleHandler.Message.ONLY_PLAYER.getMessage());
            return false;
        }
        UUID uuid = player.getUniqueId();
        PlayerSettings settings = SQLHandler.getInstance().getSettings(uuid);
        if (settings == null || settings.socialSpyEnabled()) {
            SQLHandler.getInstance().setSocialSpyEnabled(uuid, false);
            sender.sendRichMessage(LocaleHandler.Message.SOCIAL_SPY_DISABLED.getMessage());
            SimplePMs.getSpyingPlayers().remove(player);
            return true;
        }
        SQLHandler.getInstance().setSocialSpyEnabled(uuid, true);
        sender.sendRichMessage(LocaleHandler.Message.SOCIAL_SPY_ENABLED.getMessage());
        SimplePMs.getSpyingPlayers().add(player);
        return true;
    }

}
