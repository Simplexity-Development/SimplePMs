package simplexity.simplepms.saving.objects;

import java.util.UUID;

@SuppressWarnings({"StringTemplateMigration", "unused"})
public class PlayerBlock {

    private final UUID blockingPlayerUUID;
    private final UUID blockedPlayerUUID;
    private String blockedPlayerName;
    private String blockReason;

    public PlayerBlock(UUID blockingPlayerUUID, String blockedPlayerName, UUID blockedPlayerUUID, String blockReason) {
        this.blockingPlayerUUID = blockingPlayerUUID;
        this.blockedPlayerUUID = blockedPlayerUUID;
        this.blockedPlayerName = blockedPlayerName;
        this.blockReason = blockReason;
    }

    public UUID getBlockingPlayerUUID() {
        return blockingPlayerUUID;
    }

    public UUID getBlockedPlayerUUID() {
        return blockedPlayerUUID;
    }

    public String getBlockedPlayerName() {
        return blockedPlayerName;
    }

    public void setBlockedPlayerName(String name) {
        blockedPlayerName = name;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(String reason) {
        blockReason = reason;
    }


    public String toString() {
        return "PlayerBlock [" + "blockingPlayerUUID=" + blockingPlayerUUID
               + ", blockedPlayerName=" + blockedPlayerName
               + "blockedPlayerUUID=" + blockedPlayerUUID
               + ", blockReason=" + blockReason + "]";
    }
}
