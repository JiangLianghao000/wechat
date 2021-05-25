package com.jianglianghao.dao.userDao;

import com.jianglianghao.bean.CheckMSG;
import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.User;
import com.jianglianghao.entity.UserFriend;
import com.jianglianghao.entity.UserImformation;

import javax.servlet.http.HttpServletRequest;


/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/820:27
 */

public class UserMsgDao {

    /**
     * 把已经阅读过的修改为konw
     * @param userImformation
     * @return 结果
     * @throws Exception 异常
     */
    public CheckMSG hadRead(UserImformation userImformation, String content, HttpServletRequest request) throws Exception{
        User user = (User)request.getSession().getAttribute("loginUser");
        CheckMSG checkMSG = new CheckMSG();
        UserDaoImpl instance = UserDaoImpl.getInstance();
        //根据用户id和消息内容查找
        instance.change(userImformation, "userId=" + user.getId(), "content=" + content);
        checkMSG.setCheckResult("已修改");
        return checkMSG;
    }

    /**
     * 删除一条消息
     * @param userImformation 类
     * @param content 消息内容
     * @return 结果
     * @throws Exception
     */
    public CheckMSG deleteMessage(UserImformation userImformation, String content) throws Exception{
        CheckMSG checkMSG = new CheckMSG();
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.delete(userImformation);
        checkMSG.setCheckResult("已删除");
        return checkMSG;
    }

    /**
     * 反馈信息
     * @param userImformation 实体类
     * @return 结果
     * @throws Exception
     */
    public int addMsgInFeedback(UserImformation userImformation) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(userImformation);
        return 1;
    }

    /**
     * 修改头像
     * @param userFriend
     * @throws Exception
     */
    public void modifyHead(UserFriend userFriend, int userId, int friendId) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(userFriend, "userId="+userId);
        return;
    }
}
