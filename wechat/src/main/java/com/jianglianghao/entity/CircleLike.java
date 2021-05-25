package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1314:07
 */

public class CircleLike {
    private int id;
    private int contentId;
    private String likePeople;
    private String belikedPeople;


    /**
     * 特殊构造器
     * @param s 参数
     */
    public CircleLike(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("id")){
                this.id = Integer.parseInt(split[1].trim());
                continue;
            }
            if(split[0].trim().equals("contentId")){
                this.contentId = Integer.parseInt(split[1].trim());
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int circleId) {
        this.contentId = circleId;
    }

    public String getLikePeople() {
        return likePeople;
    }

    public void setLikePeople(String likePeople) {
        this.likePeople = likePeople;
    }

    public String getBelikedPeople() {
        return belikedPeople;
    }

    public void setBelikedPeople(String belikedPeople) {
        this.belikedPeople = belikedPeople;
    }

    public CircleLike() {
    }

    public CircleLike(int id, int contentId, String likePeople, String belikedPeople) {
        this.id = id;
        this.contentId = contentId;
        this.likePeople = likePeople;
        this.belikedPeople = belikedPeople;
    }

    @Override
    public String toString() {
        return "CircleLike{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", likePeople='" + likePeople + '\'' +
                ", belikedPeople='" + belikedPeople + '\'' +
                '}';
    }
}
