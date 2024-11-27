package simplexity.simplepms;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simplepms.commands.PrivateMessage;
import simplexity.simplepms.commands.ReloadCommand;
import simplexity.simplepms.commands.ReplyCommand;
import simplexity.simplepms.commands.SocialSpyCommand;
import simplexity.simplepms.config.LocaleHandler;
import simplexity.simplepms.listeners.LoginListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public final class SimplePMs extends JavaPlugin {

    private static Plugin instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static boolean papiEnabled = false;
    private static final List<Player> players = new ArrayList<>();
    private static final List<Player> spyingPlayers = new ArrayList<>();

    public static List<Player> getPlayers() {
        return players;
    }
    public static List<Player> getSpyingPlayers() {
        return spyingPlayers;
    }


    @Override
    public void onEnable() {
        instance = this;
        registerCommands();
        this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiEnabled = true;
        } else {
            this.getLogger().info("You do not have PlaceholderAPI loaded on your server. Any PlaceholderAPI placeholders used in this plugin's messages, will not work.");
        }
        LocaleHandler.getInstance().reloadLocale();
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static boolean isPapiEnabled() {
        return papiEnabled;
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("msg")).setExecutor(new PrivateMessage());
        Objects.requireNonNull(this.getCommand("reply")).setExecutor(new ReplyCommand());
        Objects.requireNonNull(this.getCommand("socialspy")).setExecutor(new SocialSpyCommand());
        Objects.requireNonNull(this.getCommand("spmreload")).setExecutor(new ReloadCommand());
    }

}
