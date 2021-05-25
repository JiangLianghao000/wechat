package com.jianglianghao.service;

import com.jianglianghao.dao.userDao.UserAndFriendDao;
import com.jianglianghao.dao.userDao.UserAndGroupDao;
import com.jianglianghao.entity.Groups;
import com.jianglianghao.entity.User;
import com.jianglianghao.entity.UserInGroup;
import com.jianglianghao.util.CommonUtil;
import com.jianglianghao.util.SensitiveWordUtil;
import com.jianglianghao.util.storageUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;


/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/108:54
 */

public class UserAndGroupService {

    /**
     * 查找用户所在的所有群
     *
     * @param groups 实体类
     * @return list集合
     * @throws Exception 异常
     */
    public List<Groups> findAllGroups(Groups groups) throws Exception {
        return new UserAndGroupDao().findAllGroups(groups);
    }

    /**
     * 创建群聊
     *
     * @param groups entity
     * @throws Exception 异常
     */
    public void createGroup(Groups groups) throws Exception {
        new UserAndGroupDao().createGroup(groups);
    }

    /**
     * 看看该用户是否在群中
     *
     * @param userInGroup
     * @return
     * @throws Exception
     */
    public List<UserInGroup> CheckIsInGroup(UserInGroup userInGroup) throws Exception {
        return new UserAndGroupDao().CheckIsInGroup(userInGroup);
    }

    /**
     * 用户添加群聊
     *
     * @param userInGroup 实体类
     * @throws Exception
     */
    public int addGroup(UserInGroup userInGroup) throws Exception {
        return new UserAndGroupDao().addGroup(userInGroup);
    }

    /**
     * 在tip界面查找该用户是管理员或者群主的群聊
     *
     * @return list
     */
    public List<UserInGroup> findPeopleAddGroup(HttpServletRequest request) {
        return new UserAndFriendDao().findPeopleAddGroup(request);
    }

    /**
     * 同意进群
     *
     * @param userInGroup
     * @return
     */
    public int userAgreeToInGroup(UserInGroup userInGroup, HttpServletRequest request) throws Exception {
        return new UserAndGroupDao().userAgreeToInGroup(userInGroup, request);
    }

    /**
     * 查找用户所在的群
     * @param userInGroup 实体类
     * @return list集合
     * @throws Exception 异常
     */
    public List<UserInGroup> findUserInGroups(UserInGroup userInGroup) throws Exception{
        return new UserAndGroupDao().findUserInGroups(userInGroup);
    }

    /**
     * 退出群聊
     * @param userInGroup 实体类
     */
    public int leftGroup(UserInGroup userInGroup, Groups groups) throws Exception{
        return new UserAndGroupDao().leftGroup(userInGroup, groups);
    }


    /**
     * 调用数据库查询是否已存在，群名是否相同，是否包含敏感内容
     * @param groups
     * @param userInGroup
     */
    public int mofidyGroupMsg(Groups groups, UserInGroup userInGroup, HttpServletRequest request) throws Exception {
        Groups groups2 = (Groups)request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
        //获取所有敏感词集合
        Set<String> badWords = storageUtils.getSensitiveWordsSet();
        //初始化
        SensitiveWordUtil.init(badWords);
        //检测
        String content = groups.getGroupAnnounce();
        if(CommonUtil.judgeMSG(content) == false){
            //如果不为空就检测
            //去除标签
            String s = CommonUtil.delHTMLTag(content);
            //检测
            boolean result = SensitiveWordUtil.contains(s);
            if(result == true){
                //证明包含敏感词汇
                return 1;
            }
        }
        //检测nickname和name和userNickName的合法
        String nameRegex = "^.{3,20}$";
        if(!groups.getGroupName().matches(nameRegex)){
            //用户输入了群名，而且群名规则不匹配
            return 2;
        }
        if(CommonUtil.judgeMSG(userInGroup.getGroupNickname()) == false){
            //证明用户有意向输入群昵称
            if(!userInGroup.getGroupNickname().matches(nameRegex)){
                //输入的昵称不合法
                return 3;
            }
        }
        if(CommonUtil.judgeMSG(userInGroup.getUserNickname()) == false){
            //证明用户有意向输入在群的昵称
            if(!userInGroup.getUserNickname().matches(nameRegex)){
                //输入的用户在群的昵称不合法
                return 4;
            }
        }

        //都没没有问题就检测name是否存在
        if(!groups.getGroupName().equals(groups2.getGroupName())){
            Groups groups1 = new Groups("groupName="+groups.getGroupName());
            List<Groups> allGroups = new UserAndGroupDao().findAllGroups(groups1);
            if(allGroups.size() == 1){
                //证明群名已经存在
                return 5;
            }
        }
        //都没有问题了
        return new UserAndGroupDao().mofidyGroupMsg(groups, userInGroup, request);
    }

    /**
     * 修改群昵称和在群的昵称
     * @param userInGroup
     * @return
     */
    public int mofidyGroupMsg1(UserInGroup userInGroup, HttpServletRequest request) throws Exception {
        User user = (User)request.getSession().getAttribute("loginUser");
        String nameRegex = "^.{3,20}$";
        String userNickName = userInGroup.getUserNickname();
        String groupNickName = userInGroup.getGroupNickname();
        if(CommonUtil.judgeMSG(userInGroup.getGroupNickname()) == false){
            //证明用户有意向输入群昵称
            if(!userInGroup.getGroupNickname().matches(nameRegex)){
                //输入的昵称不合法
                return 7;
            }
        }
        if(CommonUtil.judgeMSG(userInGroup.getUserNickname()) == false){
            //证明用户有意向输入在群的昵称
            if(!userInGroup.getUserNickname().matches(nameRegex)){
                //输入的用户在群的昵称不合法
                return 8;
            }
        }

        return new UserAndGroupDao().mofidyGroupMsg1(userInGroup, request);
    }

    /**
     * 修改群头像
     * @param groups
     * @throws Exception
     */
    public void modifyGroupHead(Groups groups, HttpServletRequest request) throws Exception{
        new UserAndGroupDao().modifyGroupHead(groups, request);
    }
}
