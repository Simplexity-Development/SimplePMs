package simplexity.simplepms.commands.arguments;

import org.bukkit.command.CommandSender;

public record Target(CommandSender sender, String providedName) {
}
