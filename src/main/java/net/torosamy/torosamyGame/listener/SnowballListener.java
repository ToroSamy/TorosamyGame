package net.torosamy.torosamyGame.listener;

import net.torosamy.torosamyGame.snow.SnowGame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SnowballListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSnowWarHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player defensePlayer)) return;
        if (!(event.getDamager() instanceof Snowball snowball)) return;
        if (!(snowball.getShooter() instanceof Player attackPlayer)) return;
        
        if (defensePlayer.equals(attackPlayer)) return;


        SnowGame game = SnowGame.getSnowGame(defensePlayer.getName());
        if (game == null) {
            return;
        }

        
        defensePlayer.playHurtAnimation(attackPlayer.getLocation().getYaw());

        game.addScore(attackPlayer.getName());
    }
}