package simplexity.simplepms.paper.logic;

import com.simplexity.simplepms.common.logic.LocaleKey;
import com.simplexity.simplepms.common.logic.PlatformAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplepms.paper.config.LocaleMessage;

import java.util.UUID;

public class PaperPlatformAdapter implements PlatformAdapter {
    @Override
    public String getPlayerName(@NotNull UUID playerUUID) {
        return Bukkit.getOfflinePlayer(playerUUID).getName();
    }

    @Override
    public @Nullable String getLocaleString(@NotNull LocaleKey key) {
        return switch (key) {
            case ERROR_NAME_NOT_FOUND -> LocaleMessage.ERROR_NAME_NOT_FOUND.getMessage();
            default -> null;
        };
    }


}
