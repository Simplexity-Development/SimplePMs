package simplexity.simplepms;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simplepms.commands.Block;
import simplexity.simplepms.commands.Blocklist;
import simplexity.simplepms.commands.MessageToggle;
import simplexity.simplepms.commands.PrivateMessage;
import simplexity.simplepms.commands.Reload;
import simplexity.simplepms.commands.Reply;
import simplexity.simplepms.commands.SocialSpy;
import simplexity.simplepms.commands.Unblock;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.listeners.LoginListener;
import simplexity.simplepms.listeners.PreCommandListener;
import simplexity.simplepms.listeners.QuitListener;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class SimplePMs extends JavaPlugin {

    private static Plugin instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static boolean papiEnabled = false;
    private static final HashSet<Player> players = new HashSet<>();
    private static final HashSet<Player> spyingPlayers = new HashSet<>();
    private static ConsoleCommandSender consoleSender;

    public static HashSet<Player> getPlayers() {
        return players;
    }

    public static Set<Player> getSpyingPlayers() {
        return spyingPlayers;
    }

    @Override
    public void onEnable() {
        instance = this;
        registerCommands();
        this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
        this.getServer().getPluginManager().registerEvents(new QuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new PreCommandListener(), this);
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiEnabled = true;
        } else {
            this.getLogger().info("You do not have PlaceholderAPI loaded on your server. Any PlaceholderAPI placeholders used in this plugin's messages, will not work.");
        }
        consoleSender = this.getServer().getConsoleSender();
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        ConfigHandler.getInstance().loadConfigValues();
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

    public static ConsoleCommandSender getPMConsoleSender() {
        return consoleSender;
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("msg")).setExecutor(new PrivateMessage());
        Objects.requireNonNull(this.getCommand("reply")).setExecutor(new Reply());
        Objects.requireNonNull(this.getCommand("socialspy")).setExecutor(new SocialSpy());
        Objects.requireNonNull(this.getCommand("spmreload")).setExecutor(new Reload());
        Objects.requireNonNull(this.getCommand("block")).setExecutor(new Block());
        Objects.requireNonNull(this.getCommand("unblock")).setExecutor(new Unblock());
        Objects.requireNonNull(this.getCommand("blocklist")).setExecutor(new Blocklist());
        Objects.requireNonNull(this.getCommand("msgtoggle")).setExecutor(new MessageToggle());
    }

}
