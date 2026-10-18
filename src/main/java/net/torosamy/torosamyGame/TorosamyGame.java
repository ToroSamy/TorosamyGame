package net.torosamy.torosamyGame;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.utils.CommandUtil;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import net.torosamy.torosamyGame.utils.ListenerUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TorosamyGame extends JavaPlugin {
    public static TorosamyGame instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigUtil.initConfig();
        ConfigUtil.reloadConfig();
        CommandUtil.registerCommand();
//        SchedulerUtil.registerScheduler();
        ListenerUtil.registerListener();

        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&a插件 &eTorosamyGame &a成功开启喵~"));

    }

    @Override
    public void onDisable() {
        ConfigUtil.saveConfig();

        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&c插件 &eTorosamyGame &c成功关闭喵~"));
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&c作者 &eTorosamy|yweiyang"));
    }
    


    public static TextComponent getComponent(String message, String hoverMessage, String command) {
        TextComponent textComponent = new TextComponent(message);

        textComponent.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverMessage))
        );

        textComponent.setClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
        );

        return textComponent;
    }
}
