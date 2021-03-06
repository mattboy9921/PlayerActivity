package edgruberman.bukkit.playeractivity.consumers.listtag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import edgruberman.bukkit.playeractivity.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class ListTag implements Listener {

    private static final SortedSet<Tag> EMTPY_TAG_SORTED_SET = Collections.unmodifiableSortedSet(new TreeSet<Tag>());

    private final String track;
    private final Main plugin;
    private final List<Tag> tags = new ArrayList<>();
    private final Map<String, TreeSet<Tag>> attached = new HashMap<>();

    public ListTag(final Main plugin, final ConfigurationSection tags, final String track) {
        this.track = track;
        this.plugin = plugin;

        for (final String key : tags.getKeys(false)) {
            if (!tags.isConfigurationSection(key)) continue;
            final ConfigurationSection config = tags.getConfigurationSection(key);
            if (!config.getBoolean("enabled", true)) continue;

            final String className = config.getString("class", config.getName());
            try {
                final Tag tag = Tag.create(className, config, this, plugin);
                this.register(tag);
            } catch (final Exception e) {
                plugin.getLogger().warning("Unable to create Tag for ListTag consumer: " + className + "; " + e);
            }
        }

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent quit) {
        this.clear(quit.getPlayer());
    }

    private void register(final Tag tag) {
        this.tags.add(tag);
    }

    public void deregister(final Tag tag) {
        this.tags.remove(tag);
    }

    void attach(final Tag tag, final Player player) {
        if (!player.hasPermission(this.track)) return;
        if (!this.attached.containsKey(player.getName())) this.attached.put(player.getName(), new TreeSet<>());
        final TreeSet<Tag> tags = this.attached.get(player.getName());
        if (!tags.add(tag)) return;
        this.updateListName(player);
    }

    void detach(final Tag tag, final Player player) {
        if (!player.hasPermission(this.track)) return;
        final TreeSet<Tag> tags = this.attached.get(player.getName());
        if (tags == null || !tags.remove(tag)) return;
        if (tags.size() == 0) this.attached.remove(player.getName());
        this.updateListName(player);
    }

    public void clear(final Player player) {
        if (player == null || !this.attached.containsKey(player.getName())) return;
        final TreeSet<Tag> tags = new TreeSet<>(this.attached.get(player.getName()));
        for (final Tag tag : tags) tag.detach(player);
    }

    public void unload() {
        for (final String name : this.attached.keySet()) {
            final Player player = Bukkit.getPlayerExact(name);
            if (player != null) this.resetListName(player);
        }
        this.attached.clear();
        for (final Tag tag : this.tags) tag.unload();
        HandlerList.unregisterAll(this);
    }

    private void resetListName(final Player player) {

        String prefix;

        if (!player.isOnline()) return;

        // Only get prefix if Vault Chat available
        prefix = plugin.vault.isAvailable() ? new Prefix(player).getPrefix() : "&f";
        player.setPlayerListName(prefix + player.getName());

    }

    private void updateListName(final Player player) {
        if (!player.isOnline()) return;
        final TreeSet<Tag> tags = this.attached.get(player.getName());
        if (tags != null) {
            player.setPlayerListName(tags.first().getPlayerListName(player));
        } else {
            this.resetListName(player);
        }
    }

    public String getTagDisplayName(final Object player) {
        final TreeSet<Tag> tags = this.attached.get(Bukkit.getName());
        if (tags == null) return ((Player) player).getDisplayName();
        return tags.first().getDisplayName(player);
    }

    public String getTagDescription(final Player player) {
        final TreeSet<Tag> tags = this.attached.get(player.getName());
        if (tags == null) return null;
        return tags.first().describe(player);
    }

    public SortedSet<Tag> getAttached(final Player player) {
        if (!this.attached.containsKey(player.getName())) return ListTag.EMTPY_TAG_SORTED_SET;
        return Collections.unmodifiableSortedSet(this.attached.get(player.getName()));
    }

}
