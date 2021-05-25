package com.jianglianghao.view.listen;

import com.jianglianghao.bean.DelayBan;
import com.jianglianghao.bean.DelayQuenueManager;
import com.jianglianghao.controller.ManagerController;
import com.jianglianghao.controller.UserMsgController;
import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.Ban;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.text.SimpleDateFormat;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 监听项目启动，运行封号检测
 * @verdion
 * @date 2021/5/1922:35
 */

public class ApplicationInit implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //项目启动的时候，创建一个线程来循环延迟数组，取出结束的对象
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取延迟队列管理器
        DelayQuenueManager delayQuenueManager = DelayQuenueManager.getInstance();
        UserDaoImpl instance = UserDaoImpl.getInstance();

        Thread checkThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    //死循环检测是否有过时的
                    // 取出队列
                    DelayBan delayBan;
                    try {
                        delayBan = delayQuenueManager.getTake();
                        if(delayBan != null){
                            //证明取出来了
                            //调用dao方法去删除数据库
                            Ban ban = new Ban();
                            ban.setBeBannedPeople(delayBan.getBeBannedPeople());
                            //调用controller方法删除数据库
                            new ManagerController().unBandUser(ban);
                            System.out.println("封号结束");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("程序结束了");
    }
}
