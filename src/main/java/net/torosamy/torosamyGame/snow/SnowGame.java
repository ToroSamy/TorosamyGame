package net.torosamy.torosamyGame.snow;

import net.torosamy.torosamyCore.api.TorosamyCoreAPI;
import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.TorosamyGame;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class SnowGame extends BukkitRunnable {
    private static final List<SnowGame> TASKS = new ArrayList<>();;

    public int senderScore = 0;

    public int receiverScore = 0;

    private final Player receiver;

    private final Player sender;

    private int second = ConfigUtil.mainConfig.snowGameFinishSecond;

    public static boolean startSnowGame(Player receiver, Player sender) {
        for (SnowGame task : TASKS) {
            if (task.isMember(receiver.getName()) || task.isMember(sender.getName())) {
                return false;
            }
        }

        SnowGame game = new SnowGame(receiver, sender);

        game.runTaskTimer(TorosamyGame.instance, 0L, 20L);

        TASKS.add(game);
        return true;
    }

    public static SnowGame getSnowGame(String username) {
        for (SnowGame task : TASKS) {
            if (task.sender.getName().equals(username)) {
                return task;
            }

            if (task.receiver.getName().equals(username)) {
                return task;
            }
        }

        return null;
    }

    private SnowGame(Player receiver, Player sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    public void addScore(String username) {
        if (this.sender.getName().equals(username)) {
            this.senderScore++;
            return;
        }

        if (this.receiver.getName().equals(username)) {
            this.receiverScore++;
            return;
        }
    }

    public boolean isMember(String username) {
        return this.sender.getName().equals(username) || this.receiver.getName().equals(username);
    }

    private void execute() {
        Player winner = getWinner();
        if (winner == null) {
            TorosamyCoreAPI.runCommands(receiver, ConfigUtil.mainConfig.snowTieCommands);
            TorosamyCoreAPI.runCommands(sender, ConfigUtil.mainConfig.snowTieCommands);
            return;
        }

        if (winner.getName().equals(sender.getName())) {
            TorosamyCoreAPI.runCommands(sender, ConfigUtil.mainConfig.snowWinCommands);
            TorosamyCoreAPI.runCommands(receiver, ConfigUtil.mainConfig.snowLoseCommands);
            return;
        }

        TorosamyCoreAPI.runCommands(receiver, ConfigUtil.mainConfig.snowWinCommands);
        TorosamyCoreAPI.runCommands(sender, ConfigUtil.mainConfig.snowLoseCommands);
    }

    @Override
    public void run() {
        if (!this.sender.isOnline() || !this.receiver.isOnline()) {
            this.cancel();
            TASKS.remove(this);

            return;
        }

        if (this.second <= 0) {
            execute();

            this.cancel();
            TASKS.remove(this);

            return;
        }

        String message = ConfigUtil.langConfig.snowGamingInfo.replaceAll("%duration%", String.valueOf(this.second));

        this.sender.sendActionBar(MessageUtil.component(message.replaceAll("%score%", String.valueOf(senderScore))));
        this.receiver.sendActionBar(MessageUtil.component(message.replaceAll("%score%", String.valueOf(receiverScore))));

        this.second--;
    }



    private Player getWinner() {
        if (this.receiverScore == this.senderScore) {
            return null;
        }

        if (this.receiverScore > this.senderScore) {
            return receiver;
        }

        return sender;
    }
}

