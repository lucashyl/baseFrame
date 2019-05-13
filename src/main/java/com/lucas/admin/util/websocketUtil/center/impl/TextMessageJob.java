package com.lucas.admin.util.websocketUtil.center.impl;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * 消息类
 * Created by hyl on 2019/2/14.
 */
public class TextMessageJob {
    private WebSocketSession session;

    private String messageBody;

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public TextMessageJob(WebSocketSession session, String messageBody) {
        this.session = session;
        this.messageBody = messageBody;
    }

    public TextMessageJob() {
    }

    /**
     * 发送
     * @param messageJob
     * @throws IOException
     */
    public void send(TextMessageJob messageJob) throws IOException {
        WebSocketSession s = messageJob.getSession();
//        synchronized (s) {
            if (s.isOpen()) {
            session.sendMessage(new TextMessage(messageJob.messageBody));
            } else {
//            }
        }
    }

}
