package edgruberman.bukkit.playeractivity.filters;

import org.bukkit.event.EventHandler;

import edgruberman.bukkit.playeractivity.Interpreter;

public class PlayerChangedWorldEvent extends Interpreter {

    @EventHandler
    public void onEvent(final org.bukkit.event.player.PlayerChangedWorldEvent event) {
        this.player = event.getPlayer();
    }

}
