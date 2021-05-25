<%@ page import="static com.jianglianghao.util.storageUtils.*" %><%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/13
  Time: 2:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/cicleMsgView.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2543693_j01vnvqdb2.css"/>
    <title>用户动态信息</title>
    <script>
        var area = document.getElementsByTagName("textarea");
        for (var i = 0; i < area.length; i++) {
            area[i].value = "";
        }
        var content;
        /*当前的页数，一进来是第一页*/
        var nowPage = ${sessionScope.nowPage};

        function show(date) {
            var id = document.getElementById(date.id).innerHTML;
            $.post("circleServlet?method=getPages&choicePage=" + id, function (date1) {
                if (date1 == "1") {
                    window.location.href = ("pages/mainView/cicleMsgView.jsp")
                } else {
                    alert("程序异常")
                }
            })
        }

        $(function () {
            /*点击下一页*/
            $("#nextpage").click(function () {
                $.post("circleServlet?method=getPages&choicePage=${sessionScope.nowPage + 1}", function (date1) {
                    if (date1 == "1") {
                        window.location.href = ("pages/mainView/cicleMsgView.jsp")
                    } else {
                        alert("程序异常")
                    }
                });
            });

            /*点击上一页*/
            $("#lastpage").click(function () {
                $.post("circleServlet?method=getPages&choicePage=" + ${sessionScope.nowPage - 1}, function (date1) {
                    if (date1 == "1") {
                        window.location.href = ("pages/mainView/cicleMsgView.jsp")
                    } else {
                        alert("程序异常")
                    }
                });
            });

            /*点赞*/
            $("#like").click(function () {
                $.post("circleServlet?method=like", function (date) {
                    if (date == "1") {
                        //证明没有点赞，已经点赞了
                        window.location.href = ("pages/mainView/cicleMsgView.jsp")
                    }
                    if (date == "0") {
                        //证明已经点过赞了
                        alert("已点过赞，不可重复点")
                    }
                })
            })

            /*取消点赞*/
            $("#dislike").click(function () {
                $.post("circleServlet?method=dislike", function (date) {
                    if (date == "1") {
                        window.location.href = ("pages/mainView/cicleMsgView.jsp")
                    }
                    if (date == "0") {
                        alert("还未点赞，不能取消");
                    }
                })
            })

            /*删除动态*/
            $("#deleteComment").click(function () {
                $.post("circleServlet?method=deleteComment", function (date) {
                    if (date == "0") {
                        alert("不能删除好友的动态");
                    }
                    if (date == "1") {
                        alert("已删除动态")
                        window.close();
                    }
                });
            });

            $("#addComment").click(function () {
                var text = $("#say").val();
                $.post("circleServlet?method=addComment&content=" + text, function (date) {
                    if (date == "1") {
                        alert("已评论");
                        window.location.href = ("pages/mainView/cicleMsgView.jsp")
                    }
                });
            });

        });

        function shows(date1) {
            var commentId = date1.value;
            var Id = commentId.substr(3);
            $.post("circleServlet?method=addCommentInComment&commentId="+ Id +"&content=" + content, function (date) {
                if(date == "1"){
                    alert("评论成功");
                    window.location.href = ("pages/mainView/cicleMsgView.jsp")
                }
                if(date == "0"){
                    alert("输入的评论为空")
                }
            });

        }
        function showContent(date) {
            content = date.value;
        }
    </script>
</head>
<body>
<div class="tishi" style="overflow: auto">
    <span class="friendMsg">发布者：${sessionScope.findFriendCircleMsg.userName}</span>&nbsp;&nbsp;&nbsp;&nbsp;
    <span class="friendMsg">发布时间：${sessionScope.findFriendCircleMsg.time}</span>&nbsp;&nbsp;&nbsp;&nbsp;
    <span class="friendMsg">发布地点：广东工业大学</span>&nbsp;&nbsp;&nbsp;&nbsp;
</div>
<%--朋友圈内容--%>
<div class="editDivs" contenteditable="false" id="editDiv"
     name="editDiv">${sessionScope.findFriendCircleMsg.content}</div>
<%--评论区--%>
<div class="tishi">
    &nbsp;&nbsp;<span class="friendMsg1" id="like_number">点赞数：${sessionScope.findLikePeople}</span>&nbsp;&nbsp;&nbsp;&nbsp;
    <span class="friendMsg1">评论数：${sessionScope.allCommentNumber}</span>&nbsp;&nbsp;&nbsp;&nbsp;
    <span class="friendMsg1">相关回复数：${sessionScope.ConnectRecallNumber}</span>&nbsp;&nbsp;&nbsp;&nbsp;
    <i class="iconfont icon-dianzan" id="like" name="like"></i>&nbsp;&nbsp;&nbsp;
    <i class="iconfont icon-quxiaodianzan" id="dislike" name="dislike"></i>
</div>
<div class="allComment">
    <input type="text" class="say" id="say" name="say" placeholder="说说此刻内心的想法吧"/>
    <button id="addComment" naame="addComment" class="addComment">发布评论</button>
    <button id="deleteComment" naame="deleteComment" class="addComment">删除动态</button>
    <ul class="commentArea">
        <%--当评论数不大于10条的时候--%>
        <c:if test="${sessionScope.findFriendCircleMsgComments.size() <= 10 && sessionScope.findFriendCircleMsgComments.size() != 0}">
        <c:forEach begin="0" end="${sessionScope.findFriendCircleMsgComments.size()-1}" var="i">
        <div class="div_3">
            <div class="div_5">
                <span class="friendMsg">${sessionScope.findFriendCircleMsgComments.get(i).commentPersonName}</span><br>
                <span class="friendMsg">评论于${sessionScope.findFriendCircleMsgComments.get(i).time}</span>&nbsp;&nbsp;&nbsp;&nbsp;
            </div>
            <div class="textArea_1"
                 style="color: black; overflow: auto">${sessionScope.findFriendCircleMsgComments.get(i).commentContent}</div>
            <br>
            <input type="button" class="button_2" value="评论-${sessionScope.findFriendCircleMsgComments.get(i).commentId}" id="button_21" name="${i}" onclick="shows(this)">
            <textarea class="textArea_2" id="${(sessionScope.nowPage - 1)*10+(i + 1)}" name="textComment"
                      onchange="showContent(this)"></textarea><br><br>
            <span style="color: #00dbde">相关评论:</span>&nbsp;&nbsp;&nbsp;&nbsp;
            <ul class="ul_1">
                    <%--放评论中的评论--%>
                <table align="center" border="0" width="290" height="20" cellspacing="0" class="table_2"
                       id="msgs2">
                    <c:if test="${sessionScope.findOneCircleComment.get(i).size() >= 1}">
                        <c:forEach begin="0" end="${sessionScope.findOneCircleComment.get(i).size() - 1}"
                                   var="j">
                            <tr class="tr_1">
                                <td>
                                        ${sessionScope.findOneCircleComment.get(i).get(j).userName}:${sessionScope.findOneCircleComment.get(i).get(j).commentForComment}
                                    <span style="font-size: small">${sessionScope.findOneCircleComment.get(i).get(j).time}</span>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
            </ul>
        </div>
        <div class="div_4"></div>
        </c:forEach>
        </c:if>

        <%--当评论数大于10条的时候，这时候可以分页查询--%>
        <c:if test="${sessionScope.findFriendCircleMsgComments.size() > 10}">
        <c:forEach begin="0" end="9" var="i">
        <div class="div_3">
            <div class="div_5">
                <span class="friendMsg">${sessionScope.findFriendCircleMsgComments.get(i).commentPersonName}</span><br>
                <span class="friendMsg">评论于${sessionScope.findFriendCircleMsgComments.get(i).time}</span>&nbsp;&nbsp;&nbsp;&nbsp;
            </div>
            <div class="textArea_1"
                 style="color: black; overflow: auto">${sessionScope.findFriendCircleMsgComments.get(i).commentContent}</div>
            <br>
                <%--按键要和页数结合起来--%>
            <input type="button" class="button_2" value="评论-${sessionScope.findFriendCircleMsgComments.get(i).commentId}" id="button_2"
                   name="${i}" onclick="shows(this)">
            <textarea class="textArea_2" id="${(sessionScope.nowPage - 1)*10+(i + 1)}" name="textComment"
                      onchange="showContent(this)"></textarea><br><br>
            <span style="color: #00dbde">相关评论:</span>&nbsp;&nbsp;&nbsp;&nbsp;
            <ul class="ul_1">
                    <%--放评论中的评论--%>
                <table align="center" border="0" width="290" height="20" cellspacing="0" class="table_2" id="msg2">
                    <c:if test="${sessionScope.findOneCircleComment.get(i).size() >= 1}">
                        <%--获取评论中的评论， i是第i个评论，获取第i个--%>
                        <c:forEach begin="0" end="${sessionScope.findOneCircleComment.get(i).size() - 1}" var="j">
                            <tr class="tr_1">
                                <td>
                                        ${sessionScope.findOneCircleComment.get(i).get(j).userName}:${sessionScope.findOneCircleComment.get(i).get(j).commentForComment}<br>
                                    <span style="font-size: xx-small">评论于${sessionScope.findOneCircleComment.get(i).get(j).time}</span>
                                    <br>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
            </ul>
        </div>
        <div class="div_4"></div>
        </c:forEach>
        </c:if>
</div>
<c:choose>
    <%--判断不是第一页就显示灰色的--%>
    <c:when test="${sessionScope.nowPage == 1}">
        <input type="button" name="lastpage" value="上一页" id="lastpage" disabled="disabled" class="btn">
    </c:when>
    <c:otherwise>
        <input type="button" name="lastpage" value="上一页" id="lastpage" class="btn"/>
    </c:otherwise>
</c:choose>
<%--页数列表--%>
<%--判断页数，分为只有一页和有多页的情况--%>
<c:if test="${sessionScope.pagesAccount >= 1}">
    <c:forEach begin="0" end="${sessionScope.pagesAccount-1}" var="items">
        <%--达到了第10页--%>
        <a id="pagesId${items}" onclick="show(this)" style="color: blue" class="a_1">${items + 1}</a>
    </c:forEach>
</c:if>
<%--按键：下一页--%>
<c:choose>
    <%--判断不是第一页就显示灰色的--%>
    <c:when test="${sessionScope.nowPage == sessionScope.allPages || sessionScope.allPages == 0}">
        <input type="button" name="nextpage" value="下一页" disabled="disabled">
    </c:when>
    <c:otherwise>
        <a><input type="button" name="nextpage" value="下一页" id="nextpage"/></a>
    </c:otherwise>
</c:choose>
<c:if test="${sessionScope.pagesAccount != 0}">
    <span>共${sessionScope.pagesAccount}页,当前是第 ${sessionScope.nowPage}页, 点击页数可跳转到指定页</span>
</c:if>
<c:if test="${sessionScope.pagesAccount == 0}">
    <span>共1页,当前是第1页, 点击页数可跳转到指定页</span>
</c:if>
<%--按键：上一页--%>
</body>
</html>