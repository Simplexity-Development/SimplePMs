package simplexity.simplepms.commands;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.LocaleMessage;

@SuppressWarnings("UnstableApiUsage")
public class Exceptions {

    private static final MiniMessage miniMessage = SimplePMs.getMiniMessage();

    public static final SimpleCommandExceptionType ERROR_EMPTY_MESSAGE = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    miniMessage.deserialize(
                            LocaleMessage.ERROR_BLANK_MESSAGE.getMessage()
                    )
            )
    );

    public static final SimpleCommandExceptionType ERROR_NO_PERMISSION = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    miniMessage.deserialize(
                            LocaleMessage.ERROR_NO_PERMISSION.getMessage()
                    )
            )
    );

    public static final DynamicCommandExceptionType ERROR_INVALID_USER = new DynamicCommandExceptionType(input -> {
        return MessageComponentSerializer.message().serialize(
                miniMessage.deserialize(
                        LocaleMessage.ERROR_RECIPIENT_NOT_EXIST.getMessage(),
                        Placeholder.unparsed("name", input.toString())
                )
        );
    });

    public static final SimpleCommandExceptionType ERROR_YOUR_MESSAGES_ARE_DISABLED = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    miniMessage.deserialize(
                            LocaleMessage.ERROR_YOUR_MESSAGES_CURRENTLY_DISABLED.getMessage()
                    )
            )
    );

    public static final SimpleCommandExceptionType ERROR_TARGET_CANNOT_RECEIVE_MESSAGE = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    miniMessage.deserialize(
                            LocaleMessage.ERROR_TARGET_CANNOT_RECIEVE_MESSAGE.getMessage()
                    )
            )
    );

    public static final SimpleCommandExceptionType ERROR_CANNOT_MESSAGE_SOMEONE_YOU_HAVE_BLOCKED = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    miniMessage.deserialize(
                            LocaleMessage.ERROR_CANNOT_MESSAGE_SOMEONE_YOU_BLOCKED.getMessage()
                    )
            )
    );

    public static final SimpleCommandExceptionType ERROR_NOBODY_TO_REPLY_TO = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    miniMessage.deserialize(
                            LocaleMessage.ERROR_CANNOT_REPLY.getMessage()
                    )
            )
    );

    public static final SimpleCommandExceptionType ERROR_MUST_BE_PLAYER = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    miniMessage.deserialize(
                            LocaleMessage.ERROR_NOT_A_PLAYER.getMessage()
                    )
            )
    );

    public static final SimpleCommandExceptionType ERROR_NOT_BLOCKING_ANYONE_BY_THIS_NAME = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    miniMessage.deserialize(
                            LocaleMessage.PLAYER_NOT_BLOCKED.getMessage()
                    )
            )
    );

}
