package com.simplexity.simplepms.common.database;

import com.simplexity.simplepms.common.logic.LocaleKey;
import com.simplexity.simplepms.common.logic.PlatformBridge;
import com.simplexity.simplepms.common.database.objects.PlayerBlock;
import com.simplexity.simplepms.common.database.objects.PlayerSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Cache {
    public static final HashMap<UUID, List<PlayerBlock>> blockList = new HashMap<>();
    public static final HashMap<UUID, PlayerSettings> playerSettings = new HashMap<>();
    public static final HashSet<UUID> spyingPlayers = new HashSet<>();

    public static List<PlayerBlock> getBlockList(UUID uuid) {
        return blockList.get(uuid);
    }

    public static PlayerSettings getPlayerSettings(UUID uuid) {
        return playerSettings.get(uuid);
    }

    public static void populateCache(UUID uuid, boolean hasSpyPerms) {
        List<PlayerBlock> blocklist = SQLHandler.getInstance().getBlockedPlayers(uuid);
        blockList.put(uuid, blocklist);
        populateNullNames(uuid);

        PlayerSettings settings = SQLHandler.getInstance().getSettings(uuid);
        playerSettings.put(uuid, settings);
        if (hasSpyPerms && settings.isSocialSpyEnabled()) {
            spyingPlayers.add(uuid);
        }
    }

    /**
     * Clears the user from the settings cache
     *
     * @param uuid Player's UUID
     */
    public static void removePlayerSettingsFromCache(UUID uuid) {
        playerSettings.remove(uuid);
    }

    /**
     * Clears the user from the blocklist cache
     *
     * @param uuid Player's UUID
     */
    public static void removeBlockListFromCache(UUID uuid) {
        playerSettings.remove(uuid);
    }

    /**
     * Updates the provided player's current social spy settings. Updates cache and then updates SQL
     *
     * @param uuid      UUID Player Uuid
     * @param socialSpy boolean New Setting
     */
    public static void updateSocialSpySettings(UUID uuid, boolean socialSpy) {
        PlayerSettings settings = playerSettings.get(uuid);
        settings.setSocialSpyEnabled(socialSpy);
        playerSettings.put(uuid, settings);
        SQLHandler.getInstance().updateSettings(uuid, settings.isSocialSpyEnabled(), settings.areMessagesDisabled());
    }

    /**
     * Updates the provided player's current message toggle state. Updates cache and then updates SQL
     *
     * @param uuid            UUID Player Uuid
     * @param messageDisabled boolean New Setting
     */
    public static void updateMessageSettings(UUID uuid, boolean messageDisabled) {
        PlayerSettings settings = playerSettings.get(uuid);
        settings.setMessagesDisabled(messageDisabled);
        playerSettings.put(uuid, settings);
        SQLHandler.getInstance().updateSettings(uuid, settings.isSocialSpyEnabled(), settings.areMessagesDisabled());
    }

    /**
     * Updates a player's blocklist by adding or updating a blocked player.
     *
     * @param uuid        UUID blocking player
     * @param playerBlock PlayerBlock block
     */

    public static void addBlockedUser(UUID uuid, PlayerBlock playerBlock) {
        removeCachedDuplicates(uuid, playerBlock.getBlockedPlayerUUID());
        List<PlayerBlock> blockedPlayers = blockList.get(uuid);
        blockedPlayers.add(playerBlock);
        blockList.put(uuid, blockedPlayers);
        SQLHandler.getInstance().addBlockedPlayer(uuid, playerBlock.getBlockedPlayerUUID(), playerBlock.getBlockedPlayerName(), playerBlock.getBlockReason());
    }

    /**
     * Removes a blocked player from a player's blocklist
     *
     * @param uuid              UUID unblocking player
     * @param blockedPlayerUuid UUID
     */
    public static void removeBlockedUser(UUID uuid, UUID blockedPlayerUuid) {
        List<PlayerBlock> userBlockList = blockList.get(uuid);
        for (PlayerBlock block : userBlockList) {
            if (block.getBlockedPlayerUUID().equals(blockedPlayerUuid)) {
                userBlockList.remove(block);
                break;
            }
        }
        blockList.put(uuid, userBlockList);
        SQLHandler.getInstance().removeBlockedPlayer(uuid, blockedPlayerUuid);
    }

    private static void removeCachedDuplicates(UUID blockingUuid, UUID blockedUuid) {
        List<PlayerBlock> blockedPlayers = blockList.get(blockingUuid);
        if (blockedPlayers == null) blockedPlayers = new ArrayList<>();
        blockedPlayers.removeIf(block -> block.getBlockedPlayerUUID().equals(blockedUuid));
        blockList.put(blockingUuid, blockedPlayers);
    }

    private static void populateNullNames(UUID uuidToCheck) {
        List<PlayerBlock> playerBlocks = blockList.get(uuidToCheck);
        if (playerBlocks == null || playerBlocks.isEmpty()) return;
        for (PlayerBlock block : playerBlocks) {
            if (block.getBlockedPlayerName() == null || block.getBlockedPlayerName().isEmpty()) {
                String newName = PlatformBridge.getPlatformAdapter().getPlayerName(block.getBlockedPlayerUUID());
                if (newName == null) newName = PlatformBridge.getPlatformAdapter().getLocaleString(LocaleKey.ERROR_NAME_NOT_FOUND);
                block.setBlockedPlayerName(newName);
            }
        }
        blockList.put(uuidToCheck, playerBlocks);
    }

    public static Set<UUID> getSpyingPlayers() {
        return spyingPlayers;
    }
}
