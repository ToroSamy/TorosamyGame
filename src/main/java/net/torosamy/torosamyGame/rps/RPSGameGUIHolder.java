package net.torosamy.torosamyGame.rps;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class RPSGameGUIHolder implements InventoryHolder {
    public static final RPSGameGUIHolder GAME_GUI_HOLDER = new RPSGameGUIHolder();

    @Override
    public @NotNull Inventory getInventory() {
        throw new UnsupportedOperationException("This InventoryHolder is only used as a marker.");
    }

    public static boolean isBlockerInventory(Inventory inventory) {
        return inventory.getHolder() instanceof RPSGameGUIHolder;
    }
}
