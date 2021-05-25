
<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/10
  Time: 9:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/createGroup.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2537798_7qzxuqxg18.css"/>
    <title>创建群聊</title>
    <script>
        //显示文件信息
        function show(f) {
            var reader = new FileReader();//创建文件读取对象
            var files = f.files[0];//获取file组件中的文件
            reader.readAsDataURL(files);//文件读取装换为base64类型
            reader.onloadend = function (e) {
                //加载完毕之后获取结果赋值给img
                document.getElementById("showimg").src = this.result;
            }

        };
    </script>
</head>
<body>
    <div id="content">
        <form action="userAndGroupServlet?method=createGroup" method="post" >
            <h1 style="color: mediumaquamarine" class="warn"> 创建群聊 </h1>
            <i class="iconfont icon-qunzhu" id="qunzhu" name="qunzhu"></i>
            <h3 style="color: #aabbcc" class="groupCreater">群主：${sessionScope.loginUser.name}</h3>
            <br>
            <i class="iconfont icon-qunzhanghao" id="qunzhanghao" name="qunzhanghao"></i>
            <input type="text"  id="account" class="itxt" name="account" placeholder="请输入群账号">
            <br><br>
            <i class="iconfont icon-qunmingpian" id="qunmingpian" name="qunmingpian"></i>
            <input type="text"   id="name" class="itxt" name="name" placeholder="请输入群名">
            <br><br>
            <i class="iconfont icon-jianjie" id="icon-jianjie" name="jianjie"></i>
            <textarea class="setIntroduction" id="jianjie" class="jianjie" name="jianjies" placeholder="请输入群简介"></textarea>
            <br><br>
            <input type="submit" id="btn_1" name="btn_1" class="btns" value="创建群聊">
        </form>
        <br><br>
        <span style="color: red" id="tipMsg">${requestScope.exceotipn}</span>
    </div>
</body>
</html>
