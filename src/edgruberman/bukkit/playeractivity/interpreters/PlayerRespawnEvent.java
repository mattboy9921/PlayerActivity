package edgruberman.bukkit.playeractivity.interpreters;

import org.bukkit.event.EventHandler;

import edgruberman.bukkit.playeractivity.Interpreter;

public class PlayerRespawnEvent extends Interpreter {

    @EventHandler
    public void onEvent(final org.bukkit.event.player.PlayerRespawnEvent event) {
        this.player = event.getPlayer();
    }

}