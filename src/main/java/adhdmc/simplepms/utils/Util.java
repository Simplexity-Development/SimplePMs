package adhdmc.simplepms.utils;

import org.bukkit.entity.Player;

import java.util.HashSet;

public class Util {
    public static final String console = "CONSOLE";
    private static final HashSet<Player> spyingPlayers = new HashSet<>();

    public static HashSet<Player> getSpyingPlayers() {
        return spyingPlayers;
    }
}
