package edgruberman.bukkit.playeractivity.interpreters;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import edgruberman.bukkit.playeractivity.StatusTracker;

public class BlockDamageEvent extends Interpreter {

    public BlockDamageEvent(final StatusTracker tracker) {
        super(tracker);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEvent(final org.bukkit.event.block.BlockDamageEvent event) {
        this.record(event.getPlayer(), event);
    }

}
