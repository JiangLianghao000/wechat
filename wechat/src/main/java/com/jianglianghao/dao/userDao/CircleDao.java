package com.jianglianghao.dao.userDao;

import com.jianglianghao.dao.UserDao;
import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1220:13
 */

public class CircleDao {

    /**
     * 找到用户的发布的所有朋友圈
     *
     * @param circle 实体类
     * @return list集合
     */
    public List<Circle> findUserCircle(Circle circle) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<Circle> seek = instance.seek(circle);
        return seek;
    }

    /**
     * 找到用户所有好友的朋友圈
     *
     * @return list集合
     * @throws Exception
     */
    public List<Circle> findAllFriendCircle(HttpServletRequest request) throws Exception {
        User user = (User)request.getSession().getAttribute("loginUser");
        //子查询
        String sql = "select content_id contentId, user_name userName, user_account userAccount, content, content_permission contentPermission, time, location  from circle \n" +
                " where user_name = ANY(\n" +
                "\t\t select name \n" +
                "\t\t\tfrom `user`\n" +
                "\t\t\twhere id = any(\n" +
                "\t\t\tselect friend_id friendId from user_friend\n" +
                "\t\t\twhere user_id= ? and state = 'friend' and cicle_permission='yes' \n" +
                "\t\t)\n" +
                " )";
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<Circle> friend = instance.getAllInstances(Circle.class, sql, user.getId());
        return friend;
    }


    /**
     * 发布朋友圈
     *
     * @param circle 实体类
     * @throws Exception 异常
     */
    public int saveInCicle(Circle circle) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(circle);
        return 1;
    }

    /**
     * 获取list
     *
     * @param circle 实体类
     * @return list集合
     */
    public List<Circle> getCircle(Circle circle) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<Circle> seek = instance.seek(circle);
        return seek;
    }

    /**
     * 获取指定id朋友圈的评论信息
     *
     * @param circleComment 实体类
     * @return list集合
     * @throws Exception 异常
     */
    public List<CircleComment> getCircleComment(CircleComment circleComment) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<CircleComment> seek = instance.seek(circleComment);
        return seek;
    }

    /**
     * 获取朋友圈的点赞数
     *
     * @param circleLike 实体类，存有content的id
     * @return
     * @throws Exception
     */
    public int getLikePeople(CircleLike circleLike) throws Exception {
        //证明是最后一页，调用方法找到最后一层
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<CircleLike> allInstance = instance.seek(circleLike);
        return allInstance.size();
    }

    /**
     * 获取评论中的评论
     *
     * @param circleCommentInComment 实体类
     * @return 集合
     * @throws Exception 异常
     */
    public List<CircleCommentInComment> getCircleCommentInComment(CircleCommentInComment circleCommentInComment) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        String sql = "select comment_id commentId, content_id contentId, user_name userName, comment_for_comment commentForComment, comment_for_comment_name commentForCommentName, public_name publicName, time time" +
                " from circle_comment_in_comment where comment_id = ? and content_id = ? limit 0, 10";
        List<CircleCommentInComment> seek = instance.getAllInstances(CircleCommentInComment.class, sql, circleCommentInComment.getCommentId(), circleCommentInComment.getContentId());
        return seek;
    }

    /**
     * 调用方法获取截取的10条数据
     *
     * @param count
     * @return
     * @throws Exception
     */
    public List<CircleComment> getPages(int count, HttpServletRequest request) throws Exception {
        List<com.jianglianghao.entity.CircleComment> findCircleComment = (List<CircleComment>) request.getSession().getAttribute("findFriendCircleMsgComments");
        //检测是否是最后一条
        int page = findCircleComment.size() % 10 == 0 ? findCircleComment.size() / 10 : findCircleComment.size() / 10 + 1;
        if (page == count){
            //证明是最后一页，调用方法找到最后一层
            String sql = "select circle_id circleId, comment_id commentId, comment_person_name commentPersonName, comment_content commentContent, time from circle_comment" +
                    " where circle_id = ? limit ?, ?";
            UserDaoImpl instance = UserDaoImpl.getInstance();
            List<CircleComment> allInstance = instance.getAllInstances( CircleComment.class, sql, findCircleComment.get(0).getCircleId()
                    , (count - 1) * 10, findCircleComment.size() - (count - 1) * 10);
            return allInstance;
        }else{
            //证明不是最后一页
            String sql = "select circle_id circleId, comment_id commentId, comment_person_name commentPersonName, comment_content commentContent, time from circle_comment" +
                    " where circle_id = ? limit ?, 10";
            UserDaoImpl instance = UserDaoImpl.getInstance();
            List<CircleComment> allInstance = instance.getAllInstances( CircleComment.class, sql, findCircleComment.get(0).getCircleId()
                    , (count - 1) * 10);
            return allInstance;
        }
    }

    /**
     * 点赞朋友圈
     * @param circleLike 朋友圈点赞类
     * @return 结果 1：表示成功， 0表示未成功
     * @throws Exception 异常
     */
    public int like(CircleLike circleLike) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        //判断是否已经点赞过了
        List<CircleLike> seek = instance.seek(circleLike);
        if(seek.size() != 0){
            //证明已经点赞过了
            return 0;
        }
        //没有点赞
        instance.add(circleLike);
        return 1;
    }

    /**
     * 取消点赞朋友圈
     * @param circleLike 朋友圈点赞类
     * @return 结果 1：表示成功， 0表示未成功
     * @throws Exception 异常
     */
    public int unlike(CircleLike circleLike) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<CircleLike> seek = instance.seek(circleLike);
        if(seek.size() == 0){
            //证明自己没有点赞过
            return 0;
        }
        //证明自己有点赞记录
        instance.delete(circleLike);
        return 1;
    }

    /**
     * 添加评论
     * @param circleComment 评论类
     * @throws Exception
     */
    public int addComment(CircleComment circleComment)throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(circleComment);
        return 1;
    }

    /**
     * 删除动态
     * @param circle
     * @throws Exception
     */
    public void deleteComment(Circle circle) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.delete(circle);
    }

    /**
     * 添加评论中的评论
     * @param circleCommentInComment 实体类
     * @return 结果
     * @throws Exception 异常
     */
    public int addCommentInComment(CircleCommentInComment circleCommentInComment) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(circleCommentInComment);
        return 1;
    }

}
