package edgruberman.bukkit.playeractivity.consumers.listtag;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import edgruberman.bukkit.playeractivity.Main;

public abstract class Tag implements Comparable<Tag> {

    //private static final int LIST_NAME_LENGTH = 16;
    private static final String TAGS_PACKAGE = Tag.class.getPackage().getName() + ".tags";

    public static Tag create(final String className, final ConfigurationSection config, final ListTag listTag, final Main plugin)
            throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException
                , InvocationTargetException, NoSuchMethodException, ClassCastException, ClassNotFoundException {
        return Tag
                .find(className)
                .getConstructor(ConfigurationSection.class, ListTag.class, Main.class)
                .newInstance(config, listTag, plugin);
    }

    private static Class<? extends Tag> find(final String className) throws ClassNotFoundException, ClassCastException {
        try {
            return Class.forName(Tag.TAGS_PACKAGE + "." + className).asSubclass(Tag.class);
        } catch (final Exception e) {
            return Class.forName(className).asSubclass(Tag.class);
        }
    }



    // ---- instance ----

    private final String pattern;
    private final int length;
    private final String description;
    private final Integer priority;
    private final ListTag listTag;
    protected final Main plugin;
    private final Map<String, Long> attached = new HashMap<>();

    public Tag(final ConfigurationSection config, final ListTag listTag, final Main plugin) {
        this.pattern = config.getString("pattern");
        this.length = MessageFormat.format(this.pattern, "").length();
        this.description = config.getString("description");
        this.priority = config.getInt("priority");
        this.listTag = listTag;
        this.plugin = plugin;
    }

    protected void attach(final Player player) {
        this.attach(player, System.currentTimeMillis());
    }

    protected void attach(final Player player, final long attached) {
        this.attached.put(player.getName(), attached);
        this.listTag.attach(this, player);
    }

    protected void detach(final Player player) {
        this.attached.remove(player.getName());
        this.listTag.detach(this, player);
    }

    final void unload() {
        this.onUnload();
        this.attached.clear();
    }

    protected abstract void onUnload();

    @Override
    public int compareTo(final Tag o) {
        return ( o == null ? -1 : this.priority.compareTo(o.priority) );
    }

    String getPlayerListName(final Player player) {
        String prefix = plugin.vault.isAvailable() ? new Prefix(player).getPrefix() : "&f";
        final String name = prefix + player.getName()/*.substring(0, Math.min(player.getName().length(), Tag.LIST_NAME_LENGTH - this.length))*/;
        return MessageFormat.format(this.pattern, name);
    }

    String getDisplayName(final Object player) {
        return MessageFormat.format(this.pattern, ((Player) player).getDisplayName());
    }

    final String describe(final Player player) {
        final List<Object> arguments = new ArrayList<>();
        if (!this.attached.containsKey(player.getName())) return null;
        final Long attached = System.currentTimeMillis() - this.attached.get(player.getName());
        final String duration = ( attached != null ? Main.readableDuration(attached) : null );
        arguments.add(duration);
        arguments.add(( attached != null ? 1 : 0 ));
        arguments.add(this.getPlayerListName(player));
        return this.onDescribe(player, arguments);
    }

    protected String onDescribe(final Player player, final List<Object> arguments) {
        return MessageFormat.format(this.description, arguments.toArray());
    }

}
