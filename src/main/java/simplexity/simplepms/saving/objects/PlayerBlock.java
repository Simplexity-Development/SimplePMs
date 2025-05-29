package simplexity.simplepms.saving.objects;

import java.util.UUID;

@SuppressWarnings("StringTemplateMigration")
public record PlayerBlock(UUID blockingPlayerUUID, String blockedPlayerName, UUID blockedPlayerUUID, String blockReason) {

    public String toString() {
        return "PlayerBlock [" + "blockingPlayerUUID=" + blockingPlayerUUID
                + ", blockedPlayerName=" + blockedPlayerName
                + "blockedPlayerUUID=" + blockedPlayerUUID
                + ", blockReason=" + blockReason + "]";
    }
}
