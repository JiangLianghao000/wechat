
<%@ page import="static com.jianglianghao.util.storageUtils.*" %><%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/15
  Time: 20:06
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
    <title>管理员主界面</title>
    <script>
        $(function (){

            //封号
            $("#banAccount").click(function (){
                var ban_day = prompt("请输入要封禁的天数(1-999天)");
                var text = /-?[0-9]+(\\\\.[0-9]+)?/;
                if(text.test(ban_day) == false){
                    alert("请输入规定的数字");
                    return;
                }
                if(ban_day > 999 || ban_day < 0){
                    alert("请输入规定范围内的封号天数");
                }else{
                    $.post("managerServlet?method=banUser&day="+ban_day, function (date){
                        if(date == "0"){
                            alert("该账号早已被封");
                        }
                        if(date == "1"){
                            alert("已封号");
                        }
                        if(date == "-1"){
                            alert("你没有权限！！！");
                        }
                    });
                }
            });

            //解除封号
            $("#not_banAccoutn").click(function (){
                $.post("managerServlet?method=unBandUser", function (date){
                    if(date == "0"){
                        alert("该账号没有被封禁");
                    }
                    if(date == "1"){
                        alert("已解封");
                    }
                    if(date == "-1"){
                        alert("你无权利解封管理员的账号");
                    }
                    if(date == "-2"){
                        alert("不能自己封自己号")
                    }
                });
            });

            //点击查看
           $("#msg3").click(function (){
               $("#banAccount").removeAttr("disabled");
               $("#not_banAccoutn").removeAttr("disabled");
               var target = event.target || event.srcElement;
               var td_id = target.id;
               //获取内容(不带span标签)
               var innerContent = target.innerHTML;
               var accounTegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,16}$/;
               if(accounTegex.test(innerContent) || innerContent == "2429890953"){
                   //只有点击账号才可以查找
                   $.post("managerServlet?method=findUserInManagerView&account="+innerContent, function (date){
                       if(date =="1"){
                           document.getElementById("mid_content_iframe").src = "pages/others/ManagerFoundUserMsg.jsp";
                       }
                       if(date == "0"){
                           alert("没有找到该用户！！！");
                       }
                   });
               }
           });

           //通过账号搜索
            $("#find_userMsg").click(function (){
                $("#banAccount").removeAttr("disabled");
                $("#not_banAccoutn").removeAttr("disabled");
                //点击搜索用户来查找
                var account = $("#userMsg").val();
                if(account == ""){
                    alert("不能输入空的账号")
                }else{
                    $.post("managerServlet?method=findUserByAccountInManagerView&account="+account, function (date){
                        if(date == "0"){
                            alert("没有找到该用户");
                        }
                        if(date == "1"){
                            document.getElementById("mid_content_iframe").src = "pages/others/ManagerFoundUserMsg.jsp";
                        }
                    });
                }
            });

            //通过账号查找用户朋友圈
            $("#find_usercicle").click(function (){
                var account = $("#accountForCicle").val();
                if(account == ""){
                    alert("你输入的账号为空")
                }else{
                    $.post("managerServlet?method=findUserCircleByManagerPrintAccount&account="+account, function (date){
                        if(date == "0"){
                            alert("没有找到该用户");
                        }
                        if(date == "1"){
                            window.open("pages/mainView/ManagerFindUserCicle.jsp");
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
<div class="content">
    <div class="managerHead">
        管理员:${sessionScope.loginManager.name}, 欢迎登陆后台管理界面<br>
        登陆时间${sessionScope.managerLoginTime}
    </div>
    <div class="middle_content">
        <ul class="allUser">
            <table align="center" border="1" width="290" height="100" cellspacing="0" class="table_1" id="msg3">
                <c:if test="${requestScope.ManagerViewfindAllUser.size() != 0}">
                    <c:forEach begin="0" end="${requestScope.ManagerViewfindAllUser.size()-1}" var="i">
                        <tr id="mags1${i}" class="td_1">
                            <td id="mags1${i}">
                            <span class="style_2" id="name1_1${i}"
                                  name="name1_1${i}">${requestScope.ManagerViewfindAllUser.get(i).name}</span>
                                <span> ：</span>
                                <span class="style_2" id="name1_2${i}"
                                      name="name1_2${i}">${requestScope.ManagerViewfindAllUser.get(i).account}</span>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
            </table>
        </ul>
        <iframe class="mid_content_iframe" id="mid_content_iframe"></iframe>
        <button  name="banAccount" id="banAccount"  class="banAccount" disabled="disabled">封号</button>
        <button  name="not_banAccoutn" id="not_banAccoutn" class="not_banAccoutn" disabled="disabled">解封</button>
    </div>
    <div class="end_content">
        <span>请输入账号查找用户信息：</span>
        <input type="text" name="userMsg" id="userMsg" placeholder="请输入账号" class="accountForCicle" >
        <input type="button" name="find_userMsg" id="find_userMsg" value="查找用户" >
        <br>
        <span>请输入账号查找用户朋友圈：</span>
        <input type="text" name="accountForCicle" id="accountForCicle" placeholder="请输入账号" class="accountForCicle">
        <input type="button" name="find_usercicle" id="find_usercicle" value="查找朋友圈">
    </div>
</div>
</body>
</html>
