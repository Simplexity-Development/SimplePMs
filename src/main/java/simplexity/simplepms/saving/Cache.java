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
        if (blockList.containsKey(uuid)) {
            return blockList.get(uuid);
        }
        List<PlayerBlock> blockedPlayers = SqlHandler.getInstance().getBlockedPlayers(uuid);
        blockList.put(uuid, blockedPlayers);
        return blockedPlayers;
    }

    public static PlayerSettings getPlayerSettings(UUID uuid) {
        if (playerSettings.containsKey(uuid)) {
            return playerSettings.get(uuid);
        }
        PlayerSettings settings = SqlHandler.getInstance().getSettings(uuid);
        playerSettings.put(uuid, settings);
        return settings;
    }

    public static void updateSocialSpySettings(UUID uuid, boolean socialSpy) {
        PlayerSettings settings = getPlayerSettings(uuid);
        settings.setSocialSpyEnabled(socialSpy);
        playerSettings.put(uuid, settings);
        SqlHandler.getInstance().updateSettings(uuid, settings);
    }

    public static void updateMessageSettings(UUID uuid, boolean messageDisabled) {
        PlayerSettings settings = getPlayerSettings(uuid);
        settings.setMessagesDisabled(messageDisabled);
        playerSettings.put(uuid, settings);
        SqlHandler.getInstance().updateSettings(uuid, settings);
    }

    public static void addBlockedUser(UUID uuid, PlayerBlock playerBlock) {
        List<PlayerBlock> blockedPlayers = getBlockList(uuid);
        if (blockedPlayers == null) blockedPlayers = new ArrayList<>();
        blockedPlayers.removeIf(block -> {
            UUID blockedUUID = playerBlock.blockedPlayerUUID();
            UUID currentUUID = block.blockedPlayerUUID();
            return currentUUID.equals(blockedUUID);
        });
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

}
