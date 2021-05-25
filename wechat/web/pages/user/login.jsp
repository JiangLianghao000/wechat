<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/4/28
  Time: 18:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- 静态包含css样式，标签，jquery文件 -->
    <meta charset="UTF-8">
    <%@ include file="/pages/common/head.jsp"%>
    <link type="text/css" rel="stylesheet" href="static/css/login.css" />
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2519156_l9u3nnks8oc.css"/>
    <title>用户登陆</title>
    <script type="text/javascript">

        $(function (){
            //改变验证码
            $("#yzmCode").click(function (){
                $("#yzmCode").attr("src","userServlet?method=code&n="+Math.random());
            });
            //对验证码进行确定
            $("#verifycode")
        });
        //对登陆操作
    </script>
</head>
<body>
    <!-- 头部图片 -->
    <div id="login_header">
        <img class="logo_img" alt="" src="static/img/weixin.jpeg" width="100px" height="100px">
    </div>

    <div id="content">
        <div class="login_form" align="center">
            <div class="login_box" align="center">
                <!-- 头部信息 -->
                <br class="tit">
                    <h1 style="color: white">MyWeChat用户登陆</h1>
                     <a href="pages/user/regist.jsp" style="color: blue" class="regist_i" class="a_regist">没有账户？立即注册</a></br>
                    <span class="login_msg" style="color: red"  font-size: 20px>${ empty requestScope.msg ? "请输入用户名和密码":requestScope.msg }</span>
                </div>
                <br/>
                <!-- 中部form表单 -->
                <div class="form">
                    <form action="userServlet?method=login" method="post">
                            <i class="iconfont icon-denglu1"></i> <!-- 将来用来绘制account前面的图标 -->

                            <input class="itxt" type="text" placeholder="请输入账号"
                                   autocomplete="off" tabindex="1" name="useraccount"
                                   value="${requestScope.useraccount}"/>
                             <label id="nameMsg"></label>
                            <br/>
                            <br/>
                            <i class="iconfont icon-denglumima-baise" aria-hidden="true"></i> <!-- 将来用来绘制password前面的图标 -->
                            <input class="itxt" type="password" placeholder="请输入密码"
                                   autocomplete="off" tabindex="1" name="password"/>
                            <br/>
                            <br/>
                            <i class="iconfont icon-dengluyanzhengma" aria-hidden="true"></i> <!-- 将来用来绘制验证码前面的图标 -->
                        <input class="itxt" type="text" placeholder="请输入验证码"
                               autocomplete="off" tabindex="1" name="verifycode"/>
                        <img src="userServlet?method=code" id="yzmCode" class="yzm" height="30px" width="100px"/>
                        <br/>
                        <br/>
                        <button type="submit" name="login" class="loginbutton">LOGIN</button>
                        <br/>
                        <br/>
                        <div class="auto">
                            <label id="autoFont"> 两周内自动登陆 </label> &nbsp;
                            <input  id="autoLogin" name="auto" type="checkbox" height="100px" width="100px"/>
                        </div>
                    </form>
                </div>
                <div class="find_password">
                    <label style="color: white">忘记密码？</label><a href="pages/user/find_password.jsp" style="color: blue">找回密码</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
