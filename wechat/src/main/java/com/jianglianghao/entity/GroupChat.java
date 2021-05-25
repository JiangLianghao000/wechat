package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 群聊记录
 * @verdion
 * @date 2021/5/618:43
 */

public class GroupChat {
    private int id;
    //群聊账号
    private String groupAccount;
    //发信息的用户id
    private String userName;
    //用户发的信息
    private String msg;
    //用户发的信息的类型
    private String msgType;


    /**
     * 特殊构造器
     *
     * @param s 参数
     */
    public GroupChat(Object... s) throws Exception {
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

    public GroupChat(int id, String groupAccount, String userName, String msg, String msgType) {
        this.id = id;
        this.groupAccount = groupAccount;
        this.userName = userName;
        this.msg = msg;
        this.msgType = msgType;
    }

    public GroupChat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupAccount() {
        return groupAccount;
    }

    public void setGroupAccount(String groupAccount) {
        this.groupAccount = groupAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "GroupChat{" +
                "id=" + id +
                ", groupAccount='" + groupAccount + '\'' +
                ", userName='" + userName + '\'' +
                ", msg='" + msg + '\'' +
                ", msgType='" + msgType + '\'' +
                '}';
    }
}
