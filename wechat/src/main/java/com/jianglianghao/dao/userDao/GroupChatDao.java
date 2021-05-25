package com.jianglianghao.dao.userDao;

import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.GroupChat;
import com.jianglianghao.entity.GroupFile;
import com.jianglianghao.entity.UserInGroup;

import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/211:17
 */

public class GroupChatDao {

    /**
     * 添加群聊消息
     * @param groupChat 实体类，装着消息
     * @throws Exception 异常
     */
    public void addGroupChat(GroupChat groupChat) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(groupChat);
    }

    /**
     * 找到在群的所有群聊记录
     * @param groupChat 实体类
     * @throws Exception 异常
     * @return
     */
    public List<GroupChat> findAllRecords(GroupChat groupChat)throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<GroupChat> seek = instance.seek(groupChat);
        return seek;
    }

    /**
     * 删除群聊中所有的记录
     * @param groupChat 实体类
     * @throws Exception 异常
     */
    public void deleteAllGroupRecords(GroupChat groupChat) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.delete(groupChat);
    }

    /**
     * 用户退出群聊
     * @param userInGroup 退出群聊
     * @throws Exception 异常
     */
    public void exitGroup(UserInGroup userInGroup) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.delete(userInGroup);
    }

    /**
     * 踢出群聊
     * @throws Exception 异常
     */
    public int kickOutGroup(UserInGroup userInGroup) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.delete(userInGroup);
        return 1;
    }

    /**
     * 查找表情包
     * @param groupFile 实体类
     * @return 异常
     */
    public List<GroupFile> getMeme(GroupFile groupFile) {
        //获取所有表情包路径
        String sql = "select file_location fileLocation \n" +
                "from group_file \n" +
                "where \n" +
                "(group_account=? and user_name = ?)\n" +
                "or\n" +
                "(group_account='表情包')\n";
        String groupAccount = groupFile.getGroupAccount();
        String userName = groupFile.getUserName();
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<GroupFile> allInstances = instance.getAllInstances(GroupFile.class, sql, groupAccount, userName);
        return allInstances;
    }

    /**
     * 添加图片
     * @param groupFile 群文件实体类
     * @throws Exception 异常
     */
    public void addMeme(GroupFile groupFile) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(groupFile);
        return;
    }
}
