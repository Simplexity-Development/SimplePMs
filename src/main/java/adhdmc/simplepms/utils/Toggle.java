package adhdmc.simplepms.utils;

public enum Toggle {
    DISPLAYNAME(true),
    SPY_ON_SELF(false),
    PERSIST_SPY(true);

    boolean Toggle;
    Toggle(boolean setting){
        this.Toggle = setting;
    }

    public boolean isEnabled() {
        return Toggle;
    }

    public void setEnabled(boolean toggle) {
        Toggle = toggle;
    }
}
