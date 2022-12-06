package adhdmc.simplepms;

import adhdmc.simplepms.commands.PrivateMessage;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimplePMs extends JavaPlugin {

    private static Plugin instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        instance = this;
        registerCommands();
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {

    }

    private void registerCommands() {
        this.getCommand("msg").setExecutor(new PrivateMessage());
    }

}
