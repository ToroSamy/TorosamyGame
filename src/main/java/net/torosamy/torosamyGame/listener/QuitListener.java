package net.torosamy.torosamyGame.listener;

import net.torosamy.torosamyGame.rps.RPSGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        RPSGame.delete(event.getPlayer().getName());
    }
}
