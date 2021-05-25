package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/618:43
 */

public class UserFriend {
    private int id;
    //用户id
    private int userId;
    //朋友id
    private int friendId;
    //朋友名字
    private String friendName;
    //朋友备注
    private String friendNote;
    //好友账号
    private String friendAccount;
    //是否拉黑
    private String isBlacklist;
    //朋友邮箱
    private String friend_email;
    //朋友圈权限
    private String ciclePermission;
    //朋友头像
    private String friendHeadportrait;
    //状态
    private String state;


    /**
     * 特殊构造器
     * @param s 参数
     */
    public UserFriend(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            if(split[0].trim().equals("id")){
                this.id = Integer.parseInt(split[1].trim());
                continue;
            }
            //对int单独处理
            if(split[0].trim().equals("userId")){
                this.userId = Integer.parseInt(split[1].trim());
                continue;
            }
            //对int单独处理
            if(split[0].trim().equals("friendId")){
                this.friendId = Integer.parseInt(split[1].trim());
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


    public UserFriend() {
    }

    @Override
    public String toString() {
        return "UserFriend{" +
                "id=" + id +
                ", userId=" + userId +
                ", friendId=" + friendId +
                ", friendName='" + friendName + '\'' +
                ", friendNote='" + friendNote + '\'' +
                ", friendAccount='" + friendAccount + '\'' +
                ", isBlacklist='" + isBlacklist + '\'' +
                ", friend_email='" + friend_email + '\'' +
                ", ciclePermission='" + ciclePermission + '\'' +
                ", friendHeadportrait='" + friendHeadportrait + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public UserFriend(int id, int userId, int friendId, String friendName, String friendNote, String friendAccount, String isBlacklist, String friend_email, String ciclePermission, String friendHeadportrait, String state) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.friendName = friendName;
        this.friendNote = friendNote;
        this.friendAccount = friendAccount;
        this.isBlacklist = isBlacklist;
        this.friend_email = friend_email;
        this.ciclePermission = ciclePermission;
        this.friendHeadportrait = friendHeadportrait;
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendNote() {
        return friendNote;
    }

    public void setFriendNote(String friendNote) {
        this.friendNote = friendNote;
    }

    public String getFriendAccount() {
        return friendAccount;
    }

    public void setFriendAccount(String friendAccount) {
        this.friendAccount = friendAccount;
    }

    public String getIsBlacklist() {
        return isBlacklist;
    }

    public void setIsBlacklist(String isBlacklist) {
        this.isBlacklist = isBlacklist;
    }

    public String getFriend_email() {
        return friend_email;
    }

    public void setFriend_email(String friend_email) {
        this.friend_email = friend_email;
    }

    public String getCiclePermission() {
        return ciclePermission;
    }

    public void setCiclePermission(String ciclePermission) {
        this.ciclePermission = ciclePermission;
    }

    public String getFriendHeadportrait() {
        return friendHeadportrait;
    }

    public void setFriendHeadportrait(String friendHeadportrait) {
        this.friendHeadportrait = friendHeadportrait;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
