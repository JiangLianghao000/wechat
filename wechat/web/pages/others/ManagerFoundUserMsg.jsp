<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/15
  Time: 21:56
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
    <title>查找用户信息</title>
    <style>
        body{
            background: url("../../static/img/beijing1.jpg") center; /* 首先增加背景图 */
            background-repeat: no-repeat; /* 将背景设置为不重复显示 */
            background-size: cover;
            /* 放大背景图片，使其覆盖整个背景区域，但可能有些部分无法显示 */
            background-attachment: fixed;
            /* 背景滚动时，固定背景 */
        }

        .content{
            border: 1px solid blue;
            width: 40%;
            height: 150%;
            text-align: center;
            margin: 0 auto;
            margin-top: 1%;
            background:wheat;
            padding: 60px 70px;
        }

        .iconfont{
            margin-top:10px;
            font-size: 50px;
            position: absolute;
            margin-left:-20px;
        }

        .head_portrait{
            width: 60px;
            height: 60px;
            border-radius: 50%;
            position: absolute;
            top:40%;
            left: 35%;
            margin-left: -70px;
            margin-top: -130px;
            background: #737373;
            text-align: center;
        }

        .labels{
            margin-left:-90px;
            position: absolute;
            color: green;

        }


        #iconid{
            position: absolute;
            margin-left: -120px;
            margin-top:initial;
        }

        #iconzhanghao{
            position: absolute;
            margin-left: -120px;
            margin-top:initial;
        }

        #iconshenfen{
            position: absolute;
            margin-left: -120px;
            margin-top:initial;
        }

        .setHead{
            position: absolute;
            margin-left: -220px;
            margin-top: 180px;
            height: 30px;
            width: 60px;
        }

        .file1{
            margin-left: -17.2%;
            margin-top:1%;
            position: absolute;
        }

        .icon-xingming{
            margin-left: -20.5%;
            margin-top: 0%;
        }

        .iconyouxiang{
            margin-left: -20.5%;
            margin-top: 0%;
        }

    </style>
</head>
<body>
<div class="content">
    <div class="tit">
        <h1 style="color:white">用户信息</h1>
    </div>
    <img id="showimg" name="showimg" class="head_portrait" src="${sessionScope.managerViewFindUser.headProtrait}"><%--群头像--%>
    <label id="id" name="id" class="labels">id: ${sessionScope.managerViewFindUser.id}</label>
    <br/><br/>
    <label id="name" name="name" class="labels">名字： ${sessionScope.managerViewFindUser.name}</label>
    <br/><br/>
    <label id="account" name="account" class="labels">账号： ${sessionScope.managerViewFindUser.account}</label>
    <br/><br/>
    <label type="text" name="email" id="email" class="labels">邮箱: ${sessionScope.managerViewFindUser.email}</label>
    <br/><br/>
    <label name="protiait" id="protrait" class="labels">身份： ${sessionScope.managerViewFindUser.userKind}</label>
    <br/><br/>
</div>
</body>
</html>
