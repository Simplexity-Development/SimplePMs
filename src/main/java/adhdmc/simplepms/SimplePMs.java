package adhdmc.simplepms;

import adhdmc.simplepms.commands.PrivateMessage;
import adhdmc.simplepms.commands.ReloadCommand;
import adhdmc.simplepms.commands.ReplyCommand;
import adhdmc.simplepms.commands.SocialSpyCommand;
import adhdmc.simplepms.listeners.LoginListener;
import adhdmc.simplepms.utils.Message;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimplePMs extends JavaPlugin {
    //todo: Add placeholderAPI support
    //todo: Add message event

    private static Plugin instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private boolean papiEnabled = false;

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
        this.saveDefaultConfig();
        Message.reloadMessages();
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public static Plugin getInstance() {
        return instance;
    }

    private void registerCommands() {
        this.getCommand("msg").setExecutor(new PrivateMessage());
        this.getCommand("reply").setExecutor(new ReplyCommand());
        this.getCommand("socialspy").setExecutor(new SocialSpyCommand());
        this.getCommand("spmreload").setExecutor(new ReloadCommand());
    }

}
