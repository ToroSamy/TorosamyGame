package net.torosamy.torosamyGame.rps;



import net.torosamy.torosamyCore.api.TorosamyCoreAPI;
import net.torosamy.torosamyGame.TorosamyGame;
import net.torosamy.torosamyGame.config.MainConfig;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;


public class RPSGame extends BukkitRunnable {
    private static final List<RPSGame> GAMES = new ArrayList<>();

    private final RPSGameGUI senderGUI;

    private final RPSGameGUI receiverGUI;

    private int counts = 0;

    private RPSGame(Player sender, Player receiver) {
        this.senderGUI = new RPSGameGUI(sender);
        this.receiverGUI = new RPSGameGUI(receiver);
    }

    public static void delete(String name) {
        GAMES.removeIf(game -> {
            if (game.isMember(name)) {
                game.cancel();
                return true;
            }
            return false;
        });
    }

    public boolean isMember(String name) {
        return this.receiverGUI.owner.getName().equals(name) ||
                this.senderGUI.owner.getName().equals(name);
    }

    public static RPSGame startGame(Player sender, Player receiver) {
        for (RPSGame game : GAMES) {
            if (game.isMember(sender.getName()) || game.isMember(receiver.getName())) {
                return null;
            }
        }

        RPSGame game = new RPSGame(sender, receiver);

        game.runTaskTimer(TorosamyGame.instance, 0L, 20L);

        GAMES.add(game);
        return game;
    }

    public static RPSGame getGame(String name) {
        for (RPSGame game : GAMES) {
            if (game.isMember(name)) {
                return game;
            }
        }
        return null;
    }

    public void delete() {
        GAMES.remove(this);
        this.cancel();
    }

    public void openSenderGUI(Player player) {
        player.openInventory(senderGUI.inventory);
    }

    public void openReceiverGUI(Player player) {
        player.openInventory(receiverGUI.inventory);
    }

    public void execute() {
        MainConfig config = ConfigUtil.mainConfig;

        RPSGameGUI.Option.Result result = senderGUI.getOption().result(receiverGUI.getOption());

        switch (result) {
            case TIE -> {
                TorosamyCoreAPI.runCommands(senderGUI.owner, config.rpsTieCommands);
                TorosamyCoreAPI.runCommands(receiverGUI.owner, config.rpsTieCommands);
            }
            case WIN -> {
                TorosamyCoreAPI.runCommands(senderGUI.owner, config.rpsWinCommands);
                TorosamyCoreAPI.runCommands(receiverGUI.owner, config.rpsLoseCommands);
            }
            case LOSE -> {
                TorosamyCoreAPI.runCommands(senderGUI.owner, config.rpsLoseCommands);
                TorosamyCoreAPI.runCommands(receiverGUI.owner, config.rpsWinCommands);
            }
        }
    }
    
    public RPSGameGUI getGameGUI(String name) {
        if (senderGUI.owner.getName().equals(name)) {
            return senderGUI;
        }

        if (receiverGUI.owner.getName().equals(name)) {
            return receiverGUI;
        }

        return null;
    }

    public Player getSender() {
        return senderGUI.owner;
    }

    public Player getReceiver() {
        return receiverGUI.owner;
    }

    @Override
    public void run() {
        if (!this.getReceiver().isOnline() || !this.getSender().isOnline()) {
            delete();
            receiverGUI.close();
            senderGUI.close();
            return;
        }
        
        if (this.receiverGUI.isConfirm() && this.senderGUI.isConfirm()) {
            delete();
            execute();

            receiverGUI.close();
            senderGUI.close();
            return;
        }

        if (this.counts >= ConfigUtil.mainConfig.rpsGameFinishSecond) {
            delete();
            execute();

            receiverGUI.close();
            senderGUI.close();
            return;
        }

        this.counts ++;
    }
}
