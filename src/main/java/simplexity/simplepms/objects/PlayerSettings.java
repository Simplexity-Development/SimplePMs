package simplexity.simplepms.objects;

import java.util.UUID;

public final class PlayerSettings {
    private final UUID playerUUID;
    private boolean socialSpyEnabled;
    private boolean messagesDisabled;

    public PlayerSettings(UUID playerUUID, boolean socialSpyEnabled, boolean messagesDisabled) {
        this.playerUUID = playerUUID;
        this.socialSpyEnabled = socialSpyEnabled;
        this.messagesDisabled = messagesDisabled;
    }

    public PlayerSettings(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.socialSpyEnabled = false;
        this.messagesDisabled = false;
    }

    public String toString() {
        return "Player UUID: " + playerUUID.toString()
                + "\nSocial Spy Enabled: " + socialSpyEnabled
                + "\nMessages Enabled: " + messagesDisabled;
    }

    public UUID playerUUID() {
        return playerUUID;
    }

    public boolean socialSpyEnabled() {
        return socialSpyEnabled;
    }

    public boolean messagesDisabled() {
        return messagesDisabled;
    }

    public void setSocialSpyEnabled(boolean socialSpyEnabled) {
        this.socialSpyEnabled = socialSpyEnabled;
    }

    public void setMessagesDisabled(boolean messagesDisabled) {
        this.messagesDisabled = messagesDisabled;
    }

}
