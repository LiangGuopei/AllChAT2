package team.allchat.two.server.command.core;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;
import team.allchat.two.server.command.Command;
import team.allchat.two.server.handler.ChatServerHandler;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Slf4j
public class HealthCommand extends Command {
    public HealthCommand(){
        super.regid = "health";
    }
    @Override
    public boolean run(String arg, IoSession iosession) {
        JSONObject object;
        if(arg == null) return false;
        try{
            object = JSONObject.parseObject(arg);
        }catch (Exception e){
            return false;
        }
        String ifstr = object.getString("token");
        if(!ifstr.matches("\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}")) return false;
        boolean a = false;
        String uname = "";
        for (Map.Entry<String, UUID> entry : ChatServerHandler.tokenlist.entrySet()) {
            a = entry.getValue().toString().equals(ifstr);
            uname = entry.getKey();
            if(a) break;
        }
        if(!a) return false;
        ChatServerHandler.UserReOnline(uname);
        return true;
    }
}
