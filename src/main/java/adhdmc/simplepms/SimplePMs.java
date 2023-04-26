package adhdmc.simplepms;

import adhdmc.simplepms.commands.PrivateMessage;
import adhdmc.simplepms.commands.ReloadCommand;
import adhdmc.simplepms.commands.ReplyCommand;
import adhdmc.simplepms.commands.SocialSpyCommand;
import adhdmc.simplepms.config.LocaleConfig;
import adhdmc.simplepms.listeners.LoginListener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

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
        this.getCommand("msg").setExecutor(new PrivateMessage());
        this.getCommand("reply").setExecutor(new ReplyCommand());
        this.getCommand("socialspy").setExecutor(new SocialSpyCommand());
        this.getCommand("spmreload").setExecutor(new ReloadCommand());
    }

    private static final HashSet<Player> spyingPlayers = new HashSet<>();

    public static HashSet<Player> getSpyingPlayers() {
        return spyingPlayers;
    }

}
