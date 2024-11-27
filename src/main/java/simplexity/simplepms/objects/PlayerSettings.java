package simplexity.simplepms.objects;

import java.util.Objects;
import java.util.UUID;

public final class PlayerSettings {
    private final UUID playerUUID;
    private boolean socialSpyEnabled;
    private boolean messagesEnabled;

    public PlayerSettings(UUID playerUUID, boolean socialSpyEnabled, boolean messagesEnabled) {
        this.playerUUID = playerUUID;
        this.socialSpyEnabled = socialSpyEnabled;
        this.messagesEnabled = messagesEnabled;
    }

    public String toString() {
        return "Player UUID: " + playerUUID.toString()
                + "\nSocial Spy Enabled: " + socialSpyEnabled
                + "\nMessages Enabled: " + messagesEnabled;
    }

    public UUID playerUUID() {
        return playerUUID;
    }

    public boolean socialSpyEnabled() {
        return socialSpyEnabled;
    }

    public boolean messagesEnabled() {
        return messagesEnabled;
    }

    public void setSocialSpyEnabled(boolean socialSpyEnabled) {
        this.socialSpyEnabled = socialSpyEnabled;
    }

    public void setMessagesEnabled(boolean messagesEnabled) {
        this.messagesEnabled = messagesEnabled;
    }

}
