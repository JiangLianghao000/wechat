package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1315:55
 */

public class CircleCommentInComment {
    //该评论的id
    private int commentId;
    //该动态内容id
    private int contentId;
    //该动态是谁的
    private String userName;
    //评论的评论
    private String commentForComment;
    //评论的评论的id
    private int commentForCommentId;
    //评论的评论是谁发的
    private String commentForCommentName;
    //该评论是谁发的
    private String publicName;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CircleCommentInComment(int commentId, int contentId, String userName, String commentForComment, int commentForCommentId, String commentForCommentName, String publicName, String time) {
        this.commentId = commentId;
        this.contentId = contentId;
        this.userName = userName;
        this.commentForComment = commentForComment;
        this.commentForCommentId = commentForCommentId;
        this.commentForCommentName = commentForCommentName;
        this.publicName = publicName;
        this.time = time;
    }

    @Override
    public String toString() {
        return "CircleCommentInComment{" +
                "commentId=" + commentId +
                ", contentId=" + contentId +
                ", userName='" + userName + '\'' +
                ", commentForComment='" + commentForComment + '\'' +
                ", commentForCommentId=" + commentForCommentId +
                ", commentForCommentName='" + commentForCommentName + '\'' +
                ", publicName='" + publicName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    /**
     * 特殊构造器
     * @param s 参数
     */
    public CircleCommentInComment(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("contentId")){
                this.contentId = Integer.parseInt(split[1].trim());
                continue;
            }
            //对int单独处理
            if(split[0].trim().equals("commentId")){
                this.commentId = Integer.parseInt(split[1].trim());
                continue;
            }
            if(split[0].trim().equals("commentForCommentId")){
                this.commentForCommentId = Integer.parseInt(split[1].trim());
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


    public CircleCommentInComment() {
    }


    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentForComment() {
        return commentForComment;
    }

    public void setCommentForComment(String commentForComment) {
        this.commentForComment = commentForComment;
    }

    public int getCommentForCommentId() {
        return commentForCommentId;
    }

    public void setCommentForCommentId(int commentForCommentId) {
        this.commentForCommentId = commentForCommentId;
    }

    public String getCommentForCommentName() {
        return commentForCommentName;
    }

    public void setCommentForCommentName(String commentForCommentName) {
        this.commentForCommentName = commentForCommentName;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }
}
