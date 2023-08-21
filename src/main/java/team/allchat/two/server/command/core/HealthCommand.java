package team.allchat.two.server.command.core;

import com.alibaba.fastjson.JSONObject;
import org.apache.mina.core.session.IoSession;
import team.allchat.two.server.command.Command;

public class HealthCommand extends Command {
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
    }
}
