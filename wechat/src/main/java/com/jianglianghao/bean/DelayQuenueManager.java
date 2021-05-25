package com.jianglianghao.bean;

import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.Ban;

import javax.swing.text.StyledEditorKit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 管理类，管理延迟队列
 * @verdion
 * @date 2021/5/1922:00
 */

public class DelayQuenueManager {
    //单例模式
    private DelayQuenueManager() {
    }

    //内部创建类的对象
    private volatile static DelayQuenueManager DelayQuenueManager = null;

    //提供对象,同步方法
    public synchronized static DelayQuenueManager getInstance() {
        //同步方法的锁就是当前类本身
        if(DelayQuenueManager == null){
            synchronized (DelayQuenueManager.class){
                if(DelayQuenueManager == null) {
                    DelayQuenueManager = new DelayQuenueManager();
                }
            }
        }
        return DelayQuenueManager;
    }

    // 参数：格式
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 延迟队列
     */
    DelayQueue<DelayBan> delayQueue = new DelayQueue<DelayBan>();

    /**
     * 在封号的时候添加进去
     * @param delayBan
     * @return
     */
    public boolean add(DelayBan delayBan){
        System.out.println("开始封号");
        boolean db = false;
        db = delayQueue.add(delayBan);
        return db;
    }

    /**
     * 时间到的时候移除队列
     * @param delayBan
     * @return
     */
    public boolean remove(DelayBan delayBan){
        //封号结束
        boolean db = false;
        for (DelayBan delayBan1 : delayQueue) {
            // 判断被封号的人是否相等，相等就移除
            if(delayBan1.getBeBannedPeople() == delayBan.getBeBannedPeople()){
                db = delayQueue.remove(delayBan);
            }
        }
        return db;
    }

    /**
     * 获取出队的方法
     * @return  出队的DelayBan
     * @throws Exception 异常
     */
    public DelayBan getTake() throws Exception{
        return delayQueue.take();
    }

    /**
     * 将普通band表单 转换成 队列ban表单数据
     * @param ban 普通的实体类
     * @return 转化后的实体类
     */
    public static DelayBan IndentToDelayIndent(Ban ban){
        try {
            DelayBan delayBan = new DelayBan();
            delayBan.setBanPeople(ban.getBanPeople());
            delayBan.setBeBannedPeople(ban.getBeBannedPeople());
            //格式化时间
            delayBan.setFinalTime(ban.getTime());
            delayBan.setStartTime(format.format(new Date()));
            return delayBan;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
