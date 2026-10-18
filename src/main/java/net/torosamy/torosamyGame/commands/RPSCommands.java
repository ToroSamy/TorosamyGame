package net.torosamy.torosamyGame.commands;

import net.torosamy.torosamyCore.request.BaseRequest;
import net.torosamy.torosamyCore.request.RequestManager;
import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.rps.RPSRequest;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;

public class RPSCommands {
    @Command(value = "game rps send <player>")
    @Permission("torosamygame.rps.use")
    public void send(Player sender, @Argument("player")Player receiver) {
        if (sender.getName().equals(receiver.getName())) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.selfError));
            return;
        }

        RequestManager.getInstance().sendRequest(new RPSRequest(sender, receiver));
    }

    @Command(value = "game rps cancel")
    @Permission("torosamygame.rps.use")
    public void cancel(Player sender) {
        RequestManager instance = RequestManager.getInstance();

        RPSRequest request = instance.getRequestBySenderName(sender.getName(), RPSRequest.class);

        if (request == null) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.rpsNotFound));
            return;
        }

        instance.cancelRequest(request);
    }

    @Command(value = "game rps accept")
    @Permission("torosamygame.rps.use")
    public void accept(Player receiver) {
        RequestManager instance = RequestManager.getInstance();

        RPSRequest request = instance.getRequestByReceiverName(receiver.getName(), RPSRequest.class);

        if (request == null) {
            receiver.sendMessage(MessageUtil.component(ConfigUtil.langConfig.rpsNotFound));
            return;
        }

        instance.acceptRequest(request);
    }

    @Command(value = "game rps deny")
    @Permission("torosamygame.rps.use")
    public void deny(Player receiver) {
        RequestManager instance = RequestManager.getInstance();

        RPSRequest request = instance.getRequestByReceiverName(receiver.getName(), RPSRequest.class);

        if (request == null) {
            receiver.sendMessage(MessageUtil.component(ConfigUtil.langConfig.rpsNotFound));
            return;
        }

        instance.denyRequest(request);
    }
}
