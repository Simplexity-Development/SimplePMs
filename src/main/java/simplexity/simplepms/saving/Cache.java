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

    public static void addPlayerSettingsToCache(UUID uuid) {
        PlayerSettings settings = SqlHandler.getInstance().getSettings(uuid);
        playerSettings.put(uuid, settings);
    }

    public static void addBlockListToCache(UUID uuid) {
        List<PlayerBlock> userBlockList = SqlHandler.getInstance().getBlockedPlayers(uuid);
        blockList.put(uuid, userBlockList);
    }

    public static void removePlayerSettingsFromCache(UUID uuid){
        playerSettings.remove(uuid);
    }

    public static void removeBlockListFromCache(UUID uuid){
        playerSettings.remove(uuid);
    }

    public static void updateSocialSpySettings(UUID uuid, boolean socialSpy) {
        PlayerSettings settings = playerSettings.get(uuid);
        settings.setSocialSpyEnabled(socialSpy);
        playerSettings.put(uuid, settings);
        SqlHandler.getInstance().updateSettings(uuid, settings.isSocialSpyEnabled(), settings.areMessagesDisabled());
    }

    public static void updateMessageSettings(UUID uuid, boolean messageDisabled) {
        PlayerSettings settings = playerSettings.get(uuid);
        settings.setMessagesDisabled(messageDisabled);
        playerSettings.put(uuid, settings);
        SqlHandler.getInstance().updateSettings(uuid, settings.isSocialSpyEnabled(), settings.areMessagesDisabled());
    }

    public static void addBlockedUser(UUID uuid, PlayerBlock playerBlock) {
        removeCachedDuplicates(uuid, playerBlock.blockedPlayerUUID());
        List<PlayerBlock> blockedPlayers = blockList.get(uuid);
        blockedPlayers.add(playerBlock);
        blockList.put(uuid, blockedPlayers);
        SqlHandler.getInstance().addBlockedPlayer(uuid, playerBlock.blockedPlayerUUID(), playerBlock.blockReason());
    }

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
