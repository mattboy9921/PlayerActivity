package edgruberman.bukkit.playeractivity.util;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultIntegration {

    private Plugin plugin;
    public static Chat chat = null;

    public VaultIntegration(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setupChat() {
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }

    public boolean isAvailable() {
        return plugin.getServer().getServicesManager().getRegistration(Chat.class) != null;
    }
}
