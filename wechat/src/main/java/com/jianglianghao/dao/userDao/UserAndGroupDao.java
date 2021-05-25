package com.jianglianghao.dao.userDao;

import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.jianglianghao.util.storageUtils.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/108:53
 */

public class UserAndGroupDao {

    /**
     * 查找用户所在的所有群
     * @param groups 实体类
     * @return list集合
     * @throws Exception 异常
     */
    public List<Groups> findAllGroups(Groups groups) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<Groups> seek = instance.seek(groups);
        return seek;
    }

    /**
     * 创建群聊
     * @param groups entity
     * @throws Exception 异常
     */
    public void createGroup(Groups groups) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(groups);
    }

    /**
     * 看看该用户是否在群中
     * @param userInGroup
     * @return
     * @throws Exception
     */
    public List<UserInGroup> CheckIsInGroup(UserInGroup userInGroup) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        return instance.seek(userInGroup);
    }

    /**
     * 用户添加群聊
     * @param userInGroup 实体类
     * @throws Exception
     */
    public int addGroup(UserInGroup userInGroup) throws Exception{
        //添加进数据库
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(userInGroup);
        return 1;
    }


    /**
     * 获取用户所有的好友
     * @param userFriend
     * @return
     * @throws Exception
     */
    public List<UserFriend> getAllFriend(UserFriend userFriend) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        return instance.seek(userFriend);
    }

    /**
     * 获取用户的所有通知信息
     * @param userImformation 用户消息实体类
     * @return
     * @throws Exception
     */
    public List<UserImformation> getAllMessage(UserImformation userImformation) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        return instance.seek(userImformation);
    }

    /**
     * 获取好友请求信息
     * @param userFriend entity类
     * @return 返回list
     * @throws Exception 异常
     */
    public List<UserFriend> getFriendApplyMsg (UserFriend userFriend) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        return instance.seek(userFriend);
    }


    public List<UserInGroup> findPeopleAddGroup(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("loginUser");
        String sql = " select user_id userId \n" +
                " from user_in_group \n" +
                " where state='wait_for_add' \n" +
                " and group_identity = '群员'\n" +
                " and group_account in (\n" +
                "\t\tselect `group_account` \n" +
                "\t\tfrom `user_in_group` \n" +
                "\t\twhere (group_account = ? AND group_identity = '群主')\n" +
                "\t\tor \t\t\n" +
                "\t\t(group_account = ? AND group_identity = '管理员')\n" +
                "\n" +
                " );";
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<UserInGroup> allInstance = instance.getAllInstances(UserInGroup.class, sql, user.getAccount(), user.getAccount());
        return allInstance;
    }

    /**
     * 同意进群
     * @param userInGroup 实体类
     * @return
     */
    public int userAgreeToInGroup(UserInGroup userInGroup, HttpServletRequest request) throws Exception {
        User user = (User)request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(userInGroup, "userId=" + user.getId());
        return 1;
    }

    /**
     * 找到在用户在哪个群
     * @param userInGroup
     * @throws Exception
     */
    public List<UserInGroup> findUserInGroups(UserInGroup userInGroup) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<UserInGroup> seek = instance.seek(userInGroup);
        return seek;
    }

    /**
     * 退出群聊
     * @param userInGroup 实体类
     */
    public int leftGroup(UserInGroup userInGroup, Groups groups) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        //删除用户在群的信息
        instance.delete(userInGroup);
        //删除群消息
        instance.delete(groups);
        return 1;
    }

    /**
     * 调用数据库查询是否已存在，群名是否相同，是否包含敏感内容
     * @param groups
     * @param userInGroup
     */
    public int mofidyGroupMsg(Groups groups, UserInGroup userInGroup, HttpServletRequest request) throws Exception {
        Groups groups1 = (Groups) request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(groups, "userId="+user.getId(), "groupName="+groups1.getGroupName());
        if(!userInGroup.getGroupNickname().equals("null") || !userInGroup.getUserNickname().equals("null")){
            instance.change(userInGroup, "groupName="+groups1.getGroupName(), "userName="+user.getName());
        }
        return 6;
    }

    /**
     * 修改群聊信息
     * @param userInGroup 实体类
     * @return
     * @throws Exception
     */
    public int mofidyGroupMsg1(UserInGroup userInGroup, HttpServletRequest request) throws Exception {
        Groups groups = (Groups) request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserDaoImpl instance = UserDaoImpl.getInstance();
        if (userInGroup.getGroupNickname() != null || userInGroup.getUserNickname() != null) {
            instance.change(userInGroup, "groupName=" + groups.getGroupName(), "userName=" + user.getName());
            return 9;
        }
        return -1;
    }

    /**
     * 修改群头像
     * @param groups
     * @throws Exception
     */
    public void modifyGroupHead(Groups groups, HttpServletRequest request) throws Exception{
        Groups groups1 = (Groups) request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(groups, "userId="+user.getId(), "groupName="+groups1.getGroupName());
        return;
    }

    /**
     * 修改群里的成员为管理员
     * @param userInGroup
     * @param userName
     * @param groupName
     * @return
     */
    public int modifyGroupIdentity(UserInGroup userInGroup, String userName, String groupName) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(userInGroup, "userName="+userName,"groupName="+groupName);
        return 1;
    }

    /**
     * 修改群公告
     * @param userInGroup
     * @param userName
     * @param groupName
     * @return
     */
    public int modifyGroupAnnounce(UserInGroup userInGroup, String userName, String groupName) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(userInGroup, "groupName="+groupName);
        return 1;
    }
}
