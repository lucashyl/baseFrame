package com.lucas.shop.dao;

/**
 * <p>Description: 消息推送数据封装类</p>
 */
public class MessageDTO {

	private String messageType;    //消息类型
	private String content;        //消息内容

	public MessageDTO(String messageType, String content) {
		this.messageType = messageType;
		this.content = content;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
