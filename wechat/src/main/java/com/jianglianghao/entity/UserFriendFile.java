package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/723:00
 */

public class UserFriendFile {
    private int id;
    private int userId;
    private int friendId;
    private String fileType;
    private String file;


    /**
     * 特殊构造器
     * @param s 参数
     */
    public UserFriendFile(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("userId")){
                this.userId = Integer.parseInt(split[1].trim());
                continue;
            }
            if(split[0].trim().equals("id")){
                this.id = Integer.parseInt(split[1].trim());
                continue;
            }
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

    public UserFriendFile() {
    }

    @Override
    public String toString() {
        return "UserFriendFile{" +
                "id=" + id +
                ", userId=" + userId +
                ", friendId=" + friendId +
                ", fileType='" + fileType + '\'' +
                ", file='" + file + '\'' +
                '}';
    }

    public UserFriendFile(int id, int userId, int friendId, String fileType, String file) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.fileType = fileType;
        this.file = file;
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
