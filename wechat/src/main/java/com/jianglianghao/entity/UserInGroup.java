package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/618:43
 */

public class UserInGroup {
    private int id;
    //用户id
    private int userId;
    //群账号
    private String groupAccount;
    //群备注
    private String groupNickname;
    //用户名字
    private String userNickname;
    //是否被群聊拉黑
    private String isGroupBlacklist;
    //在群里面的昵称
    private String groupIdentity;
    //群名字
    private String groupName;
    private String userName;
    private String userAccount;
    //状态
    private String state;
    //是否禁言
    private String banSay;
    //群公告
    private String announce;



    /**
     * 特殊构造器
     * @param s 参数
     */
    public UserInGroup(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("id")){
                this.id = Integer.parseInt(split[1].trim());
                continue;
            }
            if(split[0].trim().equals("userId")){
                this.userId = Integer.parseInt(split[1].trim());
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

    public UserInGroup() {
    }

    public UserInGroup(int id, int userId, String groupAccount, String groupNickname, String userNickname, String isGroupBlacklist, String groupIdentity, String groupName, String userName, String userAccount, String state, String banSay, String announce) {
        this.id = id;
        this.userId = userId;
        this.groupAccount = groupAccount;
        this.groupNickname = groupNickname;
        this.userNickname = userNickname;
        this.isGroupBlacklist = isGroupBlacklist;
        this.groupIdentity = groupIdentity;
        this.groupName = groupName;
        this.userName = userName;
        this.userAccount = userAccount;
        this.state = state;
        this.banSay = banSay;
        this.announce = announce;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGroupAccount() {
        return groupAccount;
    }

    public void setGroupAccount(String groupAccount) {
        this.groupAccount = groupAccount;
    }

    public String getGroupNickname() {
        return groupNickname;
    }

    public void setGroupNickname(String groupNickname) {
        this.groupNickname = groupNickname;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getIsGroupBlacklist() {
        return isGroupBlacklist;
    }

    public void setIsGroupBlacklist(String isGroupBlacklist) {
        this.isGroupBlacklist = isGroupBlacklist;
    }

    public String getGroupIdentity() {
        return groupIdentity;
    }

    public void setGroupIdentity(String groupIdentity) {
        this.groupIdentity = groupIdentity;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBanSay() {
        return banSay;
    }

    public void setBanSay(String banSay) {
        this.banSay = banSay;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    @Override
    public String toString() {
        return "UserInGroup{" +
                "id=" + id +
                ", userId=" + userId +
                ", groupAccount='" + groupAccount + '\'' +
                ", groupNickname='" + groupNickname + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", isGroupBlacklist='" + isGroupBlacklist + '\'' +
                ", groupIdentity='" + groupIdentity + '\'' +
                ", groupName='" + groupName + '\'' +
                ", userName='" + userName + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", state='" + state + '\'' +
                ", banSay='" + banSay + '\'' +
                ", announce='" + announce + '\'' +
                '}';
    }
}
