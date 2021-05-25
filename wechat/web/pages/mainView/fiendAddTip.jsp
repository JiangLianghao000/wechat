<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/8
  Time: 22:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/friendAddTip.css"/>
    <title>好友添加提示</title>
    <script>

        //初始化
        $(function () {
            //此时request未丢失
            $("#msgs2").click(function () {
                $("#btn_1").removeAttr("disabled");
                var target = event.target || event.srcElement;
                var td_id = target.id;
                //获取内容(不带span标签)
                var innerContent = target.innerHTML;
                $.post("userAndFriendServlet?method=findUserByAccountInTipJsp&account=" + innerContent, function (date){
                    if(date == "1"){
                        var accounTegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,16}$/;
                        if(accounTegex.test(innerContent)){
                            document.getElementById("iframe_1").src = "pages/others/userMessage.jsp";
                        }
                    }
                })
            });

            $("#btn_1").click(function (){
                $.post("userAndFriendServlet?method=userAgreeAddForFriend", function (date){
                    if(date == "1"){
                        $("#msg_1").html("已添加对方").css("green");
                    }else{
                        alert("网站异常，请联系管理员2429890953@qq.com反馈");
                    }
                });
            });

        });
    </script>
</head>
<body>
<div id="content">
    <div class="form_1">
        <h1 style="color: #a94442">好友或群聊申请通知</h1>
    </div>
    <%--中部--%>
    <div class="form_2">
        <%--添加表格--%>
        <ul class="fanshi1">
            <c:if test="${requestScope.lists.size() != 0 && requestScope.choiceAddTipByWhatWay == 'byUser'}">
                <table border="0" width="475px" height="300px" cellspacing="0" class="table" id="msgs2"
                       style="overflow-y: auto">
                    <c:forEach begin="0" end="${requestScope.lists.size()-1}" var="i">
                        <tr id="mags${i}">
                            <td id="mags${i}">
                                <img src="${requestScope.lists.get(i).friendHeadportrait}" class="style_1" id="picture"
                                     name="picture">
                                <span class="style_2" id="name${i}"
                                      name="name${i}">${requestScope.lists.get(i).friendAccount}</span>
                                <span class="style_3" id="name_1" name="name${i}" style="color: green">添加好友请求(查看信息请点击用户账号)：</span>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </ul>
        <iframe class="iframe_1" id="iframe_1" name="iframe_1">
        </iframe>
            <input type="submit" id="btn_1" name="btn_1" value="通过" class="btn" disabled="disabled">
            <span id="msg_1" style="color: red"></span>
    </div>
</div>
</body>
</html>
