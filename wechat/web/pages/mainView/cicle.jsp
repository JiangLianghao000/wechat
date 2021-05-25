<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/12
  Time: 15:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/cicle.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2543693_5819zdiac7k.css"/>
    <title>朋友圈</title>
    <script>
        $(function (){
            $("#addCircle").click(function (){
                window.open("pages/others/htmleditor.jsp");
            });

            $("#icon-ziji").click(function (){
                $.post("circleServlet?method=findUserServlet", function (date){
                    if(date == "1"){
                        location = location;
                    }else{
                        alert("程序异常！！！");
                    }
                });
            })

            $("#icon-qitarenyuan").click(function (){
                $.post("circleServlet?method=findAllFriendCircle", function (date){
                    if(date == "1"){
                        location = location;
                    }
                    if(date == "0"){
                        alert("好友没有发动态")
                    }
                    if(date == "2"){
                        alert("程序异常，请联系管理员反馈");
                    }
                });
            })

            $("#msgs2").click(function (){
                var target = event.target || event.srcElement;
                var td_id = target.id;
                //获取内容(不带span标签)
                var innerContent = target.innerHTML
                var pattern=/^\d+$/g;
                if(innerContent.match(pattern)){
                    $.post("circleServlet?method=findFriendCircleMsg&id=" + innerContent, function (date){
                        window.open("pages/mainView/cicleMsgView.jsp");
                    })
                }
            })
        });
    </script>
</head>
<body>
    <div id="content">
        <div class="near">
            <br><br><br><br>
            <span style="color: white">自己朋友圈</span><br><br>
            <i class="iconfont icon-ziji" id="icon-ziji" name="icon-ziji"></i>
            <br><br><br><br><br><br>
            <span style="color: white">好友朋友圈</span><br><br>
            <i class="iconfont icon-qitarenyuan" id="icon-qitarenyuan" name="icon-qitarenyuan"></i><br>
            <br><br><br><br><br><br>
            <span style="color: white">添加朋友圈</span><br><br>
            <i class="iconfont icon-add-fill-hover" id="addCircle" name="addCircle"></i><br>
        </div>
        <ul class="middle_cicle">
            <table align="center" border="1"  cellspacing="0" class="table" id="msgs2">
                <c:if test="${sessionScope.circleByWhat eq 'myself' && sessionScope.circles.size() != 0}">
                    <c:forEach begin="0" end="${sessionScope.circles.size() - 1}" var="i">
                        <tr id="mags1${i}" class="td_1">
                            <td id="mags1${i}">
                                <span style="color: green">动态id：(点击id查看详情)：</span>
                                <span>${sessionScope.circles.get(i).contentId}</span><br>
                                <div name="rachEdit${i}" contenteditable="false"  class="rachEdit" id="rachEdit${i}">
                                    内容：${sessionScope.circles.get(i).content}
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${sessionScope.circleByWhat eq 'friend' && sessionScope.circles.size() != 0}">
                    <c:forEach begin="0" end="${sessionScope.circles.size() - 1}" var="i">
                        <tr id="mags1${i}" class="td_1">
                            <td id="mags1${i}">
                                <span style="color: green">动态id：(点击id查看详情)：</span>
                                <span>${sessionScope.circles.get(i).contentId}</span><br>
                                <div name="rachEdit${i}" contenteditable="false"  class="rachEdit" id="rachEdit${i}">
                                    内容：${sessionScope.circles.get(i).content}
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
            </table>
        </ul>
    </div>

</body>
</html>
