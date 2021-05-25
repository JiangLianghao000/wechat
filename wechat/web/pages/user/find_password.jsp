<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/4/28
  Time: 22:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- 静态包含css样式，标签，jquery文件 -->
    <meta charset="UTF-8">
    <%@ include file="/pages/common/head.jsp" %>

    <link type="text/css" rel="stylesheet" href=" //at.alicdn.com/t/font_2519156_8b0e74mbbz5.css">
    <link type="text/css" rel="stylesheet" href="static/css/findPassword.css">
    <title>找回密码</title>
    <script>
        //设置点击退出跳转到登陆界面的函数
        function jump() {
            window.location.href = "pages/user/login.jsp";
        }

        //对用户账号和密码规范检测
        $(function () {
            var emailRegex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
            $("#account").change(function () {

                //通过数据库验证账号是否存在以及确定账号的规范
                $.post("userServlet?method=checkAccount", "account=" + this.value, function (data) {
                    if (data == "null") {
                        $("#accountMsg").html("账号为空").css("color", "red");
                    } else if (data == "format_error") {
                        $("#accountMsg").html("账号格式错误").css("color", "red");
                    } else if(data == "exist"){
                        $("#accountMsg").html("账号合法").css("color", "green");
                    } else{
                        $("#accountMsg").html("账号不存在").css("color", "red");
                    }
                    if ($("#accountMsg").html() == "账号合法" && $("#userEmailMsg").html() == "邮箱格式正确") {
                        $("#submit").removeAttr("disabled");
                    }
                });
            });

            //对邮箱操作
            $("#email").change(function () {
                //获取账号
                var $account = $("#account").val();
                var email = $("#email").val();
                if (email == null || email.trim().length == 0) {
                    $("#userEmailMsg").html("邮箱为空").css("color", "red");
                    return;
                }
                //验证该邮箱是不是这个用户的
                $.post("userServlet?method=checkEmail&account=" + $account, "email=" + this.value , function (data) {
                    if (!emailRegex.test(email)) {
                        //格式不正确
                        $("#userEmailMsg").html("邮箱格式不正确").css("color", "red");
                    } else if (data == "exist") {
                        $("#userEmailMsg").html("邮箱正确").css("color", "green");
                    } else {
                        $("#userEmailMsg").html("该邮箱和用户不匹配").css("color", "red");
                    }
                    if ($("#accountMsg").html() == "账号合法" && $("#userEmailMsg").html() == "邮箱正确") {
                        $("#submit").removeAttr("disabled");
                    }
                });
            });

            //改变验证码
            $("#yzmCode").click(function () {
                $("#yzmCode").attr("src", "userServlet?method=code&n=" + Math.random());
            });

            //重置
            $("#restart").click(function () {
                if ($("#accountMsg").html() == "账号合法" && $("#userEmailMsg").html() == "邮箱格式正确") {
                    $("#submit").removeAttr("disabled");
                }
                $("#userEmailMsg").html("");
                $("#accountMsg").html("");
                $("#account").val("");
                $("#email").val("");
                //设置按钮为disable
                $("#submit").attr("disabled", true);
            });
        });
    </script>
</head>
<body>
<div class="content">
    <div class="login_box">
        <div class="tit">
            <h1 style="color:white">找回密码</h1>
            <span class="errorMsg" style="color: white">
                ${ requestScope.msg }
            </span>
        </div>
        <%-- form表单 --%>
        <div class="form">
            <form action="userServlet?method=findPassword" name="regist_form" method="post">
                <i class="iconfont icon-zhanghao_huabanfuben"></i> <!-- 将来用来绘制account前面的图标 -->
                <input class="itxt" type="text" placeholder="请输入账号"
                       autocomplete="off" tabindex="1" name="account" id="account"/>
                <label class="prompt"><span class="help-block " id="accountMsg"></span></label>
                <br/>
                <br/>
                <i class="iconfont icon-youxiang"></i><%--绘制邮箱--%>
                <input class="itxt" type="text" placeholder="请输入邮箱"
                       autocomplete="off" tabindex="1" name="email" id="email"/>
                <span class="help-block " id="userEmailMsg"></span>
                <br/>
                <br/>
                <i class="iconfont icon-dengluyanzhengma" aria-hidden="true"></i> <!-- 将来用来绘制验证码前面的图标 -->
                <input class="itxt" type="text" placeholder="请输入验证码"
                       autocomplete="off" tabindex="1" name="verifycode"/>
                <img src="userServlet?method=code" id="yzmCode" class="yzm" height="30px" width="100px"/>
                <br/>
                <br/>
                <input type="submit" name="submit" id="submit" class="submit" value="查询" disabled="disabled">
                <input type="button" onclick="javascript:jump()" name="exit" id="exit" class="submit" value="退出">
                <input type="button" name="restart" id="restart" class="submit" value="重置">
            </form>
        </div>
    </div>
</div>
</body>
</html>
