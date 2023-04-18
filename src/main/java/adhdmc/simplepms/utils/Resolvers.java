package adhdmc.simplepms.utils;

import adhdmc.simplepms.SimplePMs;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class Resolvers {
    private static Resolvers instance;
    private Resolvers(){}

    public static Resolvers getInstance() {
        if (instance == null) instance = new Resolvers();
        return instance;
    }

    MiniMessage miniMessage = SimplePMs.getMiniMessage();


    //Credit: https://docs.advntr.dev/faq.html#how-can-i-use-bukkits-placeholderapi-in-minimessage-messages
    /**
     * Creates a tag resolver capable of resolving PlaceholderAPI tags for a given player.
     *
     * @param player the player
     * @return the tag resolver
     */
    public @NotNull TagResolver papiTag(final @NotNull Player player) {
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            final String papiPlaceholder = argumentQueue.popOr(Message.ERROR_PAPI_NEEDS_ARGUMENT.getMessage()).value();
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            final Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
            return Tag.selfClosingInserting(componentPlaceholder);
        });
    }

    public Component parseMessagePlayerDisplayName(String message, Player initiator, Player target, String messageContent){
        return miniMessage.deserialize(message,
                Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                Placeholder.component("target", target.displayName()),
                Placeholder.component("initiator", initiator.displayName()),
                Placeholder.unparsed("message", messageContent),
                papiTag(initiator),
                papiTag(target));
    }

    public Component parseMessagePlayerUsername(String message, Player initiator, Player target, String messageContent){
        return miniMessage.deserialize(message,
                Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                Placeholder.parsed("target", target.getName()),
                Placeholder.parsed("initiator", initiator.getName()),
                Placeholder.unparsed("message", messageContent),
                papiTag(initiator),
                papiTag(target));
    }

    public Component parseMessageConsolePlayerDisplayName(String message, Component initiator, Player target, String messageContent){
        return miniMessage.deserialize(message,
                Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                Placeholder.component("target", target.displayName()),
                Placeholder.component("initiator", initiator),
                Placeholder.unparsed("message", messageContent),
                papiTag(target));
    }

    public Component parseMessageConsolePlayerUsername(String message, Component initiator, Player target, String messageContent){
        return miniMessage.deserialize(message,
                Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                Placeholder.parsed("target", target.getName()),
                Placeholder.component("initiator", initiator),
                Placeholder.unparsed("message", messageContent),
                papiTag(target));
    }

    public Component parsePluginPrefix(String message) {
        return miniMessage.deserialize(message,
                Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()));
    }

    public Component parsePluginPrefixAndString(String message,String placeholderName, String string) {
        return miniMessage.deserialize(message,
                Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                Placeholder.parsed(placeholderName, string));
    }
}
