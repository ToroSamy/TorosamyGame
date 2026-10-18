package net.torosamy.torosamyGame.commands;

import net.torosamy.torosamyCore.request.RequestManager;
import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.fish.FishRequest;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;

public class FishCommands {
    @Command(value = "game fish send <player>")
    @Permission("torosamygame.fish.use")
    public void send(Player sender, @Argument("player")Player receiver) {
        if (sender.getName().equals(receiver.getName())) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.selfError));
            return;
        }

        RequestManager.getInstance().sendRequest(new FishRequest(sender, receiver));
    }

    @Command(value = "game fish cancel")
    @Permission("torosamygame.fish.use")
    public void cancel(Player sender) {
        RequestManager instance = RequestManager.getInstance();

        FishRequest request = instance.getRequestBySenderName(sender.getName(), FishRequest.class);

        if (request == null) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.fishNotFound));
            return;
        }

        instance.cancelRequest(request);
    }

    @Command(value = "game fish accept")
    @Permission("torosamygame.fish.use")
    public void accept(Player receiver) {
        RequestManager instance = RequestManager.getInstance();

        FishRequest request = instance.getRequestByReceiverName(receiver.getName(), FishRequest.class);

        if (request == null) {
            receiver.sendMessage(MessageUtil.component(ConfigUtil.langConfig.fishNotFound));
            return;
        }

        instance.acceptRequest(request);
    }

    @Command(value = "game fish deny")
    @Permission("torosamygame.fish.use")
    public void deny(Player receiver) {
        RequestManager instance = RequestManager.getInstance();

        FishRequest request = instance.getRequestByReceiverName(receiver.getName(), FishRequest.class);

        if (request == null) {
            receiver.sendMessage(MessageUtil.component(ConfigUtil.langConfig.fishNotFound));
            return;
        }

        instance.denyRequest(request);
    }
}
