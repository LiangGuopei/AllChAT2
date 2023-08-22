package team.allchat.two.server.command.core;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.mina.core.session.IoSession;
import team.allchat.two.server.DBControl;
import team.allchat.two.server.command.Command;
import team.allchat.two.server.db.UserJson;
import team.allchat.two.server.handler.ChatServerHandler;

import java.util.*;

@Slf4j
public class LoginCommand extends Command {
    public LoginCommand(){
        super.regid = "login";
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
        String[] ifstr1 = new String[]{object.getString("username"),object.getString("password")};
        {
            for (String s : ifstr1) {
                if(s == null) return false;
                if(!s.matches("\\w+")) return false;
            }
            if(ifstr1[0].equalsIgnoreCase(ifstr1[1])) return false;
        }
        String ujsonstr;
        if((ujsonstr = DBControl.ReadAFileFromDB(DBControl.dbClass.user,ifstr1[0]+".json")) == null) return false;  //读取数据库
        UserJson ujson;
        if((ujson = UserJson.FromStringToUserJson(ujsonstr)) == null) return false;
        ifstr1[1] = Arrays.toString(DigestUtils.md5(ifstr1[1])); //获得md5加密密码
        if(!ifstr1[0].equals(ujson.username)) return false;
        if(!ifstr1[1].equals(ujson.password)) return false; //判断用户名与密码是否一致
        for (Map.Entry<String, UUID> entry : ChatServerHandler.tokenlist.entrySet()) {
            if(entry.getKey().equals(ujson.username)){
                ChatServerHandler.tokenlist.remove(ujson.username);  //查询是否有token，有则删除
                break;
            }
        }
        UUID retuuid = UUID.randomUUID();
        ChatServerHandler.tokenlist.put(ujson.username,retuuid);
        //ChatServerHandler.tokenlist.put(ujson.username,UUID.fromString("22e232c4-6ca1-4bec-8078-76d92a2d3e03"));


        iosession.write(String.format("{\"type\":\"ok\",\"token\":\"%s\"}", retuuid));
        log.info("a user login , username: "+ujson.username);

        for (ChatServerHandler.healthuserclass healthuserclass : ChatServerHandler.onlineUser) {
            if(healthuserclass.username.equals(ifstr1[0])){
                healthuserclass.timer.cancel();
                ChatServerHandler.onlineUser.remove(healthuserclass);
                break; //删除用户在线状态
            }
        }
        ChatServerHandler.healthuserclass hhc = new ChatServerHandler.healthuserclass();
        hhc.timer = new Timer();
        hhc.username = ifstr1[0];
        hhc.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("a user offline! name: "+hhc.username);
                ChatServerHandler.onlineUser.remove(hhc);
            }
        },4000);
        ChatServerHandler.onlineUser.add(hhc);


        return true;
    }
}
