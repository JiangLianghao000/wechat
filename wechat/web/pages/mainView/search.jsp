<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/5
  Time: 20:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/search.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2519156_1uc9etarmja.css"/>
    <title>搜索窗口</title>
    <script>
        $(function (){
            $("#searchType1").click(function (){
                //点了之后才会取消disable，确保用户一定选择了群聊，否则不能取消
                $("#submit").removeAttr("disabled");
                $("#msg1").html("");
            });
            $("#searchType2").click(function (){
                //点了之后才会取消disable，确保用户一定选择了群聊，否则不能取消
                $("#submit").removeAttr("disabled");
                $("#msg1").html("");
            });
           alert("请选择群聊或者用户搜索，否则搜索不了")
        });
    </script>
</head>
<body>
<div id="content">
    <h1 style="color: mediumaquamarine" class="warn"> 查找用户或者群聊</h1>
            <form method="post" action="userAndFriendServlet?method=findFriend" class="search">
                <br><br>
                <br><br>
                <input type="radio" name="searchType" id="searchType1" class="radios" value="用户">
                <label class="labels1">用户</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="searchType" id="searchType2" class="radios" value="群聊"/>
                <label class="labels2">群聊</label>
                <br>
                <i class="iconfont icon-qunmingcheng" id="i_groupName" name="name"></i> <!-- 将来用来绘制群名前面的图标 -->
                <input type="text" class="inputs" id="name" name="name" placeholder="请输入名字"/>
                <br><br>
                <i class="iconfont icon-Uqunzhanghao" id="i_groupAccount" name="qunliao"></i> <!-- 将来用来绘制群聊账号的图标 -->
                <input type="text" class="inputs" id="account" name="account" placeholder="请输入账号"/>
                <br>

                <input type="submit" name="submit" id="submit" class="itxt" disabled="disabled">
            </form>
            <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <span class="msg" id="msg1" name="msg1" style="color: red">${requestScope.findmsg}</span>
            <div class="login_form">
                <i class="iconfont icon-haoyou" id="haoyou" name="haoyou"></i> <!-- 将来用来绘制好友前面的图标 -->
                <br><br><br><br><br>
                <i class="iconfont icon-qunliao" id="qunliao" name="qunliao"></i> <!-- 将来用来绘制群聊前面的图标 -->
                <br>
                <br>
            </div>
</div>
</body>
</html>
