<%@ page import="jdk.management.resource.internal.inst.SocketOutputStreamRMHooks" %><%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/4/28
  Time: 18:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html>
<head>
    <!-- 静态包含css样式，标签，jquery文件 -->
    <%@ include file="/pages/common/head.jsp"%>
    <link type="text/css" rel="stylesheet" href="static/css/regist.css">
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2519156_8b0e74mbbz5.css"/>
    <title>用户注册</title>
    <script>
        //对注册进行检测
        //1. 对用户名检测，提示
        $(function (){
            var namePatt = /^.{3,20}$/ ;
            var emailRegex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/

            $("#name").change(function (){
                var nameText = $("#name").val();
              //对name判断,判断输入的格式和检测数据库
                //使用ajax name 的异步验证 checkname?name=xxxx
                if(this.value==null||this.value.trim().length==0){
                    $("#nameMsg").html("用户名为空").css("color","red");
                    return;
                }
                if(!namePatt.test(nameText)){
                    $("#nameMsg").html("用户名不合法").css("color","red");
                    <% session.setAttribute("MSG1", "error"); %>
                }else{
                    //用户名合法，检测
                    $.post("userServlet?method=checkName","name="+this.value,function(data){
                        if(data=="1"){
                            $("#nameMsg").html("用户名已经存在").css("color","red");
                        }else{
                            $("#nameMsg").html("用户名可用").css("color","green");
                        }
                    });
                }
                if($("#nameMsg").html()=="用户名可用" && $("#accountMsg").html()=="账号可用" && $("#userPasswordMsg").html()=="密码合法"&& $("#userRepPasswordMsg").html()=="密码一致"&& $("#userEmailMsg").html()=="邮箱格式正确"){
                    $("#reg_button").removeAttr("disabled");
                }
           });

            $("#account").change(function (){
               //对用户账号进行操作
                $.post("userServlet?method=checkAccount","account="+this.value,function(data){
                    if(data=="format_error"){
                        $("#accountMsg").html("账号不合法").css("color","red");
                        return;
                    }
                    if(data=="exist"){
                        $("#accountMsg").html("账号已存在").css("color","red");
                        return;
                    }
                    if(data=="no_exist"){
                        $("#accountMsg").html("账号可用").css("color","green");
                        return;
                    }
                    if(data=="null"){
                        $("#accountMsg").html("账号为空").css("color","red");
                        return;
                    }
                });
                if($("#nameMsg").html()=="用户名可用" && $("#accountMsg").html()=="账号可用" && $("#userPasswordMsg").html()=="密码合法"&& $("#userRepPasswordMsg").html()=="密码一致"&& $("#userEmailMsg").html()=="邮箱格式正确"){
                    $("#reg_button").removeAttr("disabled");
                }
            });

            //对密码操作
            $("#password").change(function (){
                //对用户密码进行操作
                $.post("userServlet?method=checkUserPassword","password="+this.value,function(data){
                   if(data=="null"){
                       $("#userPasswordMsg").html("密码为空").css("color", "red");
                       document.getElementById("userPasswordMsg").innerText="密码为空";
                   }else if(data=="false"){
                       $("#userPasswordMsg").html("密码格式错误").css("color", "red");
                       document.getElementById("userPasswordMsg").innerText="密码格式错误";
                   } else if(data=="true") {
                       $("#userPasswordMsg").html("密码合法").css("color", "green");
                       document.getElementById("userPasswordMsg").innerText="密码合法"
                   } else{
                       $("#userPasswordMsg").html("格式错误").css("color", "red")
                       document.getElementById("userPasswordMsg").innerText="格式错误";
                   }

                });
                if($("#nameMsg").html()=="用户名可用" && $("#accountMsg").html()=="账号可用" && $("#userPasswordMsg").html()=="密码合法"&& $("#userRepPasswordMsg").html()=="密码一致"&& $("#userEmailMsg").html()=="邮箱格式正确"){
                    $("#reg_button").removeAttr("disabled");
                }
            });

            $("#repwd").change(function (){
                var repw = $("#repwd").val();
                var pw = $("#password").val();
                if(repw==null||repw.trim().length==0){
                    $("#userRepPasswordMsg").html("密码为空").css("color", "red");
                    return;
                }
                if(repw != pw){
                    $("#userRepPasswordMsg").html("密码不一致").css("color", "red");
                }else if(repw == pw){
                    $("#userRepPasswordMsg").html("密码一致").css("color", "green");
                } else {
                    $("#userRepPasswordMsg").html("密码为空").css("color", "red");
                }
                if($("#nameMsg").html()=="用户名可用" && $("#accountMsg").html()=="账号可用" && $("#userPasswordMsg").html()=="密码合法"&& $("#userRepPasswordMsg").html()=="密码一致"&& $("#userEmailMsg").html()=="邮箱格式正确"){
                    $("#reg_button").removeAttr("disabled");
                }
            });
            //对邮箱验证
            $("#email").change(function (){
                var email = $("#email").val();
                if(email==null||email.trim().length==0){
                    $("#userEmailMsg").html("邮箱为空").css("color", "red");
                    document
                    return;
                }
                if(!emailRegex.test(email)){
                    //格式不正确
                    $("#userEmailMsg").html("邮箱格式不正确").css("color", "red");
                }else{
                    $("#userEmailMsg").html("邮箱格式正确").css("color", "green");
                }
                if($("#nameMsg").html()=="用户名可用" && $("#accountMsg").html()=="账号可用" && $("#userPasswordMsg").html()=="密码合法"&& $("#userRepPasswordMsg").html()=="密码一致"&& $("#userEmailMsg").html()=="邮箱格式正确"){
                    $("#reg_button").removeAttr("disabled");
                }
            });

            $("#chongzhi").click(function (){
                //清空文本款内容
                $("#account").val("");
                $("#name").val("");
                $("#password").val("");
                $("#email").val("");
                $("#repwd").val("");
                //清空msg内容
                $("#nameMsg").html("");
                $("#accountMsg").html("");
                $("#userPasswordMsg").html("");
                $("#userEmailMsg").html("");
                $("#userRepPasswordMsg").html("");

                //设置按钮为disable
                $("#reg_button").attr("disabled", true);
            });

        });

    </script>
</head>
<body>
    <div class="content">
        <div class="login_box">
            <div class="tit">
                <h1 style="color:white">注册MyWeChat会员</h1>
                <span class="errorMsg" style="color: red">
                    ${ requestScope.msg }
                </span>
            </div>
            <span name="check">
            <%-- form表单 --%>
            <div class="form">
                <form action="userServlet?method=regist" name="regist_form" method="post" id="form">
                    <i class="iconfont icon-denglu1"></i> <!-- 将来用来绘制account前面的图标 -->
                    <input class="itxt" type="text" placeholder="请输入用户名"
                           autocomplete="off" tabindex="1" name="name" id="name"
                           value="${requestScope.findmsg}"/>
                    <span class="help-block " id="nameMsg" name="nameMsg"></span>
                    <br/>
                    <br/>
                    <i class="iconfont icon-zhanghao_huabanfuben"></i> <!-- 将来用来绘制account前面的图标 -->
                    <input class="itxt" type="text" placeholder="请输入账号"
                           autocomplete="off" tabindex="1" name="account" id="account"
                           value="${requestScope.account}"/>
                    <label class="prompt"><span class="help-block " id="accountMsg" name="accountMsg"></span></label>
                    <br/>
                    <br/>
                    <i class="iconfont icon-denglumima-baise"></i>
                    <input class="itxt" type="password" placeholder="请输入密码"
                           autocomplete="off" tabindex="1" name="password" id="password"/>
                        <span class="help-block " id="userPasswordMsg" name="userPasswordMsg"></span>
                    <br />
                    <br />
                    <i class="iconfont icon-iconfontmima"></i>
                    <input class="itxt" type="password" placeholder="确认密码"
                           autocomplete="off" tabindex="1" name="repwd" id="repwd" />
                        <span class="help-block " id="userRepPasswordMsg" name="userRepPasswordMsg"></span>
                    <br />
                    <br />
                    <i class="iconfont icon-youxiang"></i><%--绘制邮箱--%>
                    <input class="itxt" type="text" placeholder="请输入邮箱地址"
                           autocomplete="off" tabindex="1" name="email" id="email"
                           value="${requestScope.email}"/>
                       <span class="help-block " id="userEmailMsg" name="userEmailMsg"></span>
                    <br />
                    <br />
                    <input type="submit" name="submit" class="submit" id="reg_button" value="注册" disabled="disabled" >&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" name="chongzhi" class="chongzhi" id="chongzhi" value="重置"><br>
                      <input type="radio" name="type" checked="checked" value="user"><span style="color: white">用户</span>
                <input type="radio" name="type" value="travel"><span style="color: white">游客</span>
                </form>
                <div class="login">
                    <a href="pages/user/login.jsp" style="color: navajowhite" class="a">退出注册</a>
                </div>

                <label name="msg1"></label><label name="msg2"></label><label name="msg3"></label><label name="msg4"></label><label name="msg5"></label>
            </div>
        </div>
    </div>

    </div>
</body>
</html>
