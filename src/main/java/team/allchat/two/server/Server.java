package team.allchat.two.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.junit.Test;
import team.allchat.two.server.command.CommandControl;
import team.allchat.two.server.db.UserJson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {
        DBControl.init();
        CommandControl.RegisterCommand();
        IoAcceptor acceptor = new NioSocketAcceptor();
        //acceptor.getFilterChain().addLast("logger",new LoggingFilter());
        acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new TextLineCodecFactory(StandardCharsets.UTF_8)));

        acceptor.setHandler(new CommandControl());
        acceptor.bind(new InetSocketAddress(23334));
        log.info("server start in 23334");
    }

    @Test
    public void test1(){
        UserJson userJson = new UserJson("qwq","qwq");
        String o = JSON.toJSONString(userJson);
        log.info(o);
    }

    @Test
    public void test2(){
        JSONObject a = new JSONObject();
        log.info(a.getString("sw"));
    }


}
