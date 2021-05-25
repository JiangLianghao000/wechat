package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1312:45
 */

public class CircleComment {
    //用户朋友圈的id， 用来记录每一条朋友圈的
    private int circleId;

    @Override
    public String toString() {
        return "CircleComment{" +
                "circleId=" + circleId +
                ", commentId=" + commentId +
                ", commentPersonName='" + commentPersonName + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    //评论id，用于回复中的回复,自增长
    private int commentId;
    //评论的人
    private String commentPersonName;
    //评论的内容
    private String commentContent;

    private String time;

    public CircleComment() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CircleComment(int circleId, int commentId, String commentPersonName, String commentContent, String time) {
        this.circleId = circleId;
        this.commentId = commentId;
        this.commentPersonName = commentPersonName;
        this.commentContent = commentContent;
        this.time = time;
    }

    /**
     * 特殊构造器
     * @param s 参数
     */
    public CircleComment(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("circleId")){
                this.circleId = Integer.parseInt(split[1].trim());
                continue;
            }
            //对int单独处理
            if(split[0].trim().equals("commentId")){
                this.commentId = Integer.parseInt(split[1].trim());
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


    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentPersonName() {
        return commentPersonName;
    }

    public void setCommentPersonName(String commentPersonName) {
        this.commentPersonName = commentPersonName;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

}
