<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/16
  Time: 0:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        .content{
            background:whitesmoke;
            width: 50%;
            height: 80vh;
            border: 1px solid #ccc;
            margin-top: -1px;
            margin-left: -9px;
        }

        .head_1{
            border: 1px solid white;
            width: 105.5%;
            height: 15%;
            text-align: center;
            margin-left: -5%;
            margin-top: -2%;
            background: silver;
        }

        .friend_cicle{
            background: whitesmoke;
            width: 51.5%;
            height: 80vh;
            border: 1px solid #ccc;
            margin-top: -529px;
            margin-left: 49%;
        }

        .buttom{
            background: #e1e5e8;
            width: 101.3%;
            height: 7vh;
            border: 1px solid #ccc;
            margin-left: -10px;
            text-align: center;
        }

        .deleteCicle{
            margin-top: 1%;
        }

        .allCircle{
            border-color: #ccc;
            width: 600px;
            height: 500px;
            background-color: ghostwhite;
            overflow: auto;
        }

        .table_1{
            width: 103%;
            margin-left: -6.7%;
            text-align: center;
            overflow: auto;
        }

        .td_2{
            height: 60px;
        }
    </style>
    <title>管理员查找用户朋友圈</title>
    <script>
        $(function () {
            //点击查看朋友圈
            $("#msgs4").click(function () {
                var target = event.target || event.srcElement;
                var td_id = target.id;
                //获取内容(不带span标签)
                var innerContent = target.innerHTML
                var pattern = /^\d+$/g;
                if (innerContent.match(pattern)) {
                    $("#deleteCicle").removeAttr("disabled");
                    $.post("managerServlet?method=findContent&id="+innerContent, function (date){
                        $("#friend_cicle").html(date);
                    });
                }
            });

            //点击删除朋友圈
            $("#deleteCicle").click(function (){
                $.post("managerServlet?method=deleteCircle", function (date){
                    if(date == "1"){
                        alert("已删除")
                      window.location.href = ("pages/mainView/ManagerFindUserCicle.jsp")
                    }else if(date == "-1"){
                        alert("你没有权限");
                    }else{
                        alert("程序异常，请联系管理员2429890953@qq.com反馈问题");
                    }
                });
            });
        });
    </script>
</head>
<body>
    <div class="head_1">
        <h1 style="color: green">用户${sessionScope.managerFindCicle.size() == 0?"":sessionScope.managerFindCicle.get(0).userName}的朋友圈</h1>
    </div>
    <div class="content">
        <ul class="allCircle">
            <table align="center" border="1"  cellspacing="0" class="table_1" id="msgs4">
            <c:if test="${sessionScope.managerFindCicle.size() != 0}">
                    <c:forEach begin="0" end="${sessionScope.managerFindCicle.size() - 1}" var="i">
                        <tr class="td_2">
                            <td>
                                <span style="color: green">动态id：(点击id查看详情)：</span>
                                <span>${sessionScope.managerFindCicle.get(i).contentId}</span><br>
                                <span style="color: green">用户名字: </span>
                                <span>${sessionScope.managerFindCicle.get(i).userName}</span><br>
                            </td>
                        </tr>
                    </c:forEach>
            </c:if>
            </table>
        </ul>
    </div>
    <div class="friend_cicle" contenteditable="false" id="friend_cicle" name="friend_cicle"></div>
    <div class="buttom">
        <button name="deleteCicle" id="deleteCicle"  disabled="disabled" style="" class="deleteCicle">删除朋友圈</button>
    </div>
</body>
</html>
