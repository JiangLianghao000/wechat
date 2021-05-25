package com.jianglianghao.test;

import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.Ban;
import com.jianglianghao.entity.User;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1720:18
 */

public class delayTest {

    public static void main(String[] args) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        BlockingQueue<DelayTask> q = new DelayQueue();

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                Ban ban = new Ban(0, "aaaa", "bbbb", DateFormat.getDateTimeInstance().format(new Date()));
                try {
                    instance.add(ban);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread2 = new Thread(){
            @Override
            public void run() {
                Ban ban = new Ban(0, "aaaa", "bbbb", DateFormat.getDateTimeInstance().format(new Date()));
                try {
                    instance.add(ban);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        DelayTask t1 = new DelayTask(10000, thread2);
        DelayTask t2 = new DelayTask(30000, thread1);
        q.offer(t1);
        q.offer(t2);

        for (int i = 0; i < 2; i++) {
            DelayTask take = q.take();
            // 开启线程，执行任务
            take.data.start();
            instance.delete(null);
        }

    }

    static class DelayTask implements Delayed {
        long delayTime; // 延迟时间
        long expire; // 过期时间
        Thread data;

        public DelayTask(long delayTime, Thread data) {
            this.delayTime = delayTime;
            this.data = data;
            // 过期时间 = 当前时间 + 延迟时间
            this.expire = System.currentTimeMillis() + delayTime;
        }

        /**
         * 优先级规则：两个任务比较，时间短的优先执行
         */
        @Override
        public int compareTo(Delayed o) {
            long f = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
            return (int) f;
        }

        /**
         * 剩余时间 = 到期时间 - 当前时间
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public String toString() {
            return "线程：" + data.getName() + "执行；延迟时间为：" + delayTime + ";";
        }
    }

}
