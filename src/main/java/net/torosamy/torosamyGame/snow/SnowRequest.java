package net.torosamy.torosamyGame.snow;

import net.torosamy.torosamyCore.request.BaseRequest;
import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.TorosamyGame;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.entity.Player;

public class SnowRequest extends BaseRequest {
    public SnowRequest(Player sender, Player receiver) {
        super(sender, receiver, ConfigUtil.mainConfig.snowRequestAutoCancelSecond);
    }

    @Override
    public void onTryToSendSame() {
        sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.snowRepeat));
    }

    @Override
    public void onSend() {
        String time = String.valueOf(this.timeoutSeconds);

        sender.sendMessage(MessageUtil.component(receiver, ConfigUtil.langConfig.snowSenderMessage.replaceAll("%expired%", time)));
        sender.sendMessage(TorosamyGame.getComponent(
                MessageUtil.format(receiver, ConfigUtil.langConfig.snowSenderCancelMessage),
                MessageUtil.format(receiver, ConfigUtil.langConfig.snowSenderCancelHover),
                MessageUtil.format(receiver, ConfigUtil.langConfig.snowSenderCancelCommand)
        ));

        receiver.sendMessage(MessageUtil.format(sender, ConfigUtil.langConfig.snowReceiverMessage.replaceAll("%expired%", time)));
        receiver.sendMessage(TorosamyGame.getComponent(
                MessageUtil.format(sender, ConfigUtil.langConfig.snowReceiverAcceptMessage),
                MessageUtil.format(sender, ConfigUtil.langConfig.snowReceiverAcceptHover),
                MessageUtil.format(sender, ConfigUtil.langConfig.snowReceiverAcceptCommand)
        ));
        receiver.sendMessage(TorosamyGame.getComponent(
                MessageUtil.format(sender, ConfigUtil.langConfig.snowReceiverDenyMessage),
                MessageUtil.format(sender, ConfigUtil.langConfig.snowReceiverDenyHover),
                MessageUtil.format(sender, ConfigUtil.langConfig.snowReceiverDenyCommand)
        ));
    }

    @Override
    public void onAccept() {
        if (SnowGame.startSnowGame(sender, receiver)) {
            return;
        }
        sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.isGaming));
    }

    @Override
    public void onAutoCancel() {
        if (sender.isOnline()) {
            sender.sendMessage(MessageUtil.component(receiver, ConfigUtil.langConfig.snowSenderExpired));
        }

        if (receiver.isOnline()) {
            receiver.sendMessage(MessageUtil.component(sender, ConfigUtil.langConfig.snowReceiverExpired));
        }
    }

    @Override
    public void onManualCancel() {
        if (sender.isOnline()) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.snowSenderCancel.replace("%player_name%", receiver.getName())));
        }

        if (receiver.isOnline()) {
            receiver.sendMessage(MessageUtil.component(ConfigUtil.langConfig.snowReceiverCancel.replace("%player_name%", sender.getName())));
        }
    }

    @Override
    public void onDeny() {
        sender.sendMessage(MessageUtil.component(receiver, ConfigUtil.langConfig.snowSenderDeny));
        receiver.sendMessage(MessageUtil.component(sender,ConfigUtil.langConfig.snowReceiverDeny));
    }
}
