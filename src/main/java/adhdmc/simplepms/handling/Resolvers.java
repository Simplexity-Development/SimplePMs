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
