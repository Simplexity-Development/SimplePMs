package com.simplexity.simplepms.common.logic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PlatformAdapter {

    /**
     * Retrieve the player name based on the player's UUID.
     * @param playerUUID Player's UUID
     * @return Player's name
     */
    @Nullable String getPlayerName(@NotNull UUID playerUUID);

    /**
     * Retrieve a locale String from a given {@link LocaleKey}
     * @return String defined by the {@link LocaleKey}, null if not found.
     */
    @Nullable String getLocaleString(@NotNull LocaleKey key);

}
