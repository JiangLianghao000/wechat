package com.jianglianghao.entity;

import java.lang.reflect.Method;
import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1523:23
 */

public class Ban {
    private int id;
    //被封号的人
    private String beBannedPeople;
    //封号的人
    private String banPeople;
    //封号的时间
    private String time;


    /**
     * 特殊构造器
     * @param s 参数
     */
    public Ban(Object... s) throws Exception {
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

    public Ban() {
    }

    public Ban(int id, String beBannedPeople, String banPeople, String time) {
        this.id = id;
        this.beBannedPeople = beBannedPeople;
        this.banPeople = banPeople;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeBannedPeople() {
        return beBannedPeople;
    }

    public void setBeBannedPeople(String beBannedPeople) {
        this.beBannedPeople = beBannedPeople;
    }

    public String getBanPeople() {
        return banPeople;
    }

    public void setBanPeople(String banPeople) {
        this.banPeople = banPeople;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Ban{" +
                "id=" + id +
                ", beBannedPeople='" + beBannedPeople + '\'' +
                ", banPeople='" + banPeople + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
