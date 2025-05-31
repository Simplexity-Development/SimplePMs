package simplexity.simplepms;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
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
import simplexity.simplepms.listeners.PreLoginListener;
import simplexity.simplepms.listeners.QuitListener;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public final class SimplePMs extends JavaPlugin {

    private static Plugin instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static boolean papiEnabled = false;
    private static final HashSet<Player> spyingPlayers = new HashSet<>();
    private static ConsoleCommandSender consoleSender;


    public static Set<Player> getSpyingPlayers() {
        return spyingPlayers;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new PreLoginListener(), this);
        this.getServer().getPluginManager().registerEvents(new QuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new PreCommandListener(), this);
        this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiEnabled = true;
        }
        consoleSender = this.getServer().getConsoleSender();
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        ConfigHandler.getInstance().loadConfigValues();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(PrivateMessage.createCommand());
            commands.registrar().register(PrivateMessage.createTellAlias());
            commands.registrar().register(PrivateMessage.createWhisperAlias());
            commands.registrar().register(Reply.createCommand());
            commands.registrar().register(Reply.createAlias());
            commands.registrar().register(Block.createCommand());
            commands.registrar().register(Unblock.createCommand());
            commands.registrar().register(MessageToggle.createCommand());
            commands.registrar().register(SocialSpy.createCommand());
            commands.registrar().register(SocialSpy.createAlias());
            commands.registrar().register(Reload.createCommand());
            commands.registrar().register(Blocklist.createCommand());
        });
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

}
