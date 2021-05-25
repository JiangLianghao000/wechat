package com.jianglianghao.bean;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/2123:01
 */

public class GroupMsg {
    private boolean isSystem;
    //消息來自誰
    private String fromName;
    //如果系统消息是数组
    private Object message;
    //判断发送的消息是什么类型
    private String messageType;
    //获取用户头像
    private String head;
    //消息发到哪个群
    private String groupName;

    public boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public GroupMsg(boolean isSystem, String fromName, Object message, String messageType, String head, String groupName) {
        this.isSystem = isSystem;
        this.fromName = fromName;
        this.message = message;
        this.messageType = messageType;
        this.head = head;
        this.groupName = groupName;
    }

    public GroupMsg() {
    }

    @Override
    public String toString() {
        return "GroupMsg{" +
                "isSystem=" + isSystem +
                ", fromName='" + fromName + '\'' +
                ", message=" + message +
                ", messageType='" + messageType + '\'' +
                ", head='" + head + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
