package team.allchat.two.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import team.allchat.two.server.command.Command;
import team.allchat.two.server.command.core.HealthCommand;
import team.allchat.two.server.command.core.LoginCommand;
import team.allchat.two.server.command.core.RegisterCommand;

import java.util.*;

@Slf4j
public class ChatServerHandler extends IoHandlerAdapter {
    static ArrayList<Command> commandreg = new ArrayList<>();
    public static Map<String, UUID> tokenlist= new HashMap<>();

    public static class healthuserclass{
        public Timer timer;
        public String username;
    }
    public static ArrayList<healthuserclass> onlineUser = new ArrayList<>();

    public static void RegACommand(Command c){
        commandreg.add(c);
    }

    public static ArrayList<String> CommandRegList(){
        ArrayList<String> a = new ArrayList<>();
        for (Command command : commandreg) {
            a.add(command.regid);
        }
        return a;
    }

    public static void RegisterCommand(){
        ArrayList<Command> reg = new ArrayList<>();

        reg.add(new RegisterCommand()); //注册
        reg.add(new LoginCommand()); //登录
        reg.add(new HealthCommand()); //保持在线

        for (Command command : reg) {
            RegACommand(command);
        }
    }



    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.info("a msg received");
        long begin = System.currentTimeMillis();
        String[] msg = ((String)message).split(" ");
        for (Command command : commandreg) {
            if(("/"+command.regid).equals(msg[0])){
                if(msg.length < 2) command.run(null,session);
                else{
                    if(!command.run(msg[1],session)){
                        session.write(command.usage+"\n");
                    }
                }
                break;
            }
        }
        //session.write("Command Finish"+"("+(System.currentTimeMillis() - begin)+" ms)");
        log.info("a command finish in "+(System.currentTimeMillis() - begin) + "ms");
    }

    public static void UserReOnline(String uname){
        boolean a = true;
        for (ChatServerHandler.healthuserclass healthuserclass : ChatServerHandler.onlineUser) {
            if(healthuserclass.username.equals(uname)){
                healthuserclass.timer.cancel();
                ChatServerHandler.onlineUser.remove(healthuserclass);
                a = false;
                break; //删除用户在线状态
            }
        }
        if(a){
            log.info("a user online! name: "+uname);
        }
        ChatServerHandler.healthuserclass hhc = new ChatServerHandler.healthuserclass();
        hhc.timer = new Timer();
        hhc.username = uname;
        hhc.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("a user offline! name: "+hhc.username);
                ChatServerHandler.onlineUser.remove(hhc);
            }
        },4000);
        ChatServerHandler.onlineUser.add(hhc);
    }
}
