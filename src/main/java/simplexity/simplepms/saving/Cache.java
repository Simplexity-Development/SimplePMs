package simplexity.simplepms.saving;

import simplexity.simplepms.saving.objects.PlayerBlock;
import simplexity.simplepms.saving.objects.PlayerSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Cache {
    public static final HashMap<UUID, List<PlayerBlock>> blockList = new HashMap<>();
    public static final HashMap<UUID, PlayerSettings> playerSettings = new HashMap<>();

    public static List<PlayerBlock> getBlockList(UUID uuid) {
        return blockList.get(uuid);
    }

    public static PlayerSettings getPlayerSettings(UUID uuid) {
        return playerSettings.get(uuid);
    }

    /**
     * Adds the provided player's settings to the cache
     *
     * @param uuid Player's UUID
     */
    public static void addPlayerSettingsToCache(UUID uuid) {
        SqlHandler.getInstance().getSettings(uuid, (settings) -> {
            if (settings != null) {
                playerSettings.put(uuid, settings);
            }
        });
    }

    /**
     * Adds the provided player's Block List to the cache
     *
     * @param uuid Player's UUID
     */

    public static void addBlockListToCache(UUID uuid) {
        SqlHandler.getInstance().getBlockedPlayers(uuid, (userBlockList) -> {
            if (userBlockList != null) {
                blockList.put(uuid, userBlockList);
            }
        });
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
        SqlHandler.getInstance().updateSettings(uuid, settings.isSocialSpyEnabled(), settings.areMessagesDisabled());
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
        SqlHandler.getInstance().updateSettings(uuid, settings.isSocialSpyEnabled(), settings.areMessagesDisabled());
    }

    /**
     * Updates a player's blocklist by adding or updating a blocked player.
     *
     * @param uuid        UUID blocking player
     * @param playerBlock PlayerBlock block
     */

    public static void addBlockedUser(UUID uuid, PlayerBlock playerBlock) {
        removeCachedDuplicates(uuid, playerBlock.blockedPlayerUUID());
        List<PlayerBlock> blockedPlayers = blockList.get(uuid);
        blockedPlayers.add(playerBlock);
        blockList.put(uuid, blockedPlayers);
        SqlHandler.getInstance().addBlockedPlayer(uuid, playerBlock.blockedPlayerUUID(), playerBlock.blockReason());
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
            if (block.blockedPlayerUUID().equals(blockedPlayerUuid)) {
                userBlockList.remove(block);
                break;
            }
        }
        blockList.put(uuid, userBlockList);
        SqlHandler.getInstance().removeBlockedPlayer(uuid, blockedPlayerUuid);
    }

    private static void removeCachedDuplicates(UUID blockingUuid, UUID blockedUuid) {
        List<PlayerBlock> blockedPlayers = blockList.get(blockingUuid);
        if (blockedPlayers == null) blockedPlayers = new ArrayList<>();
        blockedPlayers.removeIf(block ->
                block.blockedPlayerUUID().equals(blockedUuid));
        blockList.put(blockingUuid, blockedPlayers);
    }

}
