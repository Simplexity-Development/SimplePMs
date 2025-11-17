package simplexity.simplepms.paper.saving;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simplexity.simplepms.paper.SimplePMs;
import simplexity.simplepms.paper.config.LocaleMessage;
import simplexity.simplepms.paper.saving.objects.PlayerBlock;
import simplexity.simplepms.paper.saving.objects.PlayerSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Cache {
    public static final HashMap<UUID, List<PlayerBlock>> blockList = new HashMap<>();
    public static final HashMap<UUID, PlayerSettings> playerSettings = new HashMap<>();
    public static final HashSet<Player> spyingPlayers = new HashSet<>();

    public static List<PlayerBlock> getBlockList(UUID uuid) {
        return blockList.get(uuid);
    }

    public static PlayerSettings getPlayerSettings(UUID uuid) {
        return playerSettings.get(uuid);
    }


    public static void populateCache(UUID uuid, Player player, boolean hasSpyPerms) {
        Bukkit.getScheduler().runTaskAsynchronously(SimplePMs.getInstance(), () -> {
            SqlHandler.getInstance().getBlockedPlayers(uuid).thenAccept(blocklist -> {
                blockList.put(uuid, blocklist);
                populateNullNames(uuid);
            });
            SqlHandler.getInstance().getSettings(uuid).thenAccept(settings -> {
                playerSettings.put(uuid, settings);
                if (hasSpyPerms && settings.isSocialSpyEnabled()) {
                    spyingPlayers.add(player);
                }
            });
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
        removeCachedDuplicates(uuid, playerBlock.getBlockedPlayerUUID());
        List<PlayerBlock> blockedPlayers = blockList.get(uuid);
        blockedPlayers.add(playerBlock);
        blockList.put(uuid, blockedPlayers);
        SqlHandler.getInstance().addBlockedPlayer(uuid, playerBlock.getBlockedPlayerUUID(), playerBlock.getBlockedPlayerName(), playerBlock.getBlockReason());
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
        SqlHandler.getInstance().removeBlockedPlayer(uuid, blockedPlayerUuid);
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
                String newName = Bukkit.getOfflinePlayer(block.getBlockedPlayerUUID()).getName();
                if (newName == null) newName = LocaleMessage.ERROR_NAME_NOT_FOUND.getMessage();
                block.setBlockedPlayerName(newName);
            }
        }
        blockList.put(uuidToCheck, playerBlocks);
    }

    public static Set<Player> getSpyingPlayers() {
        return spyingPlayers;
    }
}
