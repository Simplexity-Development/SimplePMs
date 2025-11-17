package simplexity.simplepms.paper.commands.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplepms.paper.config.ConfigHandler;
import simplexity.simplepms.paper.logic.Constants;
import simplexity.simplepms.paper.saving.Cache;
import simplexity.simplepms.paper.saving.objects.PlayerBlock;
import simplexity.simplepms.paper.saving.objects.PlayerSettings;

import java.util.List;

public class MessageChecks {


    public static void userChecks(CommandSender initiator, CommandSender target, String providedName) throws CommandSyntaxException {
        if (initiator instanceof Player playerInitiator) {
            ownMessagesDisabledCheck(playerInitiator);
            if (target instanceof Player playerTarget) {
                initiatorBlockedTargetCheck(playerInitiator, playerTarget);
                if (!initiator.hasPermission(Constants.ADMIN_OVERRIDE)) {
                    targetCanGetMessageCheck(playerTarget);
                    targetBlockedInitiatorCheck(playerInitiator, playerTarget);
                    vanishCheck(playerInitiator, playerTarget, providedName);
                }
            } else {
                canSendToConsole(providedName);
            }
        }
    }

    private static void vanishCheck(Player initiator, Player target, String providedName) throws CommandSyntaxException {
        if (ConfigHandler.getInstance().canPlayersSendToHiddenPlayers()) return;
        if (!initiator.canSee(target)) throw Exceptions.ERROR_INVALID_USER.create(providedName);
    }

    private static void ownMessagesDisabledCheck(Player initiatingPlayer) throws CommandSyntaxException {
        PlayerSettings settings = Cache.getPlayerSettings(initiatingPlayer.getUniqueId());
        if (settings == null) return;
        if (settings.areMessagesDisabled()) throw Exceptions.ERROR_YOUR_MESSAGES_ARE_DISABLED.create();
    }

    private static void targetCanGetMessageCheck(Player targetPlayer) throws CommandSyntaxException {
        if (!targetPlayer.hasPermission(Constants.MESSAGE_RECEIVE))
            throw Exceptions.ERROR_TARGET_CANNOT_RECEIVE_MESSAGE.create();
        PlayerSettings settings = Cache.getPlayerSettings(targetPlayer.getUniqueId());
        if (settings == null) return;
        if (settings.areMessagesDisabled()) throw Exceptions.ERROR_TARGET_CANNOT_RECEIVE_MESSAGE.create();
    }

    private static void targetBlockedInitiatorCheck(Player initiatingPlayer, Player targetPlayer) throws CommandSyntaxException {
        if (userBlocked(targetPlayer, initiatingPlayer)) throw Exceptions.ERROR_TARGET_CANNOT_RECEIVE_MESSAGE.create();
    }

    private static void initiatorBlockedTargetCheck(Player initiatingPlayer, Player targetPlayer) throws CommandSyntaxException {
        if (userBlocked(initiatingPlayer, targetPlayer))
            throw Exceptions.ERROR_CANNOT_MESSAGE_SOMEONE_YOU_HAVE_BLOCKED.create();
    }

    private static void canSendToConsole(String providedName) throws CommandSyntaxException {
        if (!ConfigHandler.getInstance().canPlayersSendToConsole())
            throw Exceptions.ERROR_INVALID_USER.create(providedName);
    }

    private static boolean userBlocked(Player blocklistPlayer, Player potentialBlock) {
        List<PlayerBlock> playerBlocks = Cache.blockList.get(blocklistPlayer.getUniqueId());
        if (playerBlocks == null) {
            return false;
        }
        for (PlayerBlock playerBlock : playerBlocks) {
            if (playerBlock.getBlockedPlayerUUID().equals(potentialBlock.getUniqueId())) {
                return true;
            }
        }
        return false;
    }
}
