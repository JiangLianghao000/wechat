
<%@ page import="static com.jianglianghao.util.storageUtils.*" %><%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/16
  Time: 10:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/managerMainView.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2519156_doim610x4m7.css"/>
    <title>超管界面</title>

    <script>
        $(function () {
            //点击查看好友信息
            $("#msg4").click(function () {
                $("#banManager").removeAttr("disabled");
                $("#addToManager").removeAttr("disabled");
                var target = event.target || event.srcElement;
                var td_id = target.id;
                //获取内容(不带span标签)
                var innerContent = target.innerHTML;
                var accounTegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,16}$/;
                if (accounTegex.test(innerContent) || innerContent == "2429890953") {
                    //只有点击账号才可以查找
                    $.post("managerServlet?method=findUserInSuperManagerView&account=" + innerContent, function (date) {
                        if (date == "1") {
                            document.getElementById("mid_content_iframe").src = "pages/others/superManagerFoundUserMsg.jsp";
                        }
                        if (date == "0") {
                            alert("没有找到该用户！！！");
                        }
                    });
                }
            });

            //点击查询好友信息
            $("#find_userMsg").click(function (){
                $("#banManager").removeAttr("disabled");
                $("#addToManager").removeAttr("disabled");
                var account = $("#userMsg").val();
                if(account == ""){
                    alert("输入的账号为空");
                }else{
                    $.post("managerServlet?method=findUserByAccountInSuperManagerView&account="+account, function (date){
                        if(date == "0"){
                            alert("没有找到该用户");
                        }
                        if(date == "1"){
                            document.getElementById("mid_content_iframe").src = "pages/others/superManagerFoundUserMsg.jsp";
                        }
                    });
                }
            });

            //点击取消设置为管理员
            $("#banManager").click(function (){
                $.post("managerServlet?method=banManager", function (date){
                    if(date == "0"){
                        alert("该用户本身不是管理员")
                    }else if(date =="1"){
                        alert("已修改对方权限为普通用户")
                        document.getElementById("mid_content_iframe").src = "pages/others/superManagerFoundUserMsg.jsp";
                    }else if(date == "2"){
                        alert("不可以修改自己的权限");
                    }else if(date == "4"){
                        alert("不能对游客操作");
                    }else{
                        alert("程序异常，请联系管理员2429890953@qq.com进行修复");
                    }
                });
            });

            //点击解除管理员身份
            $("#addToManager").click(function (){
                $.post("managerServlet?method=addToManager", function (date){
                    if(date == "0"){
                        alert("该用户本身是管理员")
                    }else if(date =="1"){
                        alert("已修改对方权限为管理员")
                        document.getElementById("mid_content_iframe").src = "pages/others/superManagerFoundUserMsg.jsp";
                    }else if(date == "2"){
                        alert("不可以修改自己的权限");
                    }else if(date == "4"){
                        alert("不能对游客操作");
                    }else{
                        alert("程序异常，请联系管理员2429890953@qq.com进行修复");
                    }
                });
            });
        });
    </script>
</head>
<body>
<div class="content">
    <div class="managerHead" style="color: green">
        超管:${sessionScope.loginSuperManager.name}, 欢迎用户后台超管界面，你可以设置和取消管理员权限<br>
        登陆时间: ${requestScope.superManagerLoginTime}
    </div>
    <div class="middle_content">
        <ul class="allUser">
            <table align="center" border="1" width="290" height="100" cellspacing="0" class="table_1" id="msg4">
                <c:if test="${requestScope.SuperManagerViewfindAllUser.size() != 0}">
                    <c:forEach begin="0" end="${requestScope.SuperManagerViewfindAllUser.size()-1}" var="i">
                        <tr id="mags1${i}" class="td_1">
                            <td id="mags1${i}">
                            <span class="style_2" id="name1_1${i}"
                                  name="name1_1${i}">${requestScope.SuperManagerViewfindAllUser.get(i).name}</span>
                                <span> ：</span>
                                <span class="style_2" id="name1_2${i}"
                                      name="name1_2${i}">${requestScope.SuperManagerViewfindAllUser.get(i).account}</span>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
            </table>
        </ul>
        <iframe class="mid_content_iframe" id="mid_content_iframe"></iframe>
        <button  name="banAccount" id="banManager"  class="banAccount" disabled="disabled">解除管理员权限</button>
        <button  name="addToManager" id="addToManager" class="not_banAccoutn" disabled="disabled">设置为管理员</button>
    </div>
    <div class="end_content">
        <span>请输入账号查找用户信息：</span>
        <input type="text" name="userMsg" id="userMsg" placeholder="请输入账号" class="accountForCicle" >
        <input type="button" name="find_userMsg" id="find_userMsg" value="查找用户" >
    </div>
</div>
</body>
</html>
