package simplexity.simplepms.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import simplexity.simplepms.config.LocaleMessage;
import simplexity.simplepms.logic.Constants;
import simplexity.simplepms.saving.Cache;
import simplexity.simplepms.saving.objects.PlayerSettings;

import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class MessageToggle {

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("msgtoggle")
                .requires(MessageToggle::canExecute)
                .executes(ctx -> {
                    Player sender = (Player) ctx.getSource().getSender();
                    UUID playerUuid = sender.getUniqueId();
                    PlayerSettings playerSettings = Cache.getPlayerSettings(playerUuid);
                    if (playerSettings.areMessagesDisabled()) {
                        Cache.updateMessageSettings(playerUuid, false);
                        sender.sendRichMessage(LocaleMessage.MESSAGES_ENABLED.getMessage());
                        return Command.SINGLE_SUCCESS;
                    }
                    Cache.updateMessageSettings(playerUuid, true);
                    sender.sendRichMessage(LocaleMessage.MESSAGES_DISABLED.getMessage());
                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    public static boolean canExecute(CommandSourceStack css) {
        if (!(css.getSender() instanceof Player)) return false;
        return css.getSender().hasPermission(Constants.MESSAGE_TOGGLE);
    }
}
