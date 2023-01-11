package adhdmc.simplepms.commands;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.utils.SPMKey;
import adhdmc.simplepms.utils.SPMMessage;
import adhdmc.simplepms.utils.SPMPerm;
import adhdmc.simplepms.utils.Util;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class SocialSpyCommand implements CommandExecutor, TabCompleter {

    private final HashSet<Player> spyingPlayers = Util.getSpyingPlayers();
    private final NamespacedKey spyToggle = SPMKey.SPY_TOGGLE.getKey();
    private final MiniMessage miniMessage = SimplePMs.getMiniMessage();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //Check permissions
        if (!sender.hasPermission(SPMPerm.SOCIAL_SPY_TOGGLE.getPerm())) {
            sender.sendMessage(miniMessage.deserialize(SPMMessage.ERROR_NO_PERMISSION.getMessage(),
                    Placeholder.parsed("prefix", SPMMessage.PLUGIN_PREFIX.getMessage())));
            return false;
        }
        //Console cannot toggle social spy
        if (!(sender instanceof Player player)) {
            sender.sendMessage(miniMessage.deserialize(SPMMessage.ERROR_PLAYER_COMMAND.getMessage(),
                    Placeholder.parsed("prefix", SPMMessage.PLUGIN_PREFIX.getMessage())));
            return false;
        }
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        byte toggleValue = playerPDC.getOrDefault(spyToggle, PersistentDataType.BYTE, (byte)0);
        //If social spy is not enabled, set enabled, add player to recieving set message the player and return
        if (toggleValue == (byte)0) {
            playerPDC.set(spyToggle, PersistentDataType.BYTE, (byte)1);
            spyingPlayers.add(player);
            player.sendMessage(miniMessage.deserialize(SPMMessage.SPY_ENABLED.getMessage()));
            return true;
        }
        //If social spy was enabled, disable, remove player from the set if they are there, message the player, and return
        if (toggleValue == (byte)1) {
            playerPDC.set(spyToggle, PersistentDataType.BYTE, (byte) 0);
            spyingPlayers.remove(player);
            player.sendMessage(miniMessage.deserialize(SPMMessage.SPY_DISABLED.getMessage()));
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
