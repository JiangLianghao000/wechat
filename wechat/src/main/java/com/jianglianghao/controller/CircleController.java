package com.jianglianghao.controller;

import com.jianglianghao.dao.userDao.CircleDao;
import com.jianglianghao.entity.Circle;
import com.jianglianghao.entity.CircleComment;
import com.jianglianghao.entity.CircleCommentInComment;
import com.jianglianghao.entity.CircleLike;
import com.jianglianghao.service.CircleService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1220:13
 */

public class CircleController {

    /**
     * 找到用户的发布的所有朋友圈
     * @param circle
     * @return
     */
    public List<Circle> findUserCircle(Circle circle) throws Exception{
        return new CircleService().findUserCircle(circle);
    }

    /**
     * 找到用户所有好友的朋友圈
     * @return
     * @throws Exception
     * @param request
     */
    public List<Circle> findALlUserCircle(HttpServletRequest request) throws Exception{
        return new CircleService().findAllFriendCircle(request);
    }

    /**
     * 发布朋友圈
     * @param circle 实体类
     * @throws Exception 异常
     */
    public int saveInCicle(Circle circle) throws Exception{
        return new CircleService().saveInCicle(circle);
    }

    /**
     * 获取list
     * @param circle 实体类
     * @return
     */
    public List<Circle> getCircle(Circle circle) throws Exception {
        return new CircleService().getCircle(circle);
    }

    /**
     * 获取指定id朋友圈的评论信息
     * @param circleComment 实体类
     * @return list集合
     * @throws Exception 异常
     */
    public List<CircleComment> getCircleComment(CircleComment circleComment) throws Exception{
        return new CircleService().getCircleComment(circleComment);
    }


    /**
     * 获取朋友圈的点赞数
     * @param circleLike 实体类，存有content的id
     * @return
     * @throws Exception
     */
    public int getLikePeople (CircleLike circleLike) throws Exception{
        return new CircleService().getLikePeople(circleLike);
    }

    /**
     * 获取评论中的评论
     * @param circleCommentInComment 实体类
     * @return 集合
     * @throws Exception 异常
     */
    public List<CircleCommentInComment> getCircleCommentInComment(CircleCommentInComment circleCommentInComment) throws Exception{
        return new CircleService().getCircleCommentInComment(circleCommentInComment);
    }

    /**
     * 根据页数查找评论
     * @param count
     * @return
     * @throws Exception
     */
    public List<CircleComment> getPages(int count,  HttpServletRequest request) throws Exception{
        return new CircleService().getPages(count, request);
    }

    /**
     * 点赞朋友圈
     * @param circleLike 朋友圈点赞类
     * @return 结果 1：表示成功， 0表示未成功
     * @throws Exception 异常
     */
    public int like(CircleLike circleLike) throws Exception{
        return new CircleService().like(circleLike);
    }

    /**
     * 取消点赞朋友圈
     * @param circleLike 朋友圈点赞类
     * @return 结果 1：表示成功， 0表示未成功
     * @throws Exception 异常
     */
    public int unlike(CircleLike circleLike) throws Exception{
        return new CircleService().unlike(circleLike);
    }

    /**
     * 添加评论
     * @param circleComment 评论类
     * @throws Exception
     */
    public int addComment(CircleComment circleComment)throws Exception{
        return new CircleService().addComment(circleComment);
    }

    /**
     * 删除动态
     * @param circle 动态
     * @throws Exception 异常
     */
    public void deleteComment(Circle circle) throws Exception{
        new CircleService().deleteComment(circle);
    }

    /**
     * 添加评论中的评论
     * @param circleCommentInComment
     */
    public int addCommentInComment(CircleCommentInComment circleCommentInComment) throws Exception{
        return new CircleService().addCommentInComment(circleCommentInComment);
    }
}
