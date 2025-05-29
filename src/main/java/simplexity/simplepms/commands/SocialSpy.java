package simplexity.simplepms.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.LocaleMessage;
import simplexity.simplepms.logic.Constants;
import simplexity.simplepms.saving.Cache;
import simplexity.simplepms.saving.SqlHandler;
import simplexity.simplepms.saving.objects.PlayerSettings;

import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class SocialSpy {

    public static LiteralCommandNode<CommandSourceStack> createCommand(){
        return Commands.literal("socialspy")
                .requires(SocialSpy::canExecute)
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    UUID uuid = player.getUniqueId();
                    PlayerSettings settings = SqlHandler.getInstance().getSettings(uuid);
                    if (settings == null || settings.isSocialSpyEnabled()) {
                        Cache.updateSocialSpySettings(uuid, false);
                        player.sendRichMessage(LocaleMessage.SOCIAL_SPY_DISABLED.getMessage());
                        SimplePMs.getSpyingPlayers().remove(player);
                        return Command.SINGLE_SUCCESS;
                    }
                    Cache.updateSocialSpySettings(uuid, true);
                    player.sendRichMessage(LocaleMessage.SOCIAL_SPY_ENABLED.getMessage());
                    SimplePMs.getSpyingPlayers().add(player);
                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    private static boolean canExecute(CommandSourceStack css){
        if (!(css.getSender() instanceof Player)) return false;
        return css.getSender().hasPermission(Constants.ADMIN_SOCIAL_SPY);
    }

}
