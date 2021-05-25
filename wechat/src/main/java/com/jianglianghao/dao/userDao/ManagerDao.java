package com.jianglianghao.dao.userDao;

import com.jianglianghao.bean.DelayBan;
import com.jianglianghao.bean.DelayQuenueManager;
import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.Ban;
import com.jianglianghao.entity.Circle;
import com.jianglianghao.entity.User;

import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1521:23
 */

public class ManagerDao {

    /**
     * 获取所有用户在管理员界面展示
     * @return list集合
     * @throws Exception 异常
     */
    public List<User> getAllUser() throws Exception{
        String sql = "select id id, name name, account account, email email, user_kind userKind, head_protrait headProtrait from user";
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<User> allInstances = instance.getAllInstances(User.class, sql);
        return allInstances;
    }

    /**
     * 对用户解封
     * @param ban 实体类
     * @return 结果
     * @throws Exception 异常
     */
    public int bandUser(Ban ban) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        Ban ban1 = new Ban(0, ban.getBeBannedPeople(), ban.getBanPeople(), null);
        List<Ban> seek = instance.seek(ban1);
        if(seek.size() != 0){
            //证明已经封号过了
            return 0;
        }else{
            //证明没有封号
            instance.add(ban);
            return 1;
        }
    }

    /**
     * 对用户解封
     * @param ban 实体类
     * @return 结果
     * @throws Exception 异常
     */
    public int unBandUser(Ban ban) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<Ban> seek = instance.seek(ban);
        if(seek.size() == 0){
            //证明没有找到，那么
            return 0;
        }else{
            //从延时队列中取出来
            DelayQuenueManager instance1 = DelayQuenueManager.getInstance();
            Ban ban1 = seek.get(0);
            instance1.remove(new DelayBan(ban1.getBeBannedPeople(), ban1.getBanPeople(), null, ban1.getTime()));
            instance.delete(ban);
            return 1;
        }
    }

    /**
     * 删除朋友圈
     * @param circle 朋友圈实体类
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteCircle(Circle circle) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.delete(circle);
        return 1;
    }

    /**
     * 去除管理员权限
     * @param user 用户实体类
     * @param account 账号，根据账号修改权限
     * @return 修改结果
     * @throws Exception 异常
     */
    public int banManager(User user, String account)throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(user, "account="+account);
        return 1;
    }

    /**
     * 找到被封号的人的所有消息
     * @param ban 被封号的实体类
     * @return
     */
    public List<Ban> finaBan(Ban ban) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<Ban> seek = instance.seek(ban);
        return seek;
    }
}
