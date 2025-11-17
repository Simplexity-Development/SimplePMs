package simplexity.simplepms.paper.commands.arguments;

import org.bukkit.command.CommandSender;

public record Target(CommandSender sender, String providedName) {
}
