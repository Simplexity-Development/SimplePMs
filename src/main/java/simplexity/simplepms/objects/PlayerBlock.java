package simplexity.simplepms.objects;

import java.util.UUID;

public record PlayerBlock(UUID blockingPlayerUUID, UUID blockedPlayerUUID, String blockReason) {

    public String toString() {
        return "PlayerBlock [" + "blockingPlayerUUID=" + blockingPlayerUUID
                + "blockedPlayerUUID=" + blockedPlayerUUID
                + ", blockReason=" + blockReason + "]";
    }
}
