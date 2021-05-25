<%@ page import="com.jianglianghao.util.JDBCUtils" %>
<%@ page import="com.jianglianghao.util.PasswordUtil" %>
<%@ page import="static com.jianglianghao.util.storageUtils.*" %><%--
  用户主界面
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/1
  Time: 20:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/userMain.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2519156_c2jcn9kjoyn.css"/>
    <title>用户界面</title>
    <script>
        $(function () {
            //设置界面
            $("#shezhi").click(function () {
                //打开设置界面，用json把user数据传入另一个界面中
                $.getJSON("userMSGServlet?method=getUserMSG", function (data) {
                    //获取到json对象，就可以开始调用了，把json对象setAttribute
                    //alert(${sessionScope.loginUser.email})
                    window.open("pages/mainView/userSetting.jsp");
                });
            });

            //退出
            $("#tuichu").click(function () {
                var r = confirm("你确定要注销吗？注销之后自动登陆取消");
                if (r == true) {
                    $.post("userMSGServlet?method=exit", function (date) {
                        if (date == "1") {
                            //定位到localhost:8080
                            window.location.href = "/wechat/pages/user/login.jsp";
                        }
                    });
                }
            });

            //反馈界面
            $("#fankui").click(function () {
                //点击反馈界面进行反馈
                window.open("pages/mainView/feedback.jsp");
            });

            //点击好友界面弹出用户所有好友
            $("#haoyou").click(function () {
                $.post("userAndFriendServlet?method=getAllFriend", function (date) {
                    if (date == "0") {
                        alert("好友数为0")
                    } else {
                        window.location.href = ("pages/mainView/userMain.jsp");
                    }
                });
            });

            //点击第一个微信图标弹出消息提示
            $("#weixin").click(function () {
                //调用方法获取参数
                $.post("userAndFriendServlet?method=getAllMessage", function (date) {
                    if (date == "0") {
                        alert("没有信息")
                    } else {
                        window.location.href = ("pages/mainView/userMain.jsp")
                    }
                });
            });

            //点击搜索好友和群
            $("#sousuo").click(function () {
                //弹出搜索框
                window.open("pages/mainView/search.jsp", "搜索");
            });

            //点击信息查看
            $("#msgs1").click(function () {
                //点击了查看信息就可以点击按键
                $("#hadRead").removeAttr("disabled");
                $("#deleteMessage").removeAttr("disabled");
                //获取点击了哪个td
                var target = event.target || event.srcElement;
                var td_id = target.id;
                //获取内容(不带span标签)
                var innerContent = target.innerHTML
                //去除span标签
                //var inner = test.replace(/<\s*span\s*>.*<\s*\/\s*span\s*>/gi,innerContent)
                //alert(inner)
                //展示在area中
                $("#msgContent").html(innerContent);
            });

            //点击了解
            $("#hadRead").click(function (){
                //获取html内容
                var content = $("#msgContent").val();
                //后台收到请求设置颜色
                $.post("userMSGServlet?method=hadRead&content=" + content, function (date){
                    //设置为know
                    if(date == "1"){
                        alert("已阅");
                        //重新加载页面
                        location = location;
                    }else{
                        alert("设置，请联系管理员")
                    }
                });
            });

            //点击删消息
            $("#deleteMessage").click(function (){
                var r = confirm("你确定要删除吗");
                if(r == true){
                    var content = $("#msgContent").val();
                    //调用方法删除
                    $.post("userMSGServlet?method=deleteMessage&content="+ content , function (date){
                        if(date == "1"){
                            alert("已删除");
                            location = location;
                        }else{
                            alert("删除失败，请联系管理员")
                        }
                    });
                }
            });

            //点击查看好友申请信息
            $("#req_add_friend").click(function (){
                var fontName = prompt("请输入数字（1或2）, 群聊为1，好友为2");
                if(fontName == "2"){
                    window.open("userAndFriendServlet?method=getFriendApplyMsg");
                }
                if(fontName == "1"){
                    window.open("pages/mainView/friendAddGroupTip.jsp");
                }
            });

            //点击群聊图标
            $("#qunliao").click(function (){
                $.post("userAndGroupServlet?method=findAllGroups", function (date) {
                    if (date == "0") {
                        alert("群聊数为0")
                    } else {
                        window.location.href = ("pages/mainView/userMain.jsp");
                    }
                });
            });

            //点击群聊的信息
            $("#msgs2").click(function (){
                $("#button_2").removeAttr("disabled");
                $("#button_3").removeAttr("disabled");
                $("#button_4").removeAttr("disabled");
                var target = event.target || event.srcElement;
                var td_id = target.id;
                //获取内容(不带span标签)
                var innerContent = target.innerHTML;
                $.post("userAndGroupServlet?method=findGroupInMain&groupAccount=" + innerContent, function (date){
                    if(date == "1"){
                        var accounTegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,16}$/;
                        if(accounTegex.test(innerContent)){
                            document.getElementById("iframe_1").src = "pages/others/userGroupMsg.jsp";
                        }
                    }
                })
            });

            //点击创建群聊
            $("#create_groups").click(function (){
                window.open("pages/mainView/createGroup.jsp");
            });

            //单击解散群聊
            $("#button_2").click(function (){
                var r = confirm("你确定要解散该群聊吗？");
                if(r == true){
                    $.post("userAndGroupServlet?method=leftGroup", function (data){
                       if(data == "1"){
                           $("#into_group_msg").html("已解散该群");
                       }
                    });
                }
            });

            //点击朋友圈图标
            $("#pengyouquan").click(function (){
                window.open("pages/mainView/cicle.jsp");
            });

            //点击修改群聊信息
            $("#button_4").click(function (){
                /*判断，如果不是群主是不可以修改的*/
                window.open("pages/others/midifyGroupMsg.jsp");
            });

            //点击好友的消息
            $("#msgs3").click(function (){
                $("#button_11").removeAttr("disabled");
                $("#button_12").removeAttr("disabled");
                $("#button_13").removeAttr("disabled");
                $("#button_14").removeAttr("disabled");
                var target = event.target || event.srcElement;
                var td_id = target.id;
                var accounTegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,16}$/;
                //获取内容(不带span标签)
                var innerContent = target.innerHTML;
                //只有点了账号才能进入
                if(accounTegex.test(innerContent)){
                    $.post("userAndFriendServlet?method=findFriendMsg&friendAccount=" + innerContent, function (date){
                        if(date == "1"){
                            document.getElementById("iframe_1").src = "pages/others/friendMsg.jsp";
                        };
                    });
                }
            })

            //拉黑好友
            $("#button_11").click(function (){
                $.post("userAndFriendServlet?method=addToBlackList", function (date){
                    if(date == "1"){
                        alert("已拉黑好友，对方将无法查看朋友圈以及无法进行聊天");
                        document.getElementById("iframe_1").src = "pages/others/friendMsg.jsp";
                    }
                });
            });

            //取消拉黑好友
            $("#button_12").click(function (){
                $.post("userAndFriendServlet?method=NotAllToBlackList", function (date){
                    if(date == "1"){
                        alert("已取消拉黑好友");
                        document.getElementById("iframe_1").src = "pages/others/friendMsg.jsp";
                    }
                });
            });

            //修改备注
            $("#button_13").click(function (){
                var namePatt = /^.{3,20}$/ ;
                var lineVal = prompt("请输入要修改的备注");
                if(lineVal == ""){
                    alert("还没有输入备注")
                }else if(namePatt.test(lineVal) == false){
                    alert("请输入3-20个字符")
                }else{
                    $.post("userAndFriendServlet?method=modifyNote&note="+lineVal, function (date){
                        if(date == 1){
                            alert("已修改");
                            document.getElementById("iframe_1").src = "pages/others/friendMsg.jsp";
                        }
                    });
                }
            });

            //删除好友
            $("#button_14").click(function (){
                var test = confirm("你确定要删除好友吗");
                if(test == true){
                    $.post("userAndFriendServlet?method=deleteFriend", function (date){
                        if(date=="1"){
                            alert("已删除好友");
                        }
                    });
                }
            });

            //进入好友聊天
            $("#icon-news").click(function (){
                window.open("userChatServlet?method=intoChat");
            });

            //进入群聊
            $("#icon-liaotianshi").click(function (){
                window.open("pages/mainView/groupChat.jsp");
            })

            $("#icon-qiapian").click(function (){
                window.open("pages/others/card.jsp");
            });
        });
    </script>
</head>

<body>
<div id="content">
    <div class="login_form">
        <img src="${sessionScope.loginUser.headProtrait}" id="myFile" class="head_portrait" value="头像">
        <br><br><br><br><br><br>
        <i class="iconfont icon-weixin" id="weixin" name="weixin" onclick='' title="消息"></i> <!-- 将来用来绘制微信前面的图标 -->
        <br>
        <br>
        <br>
        <i class="iconfont icon-haoyou" id="haoyou" name="haoyou" title="好友"></i> <!-- 将来用来绘制好友前面的图标 -->
        <br>
        <br>
        <br>
        <i class="iconfont icon-qunliao" id="qunliao" name="qunliao" title="群聊"></i> <!-- 将来用来绘制群聊前面的图标 -->
        <br>
        <br>
        <br>
        <i class="iconfont icon-pengyouquan" id="pengyouquan" name="pengyouquan" title="朋友圈"></i> <!-- 将来用来绘制朋友圈前面的图标 -->
        <br>
        <br>
        <br>
        <i class="iconfont icon-fankui" id="fankui" name="fankui" title="反馈"></i> <!-- 将来用来绘制反馈前面的图标 -->
        <br>
        <br>
        <br>
        <i class="iconfont icon-shezhi" id="shezhi" name="shezhi" action="userMSGServlet?method=getUserMSG" title="设置"></i>
        <!-- 将来用来绘制设置前面的图标 -->
        <br>
        <br>
        <br>
        <i class="iconfont icon-tuichu" id="tuichu" name="tuichu" action="" title="注销"></i> <!-- 将来用来绘制退出前面的图标 -->
        <br>
        <br>
        <i class="iconfont icon-xingtaiduICON_sousuo--" id="sousuo" name="icon-xingtaiduICON_sousuo--" action="" title="搜索"></i>
        <!-- 将来用来绘制搜索前面的图标 -->
    </div>
    <%--第二个区域--%>
    <ul class="fanshi">
        <%--点击微信按键，弹出消息提示--%>
        <%--选择了choiceAllMsg--%>
        <c:if test="${sessionScope.mainView_action eq 'choiceAllMsg'}">
            <%System.out.println(request.getSession().getAttribute("allUserMsgList"));%>
            <table align="center" border="1" width="290" height="200" cellspacing="0" class="table_1" id="msgs1">
                <c:forEach begin="0" end="${sessionScope.allUserMsgList.size() - 1}" var="i">
                <tr>
                    <td id="oneMsg${i}" name="oneMsg${i}" align="left">
                        <%--判断，如果是已经阅读了的，就直接显示绿色，否则红色--%>
                        <c:if test="${sessionScope.allUserMsgList.get(i).state eq 'know'}">
                            <span id="oneMsg" name="oneMsg" style="color: green">${sessionScope.allUserMsgList.get(i).content}</span>
                        </c:if>
                        <c:if test="${sessionScope.allUserMsgList.get(i).state eq 'unknow'}">
                                <span id="oneMsg" name="oneMsg" style="color: red">${sessionScope.allUserMsgList.get(i).content}</span>
                        </c:if>
                    </td>
                </tr>
                </c:forEach>
            </table>
        </c:if>
        <%--点击了群聊图标--%>
        <c:if test="${sessionScope.mainView_action eq 'choiceAllGroupsMsg'}">
            <table align="center" border="1" width="290" height="100" cellspacing="0" class="table_1" id="msgs2">
                <c:forEach begin="0" end="${sessionScope.allGroupMsgList.size() - 1}" var="i">
                    <tr id="mags1${i}" class="td_1">
                        <td id="mags1${i}">
                            <span class="style_2" id="name1_1${i}"
                                  name="name1_1${i}">${sessionScope.allGroupMsgList.get(i).groupName}</span>
                            <span> ：</span>
                            <span class="style_2" id="name1_2${i}"
                                  name="name1_2${i}">${sessionScope.allGroupMsgList.get(i).groupAccount}</span>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
            <%--点击了好友图标--%>
        <c:if test="${sessionScope.mainView_action eq 'choiceFriend'}">
            <c:if test="${sessionScope.findAllFriends.size() != 0}">
            <table align="center" border="1" width="290" height="100" cellspacing="0" class="table_1" id="msgs3">
                <c:forEach begin="0" end="${sessionScope.findAllFriends.size() -1}" var="i">
                    <tr id="mags1${i}" class="td_1">
                        <td id="mags1${i}">
                            <span class="style_2" id="name1_1${i}"
                                  name="name1_1${i}">${sessionScope.findAllFriends.get(i).friendName}</span>
                            <span> ：</span>
                            <span class="style_2" id="name1_2${i}"
                                  name="name1_2${i}">${sessionScope.findAllFriends.get(i).friendAccount}</span>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            </c:if>
        </c:if>
    </ul>
    <%--第三个区域--%>
    <div class="middle">
        <%--选择了choiceAllMsg--%>
        <c:if test="${sessionScope.mainView_action eq 'choiceAllMsg'}">
            <br><br><br>
            <span id="title" name="title" style="color: green">消息内容，如果看完，请选择已了解</span>
            <br><textarea class="text1" id="msgContent" name="msgContent" disabled ></textarea>
            <br>
            <input type="button" name="hadRead" id="hadRead" class="hadRead" value="已了解" disabled="disabled">
            <input type="button" name="deleteMessage" id="deleteMessage" class="hadRead" value="删除信息" disabled="disabled">
        </c:if>
        <%--选则了群聊--%>
        <c:if test="${sessionScope.mainView_action eq 'choiceAllGroupsMsg'}">
            <iframe class="iframe_1" id="iframe_1" name="iframe_1" width="600px" height="500px"></iframe>
            <input type="button" class="into_group" id="button_2" name="button_2" value="解散群聊" disabled="disabled">
            <input type="button" class="into_group" id="button_3" name="button_3" value="进入群聊" disabled="disabled"><br>
            <input type="button" class="into_group" id="button_4" name="button_3" value="修改信息" disabled="disabled">
            <br>
            <span style="color: red" id="into_group_msg"></span>
        </c:if>
        <%--选择了好友查看--%>
        <c:if test="${sessionScope.mainView_action eq 'choiceFriend'}">
            <iframe class="iframe_1" id="iframe_1" name="iframe_1" width="600px" height="500px"></iframe>
            <input type="button" class="into_group_1" id="button_11" name="button_2" value="拉黑好友" disabled="disabled">
            <input type="button" class="into_group_1" id="button_12" name="button_3" value="取消拉黑" disabled="disabled"><br>
            <input type="button" class="into_group_1" id="button_13" name="button_3" value="修改备注" disabled="disabled">
            <input type="button" class="into_group_1" id="button_14" name="button_3" value="删除好友" disabled="disabled">
            <input type="button" class="into_group_1" id="button_15" name="button_3" value="进入好友聊天室">
            <br>
            <span style="color: red" id="into_group_msg"></span>
        </c:if>

    </div>
    <%--第四个区域--%>
    <div class="login_form1">
        <label style="color: #1E90FF">进入时间：</label>
        <label style="color: #a94442">${sessionScope.loginTime}</label>
        <%--好友申请--%>
        <br><br>
        <i class="iconfont icon-haoyoushenqing" id="req_add_friend" name="req_add_friend"></i>
        <div class="div_1">查看申请</div>
        <%--消息提示小圆点, 当消息不等于0的时候才展示出来--%>
        <c:if test="${sessionScope.addFriendIFMT.size() != 0 }">
        <div class="tip" style="display: none"></div>
        </c:if>
        <br><br>
        <i class="iconfont icon-chuangjianqunliao" id="create_groups" name="create_groups"></i>
        <div class="div_1">创建群聊</div>
        <br><br>
        <i class="iconfont icon-news" id="icon-news" name="icon-news"></i>
        <div class="div_1">好友聊天</div>
        <br><br>
        <i class="iconfont icon-liaotianshi" id="icon-liaotianshi" name="icon-liaotianshi"></i>
        <div class="div_1">&nbsp;聊天室</div>
        <br><br>
        <i class="iconfont icon-qiapian" id="icon-qiapian" name="icon-qiapian"></i>
        <div class="div_1">&nbsp;好友卡片</div>
    </div>


</div>
</body>
</html>
