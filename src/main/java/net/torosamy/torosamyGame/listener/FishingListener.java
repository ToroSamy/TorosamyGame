package net.torosamy.torosamyGame.listener;

import net.torosamy.torosamyGame.fish.FishGame;
import org.bukkit.Tag;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingListener implements Listener {
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        Entity caught = event.getCaught();
        
        if (caught == null) {
            return;
        }

        if (!(caught instanceof Item item)) {
            return;
        }

        if (!Tag.ITEMS_FISHES.isTagged(item.getItemStack().getType())) {
            return;
        }
        
        FishGame fishGame = FishGame.getFishGame(player.getName());
        if (fishGame == null) {
            return;
        }
        
        fishGame.addScore(player.getName());
    }
}