package simplexity.simplepms.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.LocaleHandler;

public class Util {
    private static Util instance;
    private final MiniMessage miniMessage = SimplePMs.getMiniMessage();

    private Util(){}

    public static Util getInstance() {
        if (instance == null) instance = new Util();
        return instance;
    }

    public Component parseMessage(String localeMessage, @NotNull CommandSender initiator,
                                         @NotNull CommandSender target, String messageContent,
                                         boolean socialSpy) {
        Component senderName = getCommmandSenderComponent(initiator, socialSpy);
        Component otherSenderName = getCommmandSenderComponent(target, socialSpy);
        Player initiatingPlayer = getPlayerFromCommandSender(initiator);
        Player targetPlayer = getPlayerFromCommandSender(target);
        if (SimplePMs.isPapiEnabled()) {
            TagResolver initiatorResolver = papiTag(initiatingPlayer);
            TagResolver targetResolver = papiTag(targetPlayer);
            return miniMessage.deserialize(localeMessage,
                    Placeholder.component("initiator", senderName),
                    initiatorResolver,
                    Placeholder.component("target", otherSenderName),
                    targetResolver,
                    Placeholder.unparsed("message", messageContent));
        } else {
            return miniMessage.deserialize(localeMessage,
                    Placeholder.component("initiator", senderName),
                    Placeholder.component("target", otherSenderName),
                    Placeholder.unparsed("message", messageContent));
        }
    }

    private Component getCommmandSenderComponent(CommandSender sender, boolean socialSpy) {
        if (!(sender instanceof Player player)) {
            if (socialSpy) return miniMessage.deserialize(LocaleHandler.Message.CONSOLE_NAME_SOCIAL_SPY.getMessage());
            return miniMessage.deserialize(LocaleHandler.Message.CONSOLE_SENDER_NAME.getMessage());
        }
        return player.displayName();
    }

    public Player getPlayerFromCommandSender(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return null;
        }
        return player;
    }

    public TagResolver papiTag(final Player player) {
        if (player == null) return TagResolver.empty();
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            final String papiPlaceholder = argumentQueue.popOr("PLACEHOLDER API NEEDS ARGUMENT").value();
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            final Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
            return Tag.inserting(componentPlaceholder);
        });
    }

    public Player getPlayer(String name) {
        Player player;
        player = SimplePMs.getInstance().getServer().getPlayer(name);
        if (player != null) {
            return player;
        }
        for (Player listedPlayer : SimplePMs.getPlayers()) {
            String listedPlayerPlainName = PlainTextComponentSerializer.plainText().serialize(listedPlayer.displayName());
            if (listedPlayerPlainName.equalsIgnoreCase(name)) {
                return listedPlayer;
            }
        }
        return null;
    }
}
