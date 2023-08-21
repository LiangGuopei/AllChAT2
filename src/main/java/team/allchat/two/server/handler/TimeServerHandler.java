package team.allchat.two.server.handler;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeServerHandler extends IoHandlerAdapter {
    Timer timer = new Timer();
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                session.write(new Date().toString()+"\n");
            }
        },0,1000);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        session.closeOnFlush();
    }
}
