package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/231:37
 */

public class Card {
    private int id;
    private String sendPeople;
    private String beSendedPeople;
    private String cardPeople;

    public Card() {
    }

    public Card(int id, String sendPeople, String beSendedPeople, String cardPeople) {
        this.id = id;
        this.sendPeople = sendPeople;
        this.beSendedPeople = beSendedPeople;
        this.cardPeople = cardPeople;
    }

    public Card(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("contentId")){
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSendPeople() {
        return sendPeople;
    }

    public void setSendPeople(String sendPeople) {
        this.sendPeople = sendPeople;
    }

    public String getBeSendedPeople() {
        return beSendedPeople;
    }

    public void setBeSendedPeople(String beSendedPeople) {
        this.beSendedPeople = beSendedPeople;
    }

    public String getCardPeople() {
        return cardPeople;
    }

    public void setCardPeople(String cardPeople) {
        this.cardPeople = cardPeople;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", sendPeople='" + sendPeople + '\'' +
                ", beSendedPeople='" + beSendedPeople + '\'' +
                ", cardPeople='" + cardPeople + '\'' +
                '}';
    }
}
