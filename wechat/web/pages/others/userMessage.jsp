<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/8
  Time: 16:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>用户页面</title>
    <link type="text/css" rel="stylesheet" href="static/css/userMessage.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2525342_rb5azsdts5r.css"/>
    <script>
        $(function (){

        });
    </script>
</head>
<body>
<div class="content">
    <div class="login_box">
        <div class="tit">
            <h1 style="color:white">用户信息</h1>
        </div>
        <img id="showimg" name="showimg" class="head_portrait" src="${sessionScope.findUserByAccountInFriendTipJsp.headProtrait}">
        <form action="userMSGServlet?method=modifyMSG" method="post">
            <br/>
            <i class="iconfont iconid" id="iconid" name="weixin"></i> <!-- 将来用来绘制微信前面的图标 -->
            <label id="id" name="id" class="labels_1">${sessionScope.findUserByAccountInFriendTipJsp.id}
            </label>
            <br/><br/>
            <i class="iconfont icon-xingming" id="icon-xingming" name="icon-xingming"></i> <!-- 将来用来绘制微信前面的图标 -->
            <input type="text" name="name" id="name" disabled="disabled" class="itxt" value=${sessionScope.findUserByAccountInFriendTipJsp.name}>
            <br/><br/>
            <i class="iconfont iconzhanghao" id="iconzhanghao" name="iconzhanghao"></i> <!-- 将来用来绘制微信前面的图标 -->
            <label id="account" name="account" class="labels">${sessionScope.findUserByAccountInFriendTipJsp.account}
            </label>
            <br/><br/>
            <i class="iconfont iconyouxiang" id="iconyouxiang" name="iconyouxiang"></i> <!-- 将来用来绘制微信前面的图标 -->
            <input type="text" name="email" id="email" class="itxt" value="${sessionScope.findUserByAccountInFriendTipJsp.email}" disabled="disabled">
            <br/><br/>
            <i class="iconfont iconshenfen" id="iconshenfen" name="iconshenfen"></i> <!-- 将来用来绘制微信前面的图标 -->
            <label name="protiait" id="protrait" class="labels">${sessionScope.findUserByAccountInFriendTipJsp.userKind}
            </label>
        </form>
    </div>
</div>
</body>
</html>
