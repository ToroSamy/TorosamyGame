package net.torosamy.torosamyGame.config;

import net.torosamy.torosamyCore.config.IConfigManage;

import java.util.List;

public class MainConfig implements IConfigManage {
    public Integer rpsRequestAutoCancelSecond;
    public Integer rpsGameFinishSecond;
    public List<String> rpsTieCommands;
    public List<String> rpsWinCommands;
    public List<String> rpsLoseCommands;


    public Integer fishRequestAutoCancelSecond;
    public Integer fishGameFinishSecond;
    public List<String> fishTieCommands;
    public List<String> fishWinCommands;
    public List<String> fishLoseCommands;

    public Integer snowRequestAutoCancelSecond;
    public Integer snowGameFinishSecond;
    public List<String> snowTieCommands;
    public List<String> snowWinCommands;
    public List<String> snowLoseCommands;
}
