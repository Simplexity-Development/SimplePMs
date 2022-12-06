package adhdmc.simplepms;

import adhdmc.simplepms.commands.PrivateMessage;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimplePMs extends JavaPlugin {

    private static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
        registerCommands();
    }

    @Override
    public void onDisable() {

    }

    private void registerCommands() {
        this.getCommand("msg").setExecutor(new PrivateMessage());
    }

}
