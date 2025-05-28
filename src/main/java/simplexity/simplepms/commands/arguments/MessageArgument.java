package simplexity.simplepms.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.commands.Exceptions;

@SuppressWarnings("UnstableApiUsage")
public class MessageArgument implements CustomArgumentType<String, String> {
    @Override
    public @NotNull String parse(@NotNull StringReader stringReader) throws CommandSyntaxException {
        String message = stringReader.getRemaining();
        if (message.isEmpty()) throw Exceptions.ERROR_EMPTY_MESSAGE.create();
        return message;
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.greedyString();
    }
}
