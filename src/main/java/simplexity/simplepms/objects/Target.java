package simplexity.simplepms.objects;

import org.bukkit.command.CommandSender;

public record Target(CommandSender sender, String providedName) {
}
