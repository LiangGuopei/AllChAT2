package team.allchat.two.server.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class UserJson {
    @JSONField(name = "username")
    public String username;

    @JSONField(name = "password")
    public String password;

    public UserJson(String a, String h){
        username = a;
        password = h;
    }

    public JSONObject ToJsonObject(){
        return JSONObject.parseObject(JSON.toJSONString(this));
    }

    public static UserJson FromStringToUserJson(String a){
        try{
            JSONObject object = JSONObject.parseObject(a);
            return new UserJson(object.getString("username"),object.getString("password"));
        }catch (Exception e){
            return null;
        }
    }
}
