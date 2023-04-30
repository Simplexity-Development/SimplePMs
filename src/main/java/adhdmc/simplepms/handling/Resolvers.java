package adhdmc.simplepms.handling;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.utils.Message;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Resolvers {
    private static Resolvers instance;
    private Resolvers(){}

    public static Resolvers getInstance() {
        if (instance == null) instance = new Resolvers();
        return instance;
    }
    boolean papiEnabled = SimplePMs.isPapiEnabled();

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


    /**
     * Parses a message between 2 players. Will parse PAPI placeholders if placeholderAPI is on the server
     * Uses the default placeholders:
     * <ul><li>{@code <plugin_prefix>}
     * <li>{@code <target>}
     * <li>{@code <initiator>}
     * <li>{@code <message>}</ul>
     * @param message String
     * @param initiator Player
     * @param target Player
     * @param messageContent String
     * @return Component
     */
    public Component parseMessagePlayerToPlayer(String message, Player initiator, Player target, String messageContent){
        if (papiEnabled) {
            return miniMessage.deserialize(message,
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                    Placeholder.component("target", target.displayName()),
                    Placeholder.component("initiator", initiator.displayName()),
                    Placeholder.unparsed("message", messageContent),
                    papiTag(initiator),
                    papiTag(target));
        } else {
            return miniMessage.deserialize(message,
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                    Placeholder.component("target", target.displayName()),
                    Placeholder.component("initiator", initiator.displayName()),
                    Placeholder.unparsed("message", messageContent));
        }
    }

    /**
     * Parses a message from the console to a player. Will parse PAPI placeholders if placeholderAPI is on the server
     * Uses the default placeholders:
     * <ul><li>{@code <plugin_prefix>}
     * <li>{@code <target>}
     * <li>{@code <initiator>}
     * <li>{@code <message>}</ul>
     * @param message String
     * @param initiator Component
     * @param target Player
     * @param messageContent String
     * @return Component
     */

    public Component parseMessageConsoleToPlayer(String message, Component initiator, Player target, String messageContent){
        if (papiEnabled) {
            return miniMessage.deserialize(message,
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                    Placeholder.component("target", target.displayName()),
                    Placeholder.component("initiator", initiator),
                    Placeholder.unparsed("message", messageContent),
                    papiTag(target));
        } else {
            return miniMessage.deserialize(message,
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                    Placeholder.component("target", target.displayName()),
                    Placeholder.component("initiator", initiator),
                    Placeholder.unparsed("message", messageContent));
        }
    }

    /**
     * Parses a message from a Player to the Console. Will parse PAPI placeholders if placeholderAPI is on the server
     * Uses the default placeholders:
     * <ul><li>{@code <plugin_prefix>}
     * <li>{@code <target>}
     * <li>{@code <initiator>}
     * <li>{@code <message>}</ul>
     * @param message String
     * @param initiator Player
     * @param target Component
     * @param messageContent String
     * @return Component
     */

    public Component parseMessagePlayerToConsole(String message, Player initiator, Component target, String messageContent){
        if(papiEnabled) {
            return miniMessage.deserialize(message,
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                    Placeholder.component("target", target),
                    Placeholder.component("initiator", initiator.displayName()),
                    Placeholder.unparsed("message", messageContent),
                    papiTag(initiator));
        } else {
            return miniMessage.deserialize(message,
                    Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                    Placeholder.component("target", target),
                    Placeholder.component("initiator", initiator.displayName()),
                    Placeholder.unparsed("message", messageContent));
        }
    }

    /**
     * Parses the placeholder for the plugin prefix from the given message
     * Uses the default placeholders:
     * <ul><li>{@code <plugin_prefix>}</ul>
     * @param message String
     * @return Component
     */
    public Component parsePluginPrefix(String message) {
        return miniMessage.deserialize(message,
                Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()));
    }

    /**
     * Parses the plugin prefix and an additional placeholder, using the provided 'placeholderName' and 'string'
     * Uses the default placeholders:
     * <ul><li>{@code <plugin_prefix>}</ul>
     * @param message String
     * @param placeholderName String
     * @param string String
     * @return Component
     */
    public Component parsePluginPrefixAndString(String message,String placeholderName, String string) {
        return miniMessage.deserialize(message,
                Placeholder.parsed("plugin_prefix", Message.PLUGIN_PREFIX.getMessage()),
                Placeholder.parsed(placeholderName, string));
    }
}
