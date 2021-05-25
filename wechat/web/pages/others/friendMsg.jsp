<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/15
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/friendMsg.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2525342_0dtdxoru5sxu.css"/>
    <title>好友信息</title>
</head>
<body>
    <div class="content">
        <div class="tit">
            <h1 style="color:white">好友信息</h1>
        </div>
        <img id="showimg" name="showimg" class="head_portrait" src="${sessionScope.findFriendUser.headProtrait}"><%--群头像--%>
        <i class="iconfont iconid" id="iconid" name="weixin"></i> <!-- 将来用来绘制微信前面的图标 -->
        <label id="id" name="id" class="labels">id: ${sessionScope.findFriendUser.id}</label>
        <br/><br/>
        <i class="iconfont icon-xingming" id="icon-xingming" name="icon-xingming"></i> <!-- 将来用来绘制微信前面的图标 -->
        <label id="name" name="name" class="labels">名字： ${sessionScope.findFriendUser.name}</label>
        <br/><br/>
        <i class="iconfont iconzhanghao" id="iconzhanghao" name="iconzhanghao"></i> <!-- 将来用来绘制微信前面的图标 -->
        <label id="account" name="account" class="labels">账号： ${sessionScope.findFriendUser.account}</label>
        <br/><br/>
        <i class="iconfont iconyouxiang" id="iconyouxiang" name="iconyouxiang"></i> <!-- 将来用来绘制微信前面的图标 -->
        <label type="text" name="email" id="email" class="labels">邮箱: ${sessionScope.findFriendUser.email}</label>
        <br/><br/>
        <i class="iconfont iconshenfen" id="iconshenfen" name="iconshenfen"></i> <!-- 将来用来绘制微信前面的图标 -->
        <label name="protiait" id="protrait" class="labels">身份： ${sessionScope.findFriendUser.userKind}</label>
        <br/><br/>
        <i class="iconfont iconic_pengyouquan_x" id="iconic_pengyouquan_x" name="iconic_pengyouquan_x"></i> <!-- 将来用来绘制微信前面的图标 -->
        <label name="pyqqx" id="pyqqx" class="labels">朋友圈权限： ${sessionScope.UserFriend.ciclePermission}</label>
        <br/><br/>
        <i class="iconfont iconquanxian" id="iconquanxian" name="iconquanxian"></i> <!-- 将来用来绘制微信前面的图标 -->
        <label name="quanxian" id="quanxian" class="labels">是否拉黑： ${sessionScope.UserFriend.isBlacklist}</label>
        <br/><br/>
        <i class="iconfont iconbeizhu1" id="iconbeizhu1" name="iconbeizhu1"></i> <!-- 将来用来绘制微信前面的图标 -->
        <label name="beizhu" id="beizhu" class="labels">备注： ${sessionScope.UserFriend.friendNote}</label>
    </div>
</body>
</html>
