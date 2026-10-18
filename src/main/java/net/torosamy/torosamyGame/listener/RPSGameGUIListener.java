package net.torosamy.torosamyGame.listener;

import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.rps.RPSGame;
import net.torosamy.torosamyGame.rps.RPSGameGUI;
import net.torosamy.torosamyGame.rps.RPSGameGUIHolder;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class RPSGameGUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getView().getTopInventory();

        if (!RPSGameGUIHolder.isBlockerInventory(inventory)) { 
            return;
        }
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        RPSGame game = RPSGame.getGame(player.getName());
        
        if (game == null) {
            return;
        }

        RPSGameGUI gameGUI = game.getGameGUI(player.getName());
        
        if (gameGUI == null) {
            return;
        }

        if (gameGUI.isConfirm()) {
            return;
        }

        if (RPSGameGUI.CONFIRM_ITEM_SLOT == event.getSlot()) {
            gameGUI.confirm();
            return;
        }
        
        if (RPSGameGUI.PAPER_SLOT == event.getSlot()) {
            gameGUI.updateOption(RPSGameGUI.Option.PAPER);
            return;
        }
        
        if (RPSGameGUI.ROCK_SLOT == event.getSlot()) {
            gameGUI.updateOption(RPSGameGUI.Option.ROCK);
            return;
        }

        if (RPSGameGUI.SCISSORS_SLOT == event.getSlot()) {
            gameGUI.updateOption(RPSGameGUI.Option.SCISSORS);
            return;
        }
    }
    
    @EventHandler
    public void close(InventoryCloseEvent event) {
        Inventory inventory = event.getView().getTopInventory();

        if (!RPSGameGUIHolder.isBlockerInventory(inventory)) {
            return;
        }

        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        
        RPSGame game = RPSGame.getGame(player.getName());
        
        if (game == null) {
            return;
        }

        RPSGame.delete(player.getName());

        Player receiver = game.getReceiver();
        receiver.closeInventory();
        receiver.sendMessage(MessageUtil.component(ConfigUtil.langConfig.rpsCloseMenu));

        Player sender = game.getSender();
        sender.closeInventory();
        sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.rpsCloseMenu));
    
        
    }
}