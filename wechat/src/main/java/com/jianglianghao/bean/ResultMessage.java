package com.jianglianghao.bean;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1623:06
 */

public class ResultMessage {
    //判断是系统消息还是群聊消息
    //true:系统消息 false：推送给某一个人的
    private boolean isSystem;

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    private String fromName;
    //如果系统消息是数组
    private Object message;
    //判断发送的消息是什么类型
    private String messageType;
    //获取用户头像
    private String head;

    public ResultMessage() {
    }

    public boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean system) {
        isSystem = system;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public ResultMessage(boolean isSystem, String fromName, Object message, String messageType, String head) {
        this.isSystem = isSystem;
        this.fromName = fromName;
        this.message = message;
        this.messageType = messageType;
        this.head = head;
    }

    @Override
    public String toString() {
        return "ResultMessage{" +
                "isSystem=" + isSystem +
                ", fromName='" + fromName + '\'' +
                ", message=" + message +
                ", messageType='" + messageType + '\'' +
                ", head='" + head + '\'' +
                '}';
    }
}
