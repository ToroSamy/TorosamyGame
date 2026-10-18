package net.torosamy.torosamyGame.utils;


import net.torosamy.torosamyGame.TorosamyGame;
import net.torosamy.torosamyGame.listener.FishingListener;
import net.torosamy.torosamyGame.listener.QuitListener;
import net.torosamy.torosamyGame.listener.RPSGameGUIListener;
import net.torosamy.torosamyGame.listener.SnowballListener;


public class ListenerUtil {
    public static void registerListener() {
        TorosamyGame.instance.getServer().getPluginManager().registerEvents(new QuitListener(), TorosamyGame.instance);
        TorosamyGame.instance.getServer().getPluginManager().registerEvents(new RPSGameGUIListener(), TorosamyGame.instance);
        TorosamyGame.instance.getServer().getPluginManager().registerEvents(new FishingListener(), TorosamyGame.instance);
        TorosamyGame.instance.getServer().getPluginManager().registerEvents(new SnowballListener(), TorosamyGame.instance);
     }
}
