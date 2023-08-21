package team.allchat.two.server.command;

import org.apache.mina.core.session.IoSession;

import java.util.logging.LogManager;

public class Command {
    public String regid;
//    指令id
//    /[id]
    public String usage = "{\"type\":\"error\"}";

    public boolean run(String arg, IoSession iosession){
        return true;
    }
//    return true 为正确执行
//    return false 为错误
}
