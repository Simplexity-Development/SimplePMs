package simplexity.simplepms;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simplepms.commands.PrivateMessage;
import simplexity.simplepms.commands.ReloadCommand;
import simplexity.simplepms.commands.ReplyCommand;
import simplexity.simplepms.commands.SocialSpyCommand;
import simplexity.simplepms.config.LocaleConfig;
import simplexity.simplepms.listeners.LoginListener;

import java.util.HashSet;
import java.util.Objects;

public final class SimplePMs extends JavaPlugin {

    private static Plugin instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static boolean papiEnabled = false;

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
        LocaleConfig.getInstance().reloadLocale();
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


    private static final HashSet<Player> spyingPlayers = new HashSet<>();

    public static HashSet<Player> getSpyingPlayers() {
        return spyingPlayers;
    }

}
