package com.jianglianghao.bean;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 浏览器发送给服务器的websocket数据
 * @version 1.0
 * @date 2021/5/1623:04
 */

public class Message {

    private String toName;
    private String Message;
    private String head;
    //消息的类型
    private String type;

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "toName='" + toName + '\'' +
                ", Message='" + Message + '\'' +
                ", head='" + head + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Message(String toName, String message, String head, String type) {
        this.toName = toName;
        Message = message;
        this.head = head;
        this.type = type;
    }
}
