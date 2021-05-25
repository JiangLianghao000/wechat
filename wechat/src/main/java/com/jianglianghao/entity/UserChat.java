package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1912:21
 */

public class UserChat {
    private int id;
    private String userName;
    private String friendName;
    private String content;
    private String msgType;

    /**
     * 特殊构造器
     * @param s 参数
     */
    public UserChat(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("id")){
                this.id = Integer.parseInt(split[1].trim());
                continue;
            }
            //通过反射获取该属性的set方法
            String setMethod = toSet(split[0]);
            //获取该类的所有方法
            Method[] methods = this.getClass().getMethods();
            for(Method method : methods){
                //判断方法名是否相同
                if(setMethod .equals(method.getName())){
                    method.invoke(this, split[1].trim());
                }
            }
        }
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String type) {
        this.msgType = type;
    }

    @Override
    public String toString() {
        return "UserChat{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", friendName='" + friendName + '\'' +
                ", content='" + content + '\'' +
                ", type='" + msgType + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserChat() {
    }

    public UserChat(int id, String userName, String friendName, String content, String msgType) {
        this.id = id;
        this.userName = userName;
        this.friendName = friendName;
        this.content = content;
        this.msgType = msgType;
    }
}
