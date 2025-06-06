package simplexity.simplepms;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.ConsoleCommandSender;
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
import simplexity.simplepms.listeners.JoinListener;
import simplexity.simplepms.listeners.PreCommandListener;
import simplexity.simplepms.listeners.QuitListener;
import simplexity.simplepms.logic.Constants;
import simplexity.simplepms.saving.SqlHandler;

@SuppressWarnings("UnstableApiUsage")
public final class SimplePMs extends JavaPlugin {

    private static Plugin instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static boolean papiEnabled = false;
    private static ConsoleCommandSender consoleSender;


    @Override
    public void onEnable() {
        instance = this;
        consoleSender = getServer().getConsoleSender();
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiEnabled = true;
        }
        SqlHandler.getInstance().init();
        loadConfigStuff();
        registerListeners();
        registerCommands();
        registerPermissions();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new PreCommandListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
    }

    private void loadConfigStuff() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        ConfigHandler.getInstance().loadConfigValues();
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
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

    private void registerPermissions() {
        getServer().getPluginManager().addPermission(Constants.MESSAGE_BASIC);
        getServer().getPluginManager().addPermission(Constants.MESSAGE_ADMIN);
        getServer().getPluginManager().addPermission(Constants.MESSAGE_SEND);
        getServer().getPluginManager().addPermission(Constants.MESSAGE_RECEIVE);
        getServer().getPluginManager().addPermission(Constants.MESSAGE_TOGGLE);
        getServer().getPluginManager().addPermission(Constants.MESSAGE_BLOCK);
        getServer().getPluginManager().addPermission(Constants.PLUGIN_RELOAD);
        getServer().getPluginManager().addPermission(Constants.ADMIN_OVERRIDE);
        getServer().getPluginManager().addPermission(Constants.ADMIN_SOCIAL_SPY);
        getServer().getPluginManager().addPermission(Constants.ADMIN_CONSOLE_SPY);
        getServer().getPluginManager().addPermission(Constants.BYPASS_SOCIAL_SPY);
        getServer().getPluginManager().addPermission(Constants.BYPASS_COMMAND_SPY);
    }

    @Override
    public void onDisable() {
        SqlHandler.getInstance().shutdownConnection();
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
