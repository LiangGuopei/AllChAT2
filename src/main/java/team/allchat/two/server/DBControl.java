package team.allchat.two.server;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class DBControl {

    public enum dbClass{
        user("./db/user/"),
        chat("./db/chat/"),
        usercache(".db/usercache/");
        private String url;
        dbClass(String url){
            this.url = url;
        }

        @Override
        public String toString() {
            return url;
        }
    }

    public static boolean init(){
        new File("./db").mkdir();
        new File("./db/user").mkdir();
        new File("./db/chat").mkdir();
        new File("./db/usercache").mkdir();
        log.info("init db success");

        return true;
    }

    public static boolean WriteAJsonToDB(dbClass dbc, String file, JSONObject object){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(dbc+file));
            bw.write(object.toJSONString());
            bw.close();
            log.info("success write a json to db!");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String ReadAFileFromDB(dbClass dbc,String file){
        try{
            BufferedReader br = new BufferedReader(new FileReader(dbc+file));
            String a = "";
            String l;
            while(((l = br.readLine()) != null)){
                a += l;
            }
            br.close();
            log.info("success read a json from db!");
            return a;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean HasThisData(dbClass dbc, String f){
        return new File(dbc+f).exists();
    }
}
