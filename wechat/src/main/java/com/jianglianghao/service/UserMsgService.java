package com.jianglianghao.service;

import com.jianglianghao.bean.CheckMSG;
import com.jianglianghao.dao.userDao.UserMsgDao;
import com.jianglianghao.entity.UserFriend;
import com.jianglianghao.entity.UserImformation;
import com.jianglianghao.util.SensitiveWordUtil;
import com.jianglianghao.util.storageUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/820:27
 */

public class UserMsgService {

    /**
     * 把已经阅读过的修改为konw
     * @param userImformation
     * @return 结果
     * @throws Exception 异常
     */
    public CheckMSG hadRead(UserImformation userImformation, String content, HttpServletRequest request) throws Exception{
        return new UserMsgDao().hadRead(userImformation, content, request);
    }

    /**
     * 删除一条消息
     * @param userImformation 类
     * @param content 消息内容
     * @return 结果
     * @throws Exception
     */
    public CheckMSG deleteMessage(UserImformation userImformation, String content) throws Exception{
        return new UserMsgDao().deleteMessage(userImformation, content);
    }

    /**
     * 检测有没有敏感信息
     * @param text 反馈信息
     * @throws Exception
     */
    public int checkTextArea(String text) throws Exception{
        //获取所有敏感词集合
        Set<String> badWords = storageUtils.getSensitiveWordsSet();
        //初始化
        SensitiveWordUtil.init(badWords);
        //检测
        boolean result = SensitiveWordUtil.contains(text);
        //判断
        if(result == true){
            //有敏感词
            return 1;
        }else{
            return 0;
        }
    }

    /**
     * 反馈信息
     * @param userImformation 实体类
     * @return 结果
     * @throws Exception
     */
    public int addMsgInFeedback(UserImformation userImformation) throws Exception{
        return new UserMsgDao().addMsgInFeedback(userImformation);
    }

    /**
     * 修改头像
     * @param userFriend
     * @throws Exception
     */
    public void modifyHead(UserFriend userFriend, int userId, int friendId) throws Exception{
        new UserMsgDao().modifyHead(userFriend, userId, friendId);
    }
}
