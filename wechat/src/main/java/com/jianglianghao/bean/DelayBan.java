package com.jianglianghao.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 用来管理封号的延时类
 * @verdion
 * @date 2021/5/1920:43
 */

public class DelayBan implements Delayed {
    //被封号的人
    private String beBannedPeople;
    //封号的人
    private String banPeople;
    //封号的时间：什么时候封号
    private String startTime;
    //封号结束的时间，用来比较
    private String finalTime;

    @Override
    //获取剩余时间
    public long getDelay(TimeUnit unit) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //封号结束时间减去系统时间就是剩下时间
        // 毫秒
        try {
            return unit.convert(sdf.parse(this.finalTime).getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    //两个封号时间进行比较
    public int compareTo(Delayed o) {
        return  this.finalTime.compareTo(((DelayBan)o).finalTime);
    }

    @Override
    public String toString() {
        return "DelayBan{" +
                "beBannedPeople='" + beBannedPeople + '\'' +
                ", banPeople='" + banPeople + '\'' +
                ", startTime='" + startTime + '\'' +
                ", finalTime=" + finalTime +
                '}';
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public DelayBan() {
    }

    public DelayBan(String beBannedPeople, String banPeople, String startTime, String finalTime) {
        this.beBannedPeople = beBannedPeople;
        this.banPeople = banPeople;
        this.startTime = startTime;
        this.finalTime = finalTime;
    }
}
