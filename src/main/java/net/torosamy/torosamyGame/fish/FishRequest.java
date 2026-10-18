package net.torosamy.torosamyGame.fish;

import net.torosamy.torosamyCore.request.BaseRequest;
import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.TorosamyGame;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.entity.Player;

public class FishRequest extends BaseRequest {
    public FishRequest(Player sender, Player receiver) {
        super(sender, receiver, ConfigUtil.mainConfig.fishRequestAutoCancelSecond);
    }

    @Override
    public void onTryToSendSame() {
        sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.fishRepeat));
    }

    @Override
    public void onSend() {
        String time = String.valueOf(this.timeoutSeconds);

        sender.sendMessage(MessageUtil.component(receiver, ConfigUtil.langConfig.fishSenderMessage.replaceAll("%expired%", time)));
        sender.sendMessage(TorosamyGame.getComponent(
                MessageUtil.format(receiver, ConfigUtil.langConfig.fishSenderCancelMessage),
                MessageUtil.format(receiver, ConfigUtil.langConfig.fishSenderCancelHover),
                MessageUtil.format(receiver, ConfigUtil.langConfig.fishSenderCancelCommand)
        ));

        receiver.sendMessage(MessageUtil.format(sender, ConfigUtil.langConfig.fishReceiverMessage.replaceAll("%expired%", time)));
        receiver.sendMessage(TorosamyGame.getComponent(
                MessageUtil.format(sender, ConfigUtil.langConfig.fishReceiverAcceptMessage),
                MessageUtil.format(sender, ConfigUtil.langConfig.fishReceiverAcceptHover),
                MessageUtil.format(sender, ConfigUtil.langConfig.fishReceiverAcceptCommand)
        ));
        receiver.sendMessage(TorosamyGame.getComponent(
                MessageUtil.format(sender, ConfigUtil.langConfig.fishReceiverDenyMessage),
                MessageUtil.format(sender, ConfigUtil.langConfig.fishReceiverDenyHover),
                MessageUtil.format(sender, ConfigUtil.langConfig.fishReceiverDenyCommand)
        ));
    }

    @Override
    public void onAccept() {
        if (FishGame.startFishGame(sender, receiver)) {
            return;
        }
        sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.isGaming));
    }

    @Override
    public void onAutoCancel() {
        if (sender.isOnline()) {
            sender.sendMessage(MessageUtil.component(receiver, ConfigUtil.langConfig.fishSenderExpired));
        }

        if (receiver.isOnline()) {
            receiver.sendMessage(MessageUtil.component(sender, ConfigUtil.langConfig.fishReceiverExpired));
        }
    }

    @Override
    public void onManualCancel() {
        if (sender.isOnline()) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.fishSenderCancel.replace("%player_name%", receiver.getName())));
        }

        if (receiver.isOnline()) {
            receiver.sendMessage(MessageUtil.component(ConfigUtil.langConfig.fishReceiverCancel.replace("%player_name%", sender.getName())));
        }
    }

    @Override
    public void onDeny() {
        sender.sendMessage(MessageUtil.component(receiver, ConfigUtil.langConfig.fishSenderDeny));
        receiver.sendMessage(MessageUtil.component(sender,ConfigUtil.langConfig.fishReceiverDeny));
    }
}
