package com.lucas.admin.util.websocketUtil;

import com.alibaba.fastjson.JSONObject;
import com.lucas.admin.util.websocketUtil.center.impl.JobCenter;
import com.lucas.admin.util.websocketUtil.center.impl.TextMessageJob;
import com.lucas.shop.dao.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket 多线程发送消息
 */
public class SocketSessionUtils {

	private static Logger logger = LoggerFactory.getLogger(SocketSessionUtils.class);
	//存储用户
	private static final Map<Long,WebSocketSession> users;
	//用户标识
	private static final String CLIENT_ID = "id";

	static {
		users = new ConcurrentHashMap<>();
	}

	public static void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Long memberId= getClientId(session);
		if(!StringUtils.isEmpty(memberId)){
			users.put(memberId,session);
		}
//		session.sendMessage(new TextMessage("成功建立socket连接"));
	}

	public static void remove(WebSocketSession session){
		for(Long memberId:users.keySet()){
			if(session.getId().equals(users.get(memberId).getId())){
				users.remove(memberId);
				break ;
			}
		}
	}



	/**
	 * 获取用户标识
	 * @param session
	 * @return
	 */
	private static Long getClientId(WebSocketSession session) {
		try {
			Long clientId = Long.valueOf(String.valueOf( session.getAttributes().get(CLIENT_ID)));
			return clientId;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据value获取key
	 * @return
	 */
	public static Set<WebSocketSession> getMapKey(Map<WebSocketSession,Long> map, List<Long> valueList){
		Set<WebSocketSession> sessionsKey = new HashSet<>();
		for(Long value :valueList){//获取所有的session
			if(!StringUtils.isEmpty(value)){
				for (Map.Entry<WebSocketSession, Long> entry : map.entrySet()) {
					if( value.equals(entry.getValue())){
						sessionsKey.add(entry.getKey());
					}
				}
			}

		}
		return sessionsKey;
	}

	public static void commonSend(WebSocketSession session, String message){
		JobCenter.pushMessage(new TextMessageJob(session,message));
	}


	/**
	 * 单发
	 * @param message
	 * @throws IOException
	 */
	public static void pushMessageSingle(MessageDTO message,Long memberId){
		WebSocketSession session = users.get(memberId);
		if(session.isOpen()){
			commonSend(session, JSONObject.toJSONString(message));
		}
		else{
			logger.info("Id为 "+memberId+" 的用户连接不存在!");
		}
	}

	/**
	 * 群发
	 * @param message
	 * @throws IOException
	 */
	public static void pushMessage(MessageDTO message,List<Long> memberIdList) {
		for(Long memberId:memberIdList){
			WebSocketSession session = users.get(memberId);
			if(session.isOpen()){
				commonSend(session, JSONObject.toJSONString(message));
			}
		}
	}

	/**
	 * 广播信息
	 * @param
	 * @return
	 */
	public static void sendMessageToAllUsers(MessageDTO messageDTO) {
		for(Long memberId:users.keySet()){
			WebSocketSession session = users.get(memberId);
			if (session.isOpen()) {
				commonSend(session,JSONObject.toJSONString(messageDTO));
			}
		}
	}

}
