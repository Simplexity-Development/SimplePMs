package simplexity.simplepms.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.commands.Exceptions;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.objects.Target;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class TargetArgument implements CustomArgumentType<Target, String> {

    private static final CommandSender consoleSender = SimplePMs.getPMConsoleSender();

    @Override
    public @NotNull Target parse(@NotNull StringReader reader) throws CommandSyntaxException {
        String targetName = reader.readString();
        if (ConfigHandler.getInstance().getValidNamesForConsole().contains(targetName.toLowerCase())) return new Target(consoleSender, targetName);
        Player targetPlayer = Bukkit.getPlayerExact(targetName);
        if (targetPlayer != null) return new Target(targetPlayer, targetName);
        throw Exceptions.ERROR_INVALID_USER.create(targetName);
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }


    /**
     * Provides suggestions for players based on the online player list.
     * Will also provide a hover-able element that shows their current nickname.
     *
     * @param context Command Context
     * @param builder SuggestionsBuilder object for adding suggestions to
     * @param <S>     For Paper, generally CommandSourceStack
     * @return Suggestions as a CompletableFuture
     */
    public <S> @NotNull CompletableFuture<Suggestions> suggestOnlinePlayers(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        if (!(context.getSource() instanceof CommandSourceStack css)) return builder.buildFuture();
        CommandSender sender = css.getSender();
        Player playerSender = null;
        if (sender instanceof Player) playerSender = (Player) sender;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playerSender != null) {
                if (!playerSender.canSee(player) &&
                    !ConfigHandler.getInstance().canPlayersSendToHiddenPlayers()) continue;
            }
            String suggestion = player.getName();
            if (suggestion.toLowerCase().contains(builder.getRemainingLowerCase())) {
                builder.suggest(
                        suggestion,
                        MessageComponentSerializer.message().serialize(
                                player.displayName()
                        )
                );
            }
        }
        return builder.buildFuture();
    }
}
