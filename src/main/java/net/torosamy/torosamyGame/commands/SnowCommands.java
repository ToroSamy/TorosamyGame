package net.torosamy.torosamyGame.commands;

import net.torosamy.torosamyCore.request.RequestManager;
import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.snow.SnowRequest;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;

public class SnowCommands {
    @Command(value = "game snow send <player>")
    @Permission("torosamygame.snow.use")
    public void send(Player sender, @Argument("player")Player receiver) {
        if (sender.getName().equals(receiver.getName())) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.selfError));
            return;
        }

        RequestManager.getInstance().sendRequest(new SnowRequest(sender, receiver));
    }

    @Command(value = "game snow cancel")
    @Permission("torosamygame.snow.use")
    public void cancel(Player sender) {
        RequestManager instance = RequestManager.getInstance();

        SnowRequest request = instance.getRequestBySenderName(sender.getName(), SnowRequest.class);

        if (request == null) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.snowNotFound));
            return;
        }

        instance.cancelRequest(request);
    }

    @Command(value = "game snow accept")
    @Permission("torosamygame.snow.use")
    public void accept(Player receiver) {
        RequestManager instance = RequestManager.getInstance();

        SnowRequest request = instance.getRequestByReceiverName(receiver.getName(), SnowRequest.class);

        if (request == null) {
            receiver.sendMessage(MessageUtil.component(ConfigUtil.langConfig.snowNotFound));
            return;
        }

        instance.acceptRequest(request);
    }

    @Command(value = "game snow deny")
    @Permission("torosamygame.snow.use")
    public void deny(Player receiver) {
        RequestManager instance = RequestManager.getInstance();

        SnowRequest request = instance.getRequestByReceiverName(receiver.getName(), SnowRequest.class);

        if (request == null) {
            receiver.sendMessage(MessageUtil.component(ConfigUtil.langConfig.snowNotFound));
            return;
        }

        instance.denyRequest(request);
    }
}
