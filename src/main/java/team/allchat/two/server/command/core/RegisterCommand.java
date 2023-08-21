package team.allchat.two.server.command.core;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.mina.core.session.IoSession;
import team.allchat.two.server.DBControl;
import team.allchat.two.server.command.Command;
import team.allchat.two.server.db.UserJson;

import java.util.Arrays;

public class RegisterCommand extends Command {
    public RegisterCommand(){
        super.regid = "register";
    }

    @Override
    public boolean run(String arg, IoSession session) {
        JSONObject object;
        if(arg == null) return false;
        try{
            if((object = JSONObject.parseObject(arg)) == null) return false;
        }catch (Exception e){
            return false;
        }
        String[] ifstr1 = new String[]{object.getString("username"),object.getString("password")};
        {
            for (String s : ifstr1) {
                if(s == null) return false;
                if(!s.matches("\\w+")) return false;
            }
            if(ifstr1[0].equalsIgnoreCase(ifstr1[1])) return false;
        }
        if(DBControl.HasThisData(DBControl.dbClass.user,ifstr1[0]+".json")) return false;
        ifstr1[1] = Arrays.toString(DigestUtils.md5(ifstr1[1]));
        UserJson ujson = new UserJson(ifstr1[0],ifstr1[1]);
        if(!DBControl.WriteAJsonToDB(DBControl.dbClass.user,ifstr1[0]+".json",ujson.ToJsonObject())) return false; //写入数据库
        session.write("{\"type\":\"ok\"}");
        return true;
    }
}
