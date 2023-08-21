package team.allchat.two.server.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import team.allchat.two.server.command.core.RegisterCommand;

import java.util.ArrayList;

@Slf4j
public class CommandControl extends IoHandlerAdapter {
    static ArrayList<Command> commandreg = new ArrayList<>();

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
        session.write("Command Finish"+"("+(System.currentTimeMillis() - begin)+" ms)");
        log.info("a command finish in "+(System.currentTimeMillis() - begin) + "ms");
    }
}
