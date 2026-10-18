package net.torosamy.torosamyGame.rps;

import net.torosamy.torosamyCore.request.BaseRequest;
import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.TorosamyGame;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.entity.Player;

public class RPSRequest extends BaseRequest {
    public RPSRequest(Player sender, Player receiver) {
        super(sender, receiver, ConfigUtil.mainConfig.rpsRequestAutoCancelSecond);
    }

    @Override
    public void onTryToSendSame() {
        sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.rpsRepeat));
    }

    @Override
    public void onSend() {
        String time = String.valueOf(this.timeoutSeconds);
        
        sender.sendMessage(MessageUtil.component(receiver, ConfigUtil.langConfig.rpsSenderMessage.replaceAll("%expired%", time)));
        sender.sendMessage(TorosamyGame.getComponent(
                MessageUtil.format(receiver, ConfigUtil.langConfig.rpsSenderCancelMessage),
                MessageUtil.format(receiver, ConfigUtil.langConfig.rpsSenderCancelHover),
                MessageUtil.format(receiver, ConfigUtil.langConfig.rpsSenderCancelCommand)
        ));

        receiver.sendMessage(MessageUtil.format(sender, ConfigUtil.langConfig.rpsReceiverMessage.replaceAll("%expired%", time)));
        receiver.sendMessage(TorosamyGame.getComponent(
                MessageUtil.format(sender, ConfigUtil.langConfig.rpsReceiverAcceptMessage),
                MessageUtil.format(sender, ConfigUtil.langConfig.rpsReceiverAcceptHover),
                MessageUtil.format(sender, ConfigUtil.langConfig.rpsReceiverAcceptCommand)
        ));
        receiver.sendMessage(TorosamyGame.getComponent(
                MessageUtil.format(sender, ConfigUtil.langConfig.rpsReceiverDenyMessage),
                MessageUtil.format(sender, ConfigUtil.langConfig.rpsReceiverDenyHover),
                MessageUtil.format(sender, ConfigUtil.langConfig.rpsReceiverDenyCommand)
        ));
    }

    @Override
    public void onAccept() {
        RPSGame game = RPSGame.startGame(sender, receiver);

        if (game == null) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.isGaming));
            return;
        }

        game.openReceiverGUI(receiver);
        game.openSenderGUI(sender);
    }

    @Override
    public void onAutoCancel() {
        if (sender.isOnline()) {
            sender.sendMessage(MessageUtil.component(receiver, ConfigUtil.langConfig.rpsSenderExpired));
        }

        if (receiver.isOnline()) {
            receiver.sendMessage(MessageUtil.component(sender, ConfigUtil.langConfig.rpsReceiverExpired));
        }
    }

    @Override
    public void onManualCancel() {
        if (sender.isOnline()) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.rpsSenderCancel.replace("%player_name%", receiver.getName())));
        }

        if (receiver.isOnline()) {
            receiver.sendMessage(MessageUtil.component(ConfigUtil.langConfig.rpsReceiverCancel.replace("%player_name%", sender.getName())));
        }
    }

    @Override
    public void onDeny() {
        sender.sendMessage(MessageUtil.component(receiver, ConfigUtil.langConfig.rpsSenderDeny));
        receiver.sendMessage(MessageUtil.component(sender,ConfigUtil.langConfig.rpsReceiverDeny));
    }
}
