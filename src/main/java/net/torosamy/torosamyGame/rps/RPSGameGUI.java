package net.torosamy.torosamyGame.rps;

import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.config.LangConfig;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RPSGameGUI {
    public final Player owner;

    public final Inventory inventory = generateGUI();

    private static final int[] FRAME_ITEM_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34};

    public static final int CONFIRM_ITEM_SLOT = 35;

    public static final int CURRENT_OPTION_SLOT = 13;

    public static final int ROCK_SLOT = 21;

    public static final int PAPER_SLOT = 22;

    public static final int SCISSORS_SLOT = 23;

    private Option option = Option.ROCK;

    private boolean isConfirm = false;

    public RPSGameGUI(Player owner) {
        this.owner = owner;
    }

    public void close() {
        if (!this.owner.isOnline()) {
            return;
        }
        this.owner.closeInventory();
    }

    private static Inventory generateGUI() {
        LangConfig langConfig = ConfigUtil.langConfig;

        Inventory inventory = Bukkit.createInventory(RPSGameGUIHolder.GAME_GUI_HOLDER, 36, MessageUtil.component(langConfig.rpsMenuTitle));

        ItemStack frameItem = getItem(Material.GRAY_STAINED_GLASS_PANE, langConfig.rpsFrameDisplay);
        for (int frameItemSlot : FRAME_ITEM_SLOTS) {
            inventory.setItem(frameItemSlot, frameItem);
        }

        inventory.setItem(ROCK_SLOT, getItem(Material.STONE, langConfig.rpsStoneDisplay));
        inventory.setItem(PAPER_SLOT, getItem(Material.PAPER, langConfig.rpsPaperDisplay));
        inventory.setItem(SCISSORS_SLOT, getItem(Material.SHEARS, langConfig.rpsScissorsDisplay));
        inventory.setItem(CURRENT_OPTION_SLOT, getItem(Material.NETHER_STAR, langConfig.rpsOptionDisplay.replaceAll("%option%", Option.ROCK.getDisplayName())));
        inventory.setItem(CONFIRM_ITEM_SLOT, getItem(Material.NETHER_STAR, langConfig.rpsConfirmDisplay));
        return inventory;
    }

    private static ItemStack getItem(Material material, String name) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.itemName(MessageUtil.component(name));
        item.setItemMeta(meta);
        return item;
    }

    public void updateOption(Option option) {
        this.option = option;
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.itemName(MessageUtil.component(ConfigUtil.langConfig.rpsOptionDisplay.replaceAll("%option%", option.getDisplayName())));
        item.setItemMeta(meta);
        this.inventory.setItem(CURRENT_OPTION_SLOT, item);
    }


    public Option getOption() {
        return this.option;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public void confirm() {
        isConfirm = true;
        ItemStack item = this.inventory.getItem(CONFIRM_ITEM_SLOT);
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        meta.itemName(MessageUtil.component(ConfigUtil.langConfig.rpsFinishDisplay));
        item.setItemMeta(meta);
        
        this.inventory.setItem(CONFIRM_ITEM_SLOT, item);
    }

    public enum Option {
        ROCK,
        PAPER,
        SCISSORS;

        public String getDisplayName() {
            LangConfig config = ConfigUtil.langConfig;
            if (this == SCISSORS) {
                return config.rpsScissorsDisplay;
            }

            if (this == PAPER) {
                return config.rpsPaperDisplay;
            }

            return config.rpsStoneDisplay;
        }

        public enum Result {
            WIN,
            LOSE,
            TIE;
        }

        public Result result(Option other) {
            if (this == other) {
                return Result.TIE;
            }

            return switch (this) {
                case ROCK -> (other == SCISSORS) ? Result.WIN : Result.LOSE;
                case PAPER -> (other == ROCK) ? Result.WIN : Result.LOSE;
                case SCISSORS -> (other == PAPER) ? Result.WIN : Result.LOSE;
            };
        }
    }
}
