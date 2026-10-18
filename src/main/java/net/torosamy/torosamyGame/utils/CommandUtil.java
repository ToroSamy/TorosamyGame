package net.torosamy.torosamyGame.utils;


import net.torosamy.torosamyCore.commands.CommandManager;
import net.torosamy.torosamyGame.TorosamyGame;
import net.torosamy.torosamyGame.commands.AdminCommands;
import net.torosamy.torosamyGame.commands.FishCommands;
import net.torosamy.torosamyGame.commands.RPSCommands;
import net.torosamy.torosamyGame.commands.SnowCommands;

public class CommandUtil {
    private static final CommandManager commandManager = new CommandManager(TorosamyGame.instance);
    
    public static final AdminCommands ADMIN_COMMANDS = new AdminCommands();
    public static final RPSCommands RPS_COMMANDS = new RPSCommands();
    public static final FishCommands FISH_COMMANDS = new FishCommands();
    public static final SnowCommands SNOW_COMMANDS = new SnowCommands();

    public static void registerCommand() {
        commandManager.annotationParser.parse(ADMIN_COMMANDS);
        commandManager.annotationParser.parse(RPS_COMMANDS);
        commandManager.annotationParser.parse(FISH_COMMANDS);
        commandManager.annotationParser.parse(SNOW_COMMANDS);
    }
}
