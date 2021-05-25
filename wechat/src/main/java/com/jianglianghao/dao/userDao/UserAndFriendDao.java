package com.jianglianghao.dao.userDao;

import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/622:01
 */

public class UserAndFriendDao {

    /**
     * 传入entity实体类查找list集合
     * @param userFriend 实体类
     * @return 好友list集合
     * @throws Exception
     */
    public List<UserFriend> getUserFriend(UserFriend userFriend) throws Exception {
        //查找
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<UserFriend> seek = instance.seek(userFriend);
        return seek;
    }

    /**
     * 添加好友
     * @param userFriend entity类
     * @throws Exception
     */
    public void addFriend(UserFriend userFriend) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(userFriend);
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
        String sql = " select user_id userId,group_name groupName, user_account userAccount\n" +
                " from user_in_group \n" +
                " where state='wait_for_add' \n" +
                " and group_identity = '群员'\n" +
                " and group_account in (\n" +
                "\t\tselect `group_account` \n" +
                "\t\tfrom `user_in_group` \n" +
                "\t\twhere (user_account = ? AND group_identity = '群主')\n" +
                "\t\tor \t\t\n" +
                "\t\t(user_account = ? AND group_identity = '管理员')\n" +
                "\n" +
                " );";
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<UserInGroup> allInstance = instance.getAllInstances(UserInGroup.class, sql, user.getAccount(), user.getAccount());
        return allInstance;

    }

    /**
     * 同意添加好友
     * @param userFriend
     * @return
     * @throws Exception
     */
    public int userAgreeAddForFriend(UserFriend userFriend, HttpServletRequest request) throws Exception{
        User User = (User) request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(userFriend, "userId=" + User.getId(), "friendId="+user.getId());
        return 1;
    }

    /**
     * 拉黑好友
     * @param userFriend 实体类
     * @throws Exception 异常
     */
    public int addToBlackList(UserFriend userFriend, HttpServletRequest request) throws Exception{
        User user1 = (User)request.getSession().getAttribute("findFriendUser");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(userFriend, "friendId="+user.getId(), "userId="+user1.getId());
        return 1;
    }

    /**
     * 取消拉黑好友
     * @throws Exception 异常
     */
    public int NotAllToBlackList(UserFriend userFriend, HttpServletRequest request) throws Exception{
        User user1 = (User)request.getSession().getAttribute("findFriendUser");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(userFriend, "friendId="+user.getId(), "userId="+user1.getId());
        return 1;
    }

    /**
     * 修改备注
     * @param userFriend 实体类
     * @return 修改结果
     * @throws Exception 异常
     */
    public int modifyNote(UserFriend userFriend, HttpServletRequest request) throws Exception{
        User user1 = (User)request.getSession().getAttribute("findFriendUser");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.change(userFriend, "friendId="+user.getId(), "userId="+user1.getId());
        return 1;
    }

    /**
     * 删除好友
     * @param userFriend 实体类
     * @throws Exception 异常
     */
    public int deleteFriend(UserFriend userFriend) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.delete(userFriend);
        return 1;
    }

    /**
     * 游客拉黑好友
     * @param userFriend 好友类
     * @param request 请求
     * @throws Exception 异常
     */
    public int travelAddToBlacklist(UserFriend userFriend, HttpServletRequest request, String friendName) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        User user = (User)request.getSession().getAttribute("loginUser");
        instance.change(userFriend,"friendId="+user.getId(), "friendName="+friendName);
        return 1;
    }

    /**
     * 游客取消拉黑好友
     * @param userFriend 好友类
     * @param request 请求
     * @throws Exception 异常
     */
    public int travelNotAddToBlacklist(UserFriend userFriend, HttpServletRequest request, String friendName) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        User user = (User)request.getSession().getAttribute("loginUser");
        instance.change(userFriend,"friendId="+user.getId(), "friendName="+friendName);
        return 1;
    }

    /**
     * 发送好友卡片
     * @param card 好友卡片
     * @throws Exception 异常
     */
    public int sendCard(Card card) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(card);
        return 1;
    }

    /**
     * 查看好友卡片
     * @param card 好友卡片
     * @throws Exception 异常
     */
    public List<Card> findAllCards(Card card) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<Card> seek = instance.seek(card);
        return seek;
    }

    /**
     * 使用完之后删除卡片
     * @param card 卡片
     * @throws Exception
     */
    public void deleteACard(Card card) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.delete(card);
        return;
    }

}
