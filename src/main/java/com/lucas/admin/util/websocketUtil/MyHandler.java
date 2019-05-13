package com.lucas.admin.util.websocketUtil;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by hyl on 2019/2/15.
 */
public class MyHandler extends TextWebSocketHandler {


    //建立连接
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SocketSessionUtils.afterConnectionEstablished(session);
    }

    // 消息处理
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
//        // ...
//        System.out.println(message.getPayload());
//
//        WebSocketMessage message1 = new TextMessage("server:"+message);
//        try {
//            session.sendMessage(message1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }



    // 消息传输错误
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        SocketSessionUtils.remove(session);
    }

    //关闭连接
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SocketSessionUtils.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


}
