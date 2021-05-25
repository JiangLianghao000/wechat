package com.jianglianghao.view;

import com.jianglianghao.controller.CircleController;
import com.jianglianghao.entity.*;
import com.jianglianghao.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.jianglianghao.util.storageUtils.*;
import static com.jianglianghao.util.storageUtils.findOneCircleComment;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1220:13
 */

public class CircleServlet extends BaseServlet {

    /**
     * 查找自己朋友圈
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void findUserServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User)request.getSession().getAttribute("loginUser");
        Circle circle = new Circle("userName=" + user.getName(), "userAccount=" + user.getAccount());
        List<Circle> userCircle = new CircleController().findUserCircle(circle);
        request.getSession().setAttribute("circleByWhat", "myself");
        request.getSession().setAttribute("circles", userCircle);
        response.getWriter().write("1");
        return;
    }

    /**
     * 找到所有好友的朋友圈，被朋友圈拉黑的好友除外
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void findAllFriendCircle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Circle> aLlUserCircle = new CircleController().findALlUserCircle(request);
        if (aLlUserCircle.size() == 0) {
            response.getWriter().write("0");
            return;
        } else if (aLlUserCircle.size() != 0) {
            request.getSession().setAttribute("circleByWhat", "friend");
            request.getSession().setAttribute("circles", aLlUserCircle);
            response.getWriter().write("1");
            return;
        } else {
            response.getWriter().write("2");
            return;
        }
    }

    /**
     * 发布朋友圈
     *
     * @param request  请求
     * @param response 转发
     * @throws Exception 异常
     */
    public void saveInCicle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User)request.getSession().getAttribute("loginUser");
        //获取发布时间
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(calendar.getTime());
        String content = request.getParameter("content");
        Circle circle = new Circle(0, user.getName(), user.getAccount()
                , content, "yes", time, null);
        int i = new CircleController().saveInCicle(circle);
        if (i == 1) {
            response.getWriter().write("1");
            return;
        } else if (i == 2) {
            response.getWriter().write("2");
            return;
        } else {
            response.getWriter().write("0");
            return;
        }
    }

    /**
     * 点击id查询该条朋友圈记录
     *
     * @param request  请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void findFriendCircleMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //先初始化
        findOneCircleComment.clear();
        ConnectRecallNumber = 0;
        String id = request.getParameter("id");
        //获取朋友圈的id
        int circleId = Integer.parseInt(id);
        //获取朋友圈内容
        Circle circle = new Circle("contentId=" + circleId);

        List<Circle> circle1 = new CircleController().getCircle(circle);
        //获取评论
        CircleComment circleComment = new CircleComment("circleId=" + circleId);
        List<CircleComment> circleComment1 = new CircleController().getCircleComment(circleComment);
        request.getSession().setAttribute("findFriendCircleMsg", circle1.get(0));
        request.getSession().setAttribute("allCommentNumber", circleComment1.size());
        request.getSession().setAttribute("findFriendCircleMsgComments", circleComment1);
        //总共有多少页
        request.getSession().setAttribute("allPages", circleComment1.size()%10==0?circleComment1.size()/10:circleComment1.size()/10+1);
        //获取点赞数
        CircleLike circleLike = new CircleLike("contentId=" + circleComment.getCircleId());
        int likePeople= new CircleController().getLikePeople(circleLike);
        request.getSession().setAttribute("findLikePeople", likePeople);
        //获取评论中的评论
        //参数1：动态id， 参数2：动态下的回复的id
        for (CircleComment comment : circleComment1) {
            CircleCommentInComment circleCommentInComment = new CircleCommentInComment("contentId=" + comment.getCircleId(), "commentId=" + comment.getCommentId());
            List<CircleCommentInComment> circleCommentInComment1 = new CircleController().getCircleCommentInComment(circleCommentInComment);
            findOneCircleComment.add(circleCommentInComment1);
            if (circleCommentInComment1 != null) {
                ConnectRecallNumber += circleCommentInComment1.size();
            }
        }
        //获取分页
        //1. 获取总共的页数
        int allCommentSize = circleComment1.size() % 10 == 0 ? circleComment1.size() / 10 : circleComment1.size() / 10 + 1;
        request.getSession().setAttribute("pagesAccount", allCommentSize);
        //一打开，显示第一页
        request.getSession().setAttribute("nowPage", 1);
        request.getSession().setAttribute("findOneCircleComment", findOneCircleComment);
        request.getSession().setAttribute("ConnectRecallNumber", ConnectRecallNumber);
        return;
    }

    /**
     * 获取请求的页数，返回消息
     * @throws Exception
     */
    public void getPages(HttpServletRequest request, HttpServletResponse response) throws Exception {
        findOneCircleComment.clear();
        String choicePages = request.getParameter("choicePage");
        //调用数据库limit搜索相应的10条数据
        int choicePages1 = Integer.parseInt(choicePages);
        List<CircleComment> pages = new CircleController().getPages(choicePages1, request);
        request.getSession().setAttribute("findFriendCircleMsgComments", pages);
        //修改session值
        for (CircleComment comment : pages) {
            CircleCommentInComment circleCommentInComment = new CircleCommentInComment("contentId=" + comment.getCircleId(), "commentId=" + comment.getCommentId());
            List<CircleCommentInComment> circleCommentInComment1 = new CircleController().getCircleCommentInComment(circleCommentInComment);
            findOneCircleComment.add(circleCommentInComment1);
        }
        request.getSession().setAttribute("findOneCircleComment", findOneCircleComment);
        request.getSession().setAttribute("nowPage", choicePages);
        response.getWriter().write("1");
        return;
    }

    /**
     * 朋友圈点赞
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void like(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //获取点赞的人，也就是登陆的人
        String likePeople =(String)((User)( request.getSession().getAttribute("loginUser"))).getName();
        //获取被点赞的人
        Circle findFriendCircleMsg = (Circle)request.getSession().getAttribute("findFriendCircleMsg");
        String belikedPeople = findFriendCircleMsg.getUserName();
        //获取被点赞的id
        int contentId = findFriendCircleMsg.getContentId();
        CircleLike circleLike = new CircleLike(0, contentId, likePeople, belikedPeople);
        int like = new CircleController().like(circleLike);
        //获取content_id:findFriendCircleMsg指查看的人的circle，包含了其id
        //更新session的人数
        CircleLike circleLike1 = new CircleLike("contentId=" + findFriendCircleMsg.getContentId());
        int likePeople1 = new CircleController().getLikePeople(circleLike1);
        request.getSession().setAttribute("findLikePeople", likePeople1);
        if(like == 1){
            response.getWriter().write("1");
            return;
        }
        if(like == 0){
            response.getWriter().write("0");
            return;
        }
    }

    /**
     * 朋友圈取消点赞
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void dislike(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //获取点赞的人，也就是登陆的人
        String likePeople = (String)((User)( request.getSession().getAttribute("loginUser"))).getName();
        //获取被点赞的人
        Circle findFriendCircleMsg = (Circle)request.getSession().getAttribute("findFriendCircleMsg");
        String belikedPeople = findFriendCircleMsg.getUserName();
        //获取被点赞的id
        int contentId = findFriendCircleMsg.getContentId();
        CircleLike circleLike = new CircleLike(0, contentId, likePeople, belikedPeople);
        int unlike = new CircleController().unlike(circleLike);
        //更新session的人数
        CircleLike circleLike1 = new CircleLike("contentId=" + findFriendCircleMsg.getContentId());
        int likePeople1 = new CircleController().getLikePeople(circleLike1);
        request.getSession().setAttribute("findLikePeople", likePeople1);
        if(unlike == 1){
            response.getWriter().write("1");
            return;
        }
        if(unlike == 0){
            response.getWriter().write("0");
            return;
        }
    }

    /**
     * 删除动态
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void addComment(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("loginUser");
        Circle circle = (Circle) request.getSession().getAttribute("findFriendCircleMsg");
        //获取内容
        String content = request.getParameter("content");
        //获取时间
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(calendar.getTime());
        CircleComment circleComment = new CircleComment(circle.getContentId(), 0,user.getName(), content, time);
        int i = new CircleController().addComment(circleComment);
        //获取comment的id
        List<CircleComment> circleComment3 = new CircleController().getCircleComment(circleComment);
        request.getSession().setAttribute("commentId",circleComment3.get(0).getCommentId());
        if(i == 1){
            //重新赋值
            CircleComment circleComment1 = new CircleComment("circleId=" + circle.getContentId());
            List<CircleComment> circleComment2 = new CircleController().getCircleComment(circleComment1);
            request.getSession().setAttribute("allCommentNumber", circleComment2.size());
            request.getSession().setAttribute("findFriendCircleMsgComments", circleComment2);
            //获取评论中的评论
            //参数1：动态id， 参数2：动态下的回复的id
            for (CircleComment comment : circleComment2) {
                CircleCommentInComment circleCommentInComment = new CircleCommentInComment("contentId=" + comment.getCircleId(), "commentId=" + comment.getCommentId());
                List<CircleCommentInComment> circleCommentInComment1 = new CircleController().getCircleCommentInComment(circleCommentInComment);
                findOneCircleComment.add(circleCommentInComment1);
                if (circleCommentInComment1 != null) {
                    ConnectRecallNumber += circleCommentInComment1.size();
                }
            }
            request.getSession().setAttribute("findOneCircleComment", findOneCircleComment);
            request.getSession().setAttribute("ConnectRecallNumber", ConnectRecallNumber);
            response.getWriter().write("1");
            return;
        }
    }

    /**
     * 删除动态
     * @param request
     * @param response
     * @throws Exception
     */
    public void deleteComment(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("loginUser");
        //检查该动态是不是自己的，如果不是自己的不能删除
        Circle findFriendCircleMsg = (Circle)request.getSession().getAttribute("findFriendCircleMsg");
        //获取该动态是谁的
        String userName = findFriendCircleMsg.getUserName();
        //对比
        boolean judgeEqual = CommonUtil.judgeEqual(userName, user.getName());
        if(judgeEqual == false){
            //如果不相同，就不可以删除
            response.getWriter().write("0");
            return;
        }else{
            //证明相同，把评论中的评论和评论都删除，再重新赋值
            Circle circle = new Circle("contentId=" + findFriendCircleMsg.getContentId());
            new CircleController().deleteComment(circle);
            response.getWriter().write("1");
            return;
        }
    }

    /**
     * 发布评论中的评论
     * @param request 请求
     * @param response 响应
     * @throws Exception
     */
    public void addCommentInComment(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("loginUser");
        //获取时间
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(calendar.getTime());
        int index = 0;
        //获取点进去的人的circle
        int commentId = Integer.parseInt(request.getParameter("commentId"));
        //获取评论评论id
        Circle findFriendCircleMsg = (Circle)request.getSession().getAttribute("findFriendCircleMsg");
        //获取评论内容
        String content = request.getParameter("content");
        if(content == ""){
            response.getWriter().write("0");
            return;
        }

        //获取总评论数
        List<CircleComment> findFriendCircleMsgComments = (List<CircleComment>) request.getSession().getAttribute("findFriendCircleMsgComments");
        for (CircleComment findFriendCircleMsgComment : findFriendCircleMsgComments) {
            //获取相关评论在当前的位置，从而获取评论的人的名字
            index ++;
            if(findFriendCircleMsgComment.getCommentId() == commentId){
                break;
            }
        }
        CircleCommentInComment circleCommentInComment = new CircleCommentInComment(commentId, findFriendCircleMsg.getContentId(),
                findFriendCircleMsg.getUserName() , content, 0, user.getName(),findFriendCircleMsgComments.get(index-1).getCommentPersonName(),
                time);

        int i = new CircleController().addCommentInComment(circleCommentInComment);
        if(i == 1){
            //重新找，赋值
            findOneCircleComment.clear();
            ConnectRecallNumber=0;
            CircleComment circleComment = new CircleComment("circleId=" + findFriendCircleMsg.getContentId());
            List<CircleComment> circleComment1 = new CircleController().getCircleComment(circleComment);
            for (CircleComment comment : circleComment1) {
                CircleCommentInComment circleCommentInComment1 = new CircleCommentInComment("contentId=" + comment.getCircleId(), "commentId=" + comment.getCommentId());
                List<CircleCommentInComment> circleCommentInComment2 = new CircleController().getCircleCommentInComment(circleCommentInComment1);
                findOneCircleComment.add(circleCommentInComment2);
                if (circleCommentInComment2 != null) {
                    ConnectRecallNumber += circleCommentInComment2.size();
                }
            }
            request.getSession().setAttribute("findOneCircleComment", findOneCircleComment);
            request.getSession().setAttribute("ConnectRecallNumber", ConnectRecallNumber);
            response.getWriter().write("1");
        }
    }
}
