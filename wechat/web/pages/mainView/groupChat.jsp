<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/20
  Time: 2:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="static/lib/jquery-CustomScrollbar/css/jquery.mCustomScrollbar.min.css"/>
    <link rel="stylesheet" href="static/lib/jquery-emoji/css/jquery.emoji.css"/>
    <link type="text/css" rel="stylesheet" href="static/css/userChatView.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2551021_zy3mhzrkvh.css"/>
    <title>群聊界面</title>
    <script>
        //选择哪个群聊
        var groupName;
        var userName = "${sessionScope.loginUser.name}";
        var groupNickName
        var ws;
        //发送给哪一个群聊的;
        var toGroupName;
        var head;
        var isBand;
        var number = 0;
        var fromName;

        function ws_connect() {
            //获取连接
            if ('WebSocket' in window) {
                ws = new WebSocket("ws://localhost:8080/wechat/groupChat");
            } else if ('MozWebSocket' in window) {
                ws = new MozWebSocket("ws://localhost:8080/wechat/chat");
            } else {
                Console.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            ws.onopen = function () {
                //显示在线信息
                $("#username").html("用户：" + userName + "(" + "<span style=' color: green'>在线</span>" + ")")
            }

            ws.onclose = function () {

            }

            ws.onmessage = function (message) {
                //接受到信息，判断是否是系统信息
                var dateStr = message.data;
                //判断是否是流消息，流消息：文件、图片；文本信息：系统消息，好友发送的消息
                if (typeof (dateStr) == "string") {
                    var res = JSON.parse(dateStr);
                    //证明是文本消息，不是流消息
                    //获取消息来自谁
                    fromName = res.fromName;
                    //判断是不是系统信息
                    if (res.isSystem) {
                        //是系统消息：用户上线
                        //获取所有的用户，推送消息
                        var names = res.message;
                        var userName = "${sessionScope.loginUser.name}";
                        if(names.length != 0){
                            //当长度不为0的时候，证明有用户在线
                            for (var name of names) {
                                if (name !=userName) {
                                    //上线消息，自己上线不需要提示给自己看
                                   var str = "<span style='color: green; font-size: small; margin-left: 50%'>用户"+ name +"已加入聊天室</span>"
                                    $("#chatArea").append(str);
                                }
                            }
                        }
                    } else {
                        //不是系统消息,是群友普通消息将服务端推送的消息展示
                        var content = res.message;
                        //获取推送过来的用户头像
                        var head = res.head;
                        var fromGroup = res.groupName;
                        var userName = res.fromName;
                        var type = res.messageType;
                        //对群公告单独处理
                        if(type == "announce"){
                            //是群公告
                            $("#announces").text("");
                            $("#announces").text("公告：" + content);
                            alert("有新的群公告，请到底部查看")
                            return;
                        }

                        var str = "<div class='on_left'><div class='on_right_content'>"+userName+":"+ content + "</div></div><br><br>";
                        //判断如果选择的群和推送的群是一样的就展示
                        if(fromGroup == groupName){
                            $("#chatArea").append(str);
                        }
                        var charDate = sessionStorage.getItem(groupName);
                        if (charDate != null) {
                            str = charDate + str;
                        }
                        sessionStorage.setItem(groupName, str);
                    }
                } else {
                    //证明是流消息，就是文件和图片
                    //流消息
                    var reader = new FileReader();
                    reader.onload = function loaded(evt) {
                        var url = evt.target.result;
                        var str = "<div class='on_right'><span>"+ fromName +"</span><img style='width: 40px; height: 40px' class='' src='" + url + "'>" + "</img></div>";
                        $("#chatArea").append(str);
                        var charDate = sessionStorage.getItem(groupName);
                        if (charDate != null) {
                            str = charDate + str;
                        }
                        sessionStorage.setItem(groupName, str);
                    }
                    reader.readAsDataURL(message.data);
                }
            }


        }

        //发送消息
        function ws_senMsg() {
            $.post("groupChatServlet?method=isBan&groupName="+groupName+"&userName="+userName, function (date){
                if(date == "1"){
                    alert("你已被禁言");
                }else{
                    var content = $("#printContent").html();
                    //设置输入框为空
                    $("#printContent").html("");
                    //内容拼接
                    var str = "<div class='on_right'><div class='on_left_content'>" + content +":"+ userName + "</div></div><br><br>";
                    //JSON
                    var type = "text";
                    var json = {"toName": groupName, "message": content, "head": head, "type":type};
                    if(content != ""){
                        $("#chatArea").append(str);
                    }
                    //浏览器存储聊天消息
                    var charDate = sessionStorage.getItem(groupName);
                    if (charDate != null) {
                        str = charDate + str;
                    }
                    sessionStorage.setItem(groupName, str);
                    //发送给服务端
                    ws.send(JSON.stringify(json));
                }
            })
        }

        $(function () {


            //ajax获取用户所有的群聊
            $.ajax({
                url: "userChatServlet?method=getAllGroups",
                success: function (date) {
                    if (date == "0") {
                        alert("你还没有加入群聊")
                    } else {
                        var groups = JSON.parse(date);
                        for (var i = 0; i < groups.length; i++) {
                            var groupNickname = groups[i].groupNickname;
                            var groupName = groups[i].groupName;
                            var str = "<span id='" + groupName + "' onclick='showGroup(this)'>" + groupName + "(" + groupNickname + ")" + "</span></br>"
                            $("#allGroups").append(str);
                        }
                    }
                },
                async: false
            });

            //ajax获取用户头像
            $.ajax({
                url: "userChatServlet?method=getHead",
                success: function (date) {
                    //赋值
                    head = date;
                },
                async: false
            });

            //执行websocket——connect方法
            ws_connect();
            //点击发送消息
            $("#sendMsg").click(function () {
                ws_senMsg();
            });

            //打开历史记录
            $("#showRecord").click(function (){
                $.post("groupChatServlet?method=saveGroupName&groupName=" + groupName, function (date) {
                    if (date == "0") {
                        alert("还没有选择群聊");
                    } else {
                        window.open("pages/others/GroupChattingRecord.jsp");
                    }
                });
            })

            //退出群聊
            $("#exitGroup").click(function (){
                if(username = this.userName || ${sessionScope.loginUser.userKind.equals("群主")}){
                    alert("群主不能退出群聊，只能解散群聊");
                }
                $.post("groupChatServlet?method=exitGroup&groupName=" + groupName, function (date){
                     if(date == "1"){
                         alert("已退出")
                         $("#allGroups").html("");
                         $.ajax({
                             url: "userChatServlet?method=getAllGroups",
                             success: function (date) {
                                 if (date == "0") {
                                     alert("你还没有加入群聊")
                                 } else {
                                     var groups = JSON.parse(date);
                                     for (var i = 0; i < groups.length; i++) {
                                         var groupNickname = groups[i].groupNickname;
                                         var groupName = groups[i].groupName;
                                         var str = "<span id='" + groupName + "' onclick='showGroup(this)'>" + groupName + "(" + groupNickname + ")" + "</span></br>"
                                         $("#allGroups").append(str);
                                     }
                                 }
                             },
                             async: false
                         });
                     }
                });
            })

            //发布群公告
            $("#addAnnounce").click(function (){
                var content = $("#printContent").text();
                //设置输入框为空
                $("#printContent").html("");

                //调用方法检测，发送到其他用户的浏览器上
                //展示
                $.post("groupChatServlet?method=addAnnounce&announce="+content+"&groupName="+groupName+"&userName="+userName, function (date){
                    if(date == "0"){
                        alert("你不是群主或者管理员")
                    }else if(date == "1"){
                        alert("群公告含有敏感词，请重写")
                    }else{
                        //不含有敏感词
                        $("#announces").html("群公告：" + content);
                        //发送给服务端
                        var json = {"toName": groupName, "message": content, "head": head, "type":"announce"};
                        ws.send(JSON.stringify(json));
                        alert("已发布公告");
                    }
                })


            });

            $("#icon-biaoqingbao").click(function (){

            });

            $("#icon-Ovalx").click(function (){

            })

            $("#icon-liaotian-copy").click(function (){

            })


        })

        //点击群聊名字，显示群聊朋友列表
        function showGroup(group) {
            number = 0;
            groupName = group.id;
            //清空列表区
            $("#exitGroup").removeAttr("disabled");
            $("#broadcastList").html("");
            $("#printContent").attr("contentEditable", true);
            $("#showRecord").removeAttr("disabled");
            $("#sendMsg").removeAttr("disabled");
            //先清空聊天区
            $("#chatArea").html("");
            var charDate = sessionStorage.getItem(groupName);
            if (charDate != null) {
                //页面缓存聊天记录
                $("#chatArea").html(charDate);
            }
            //获取nickname
            $.ajax({
                url: "userChatServlet?method=findGroupNickName&groupName=" + group.id,
                success: function (date) {
                    groupNickName = date;
                    $("#chatToWho").html("正在" + groupNickName + "(" + groupName + ")" + "聊天").css("color", "green");
                }
            })
            //获取群里的用户的列表
            $.ajax({
                url: "userChatServlet?method=findAllPeopleInGroup&groupName=" + group.id,
                success: function (date) {
                    if (date == "0") {
                        alert("没有用户在该群")
                    } else {
                        var person = JSON.parse(date);
                        for (var i = 0; i < person.length; i++) {
                            var username = person[i].userName;
                            var userNickName = person[i].userNickname;
                            var str = "<div id='" + username + "' onclick='getUserName(this)'>" + username + "</div><span>"+ "(" +userNickName + ")"  +"</span></br>"
                            $("#broadcastList").append(str);
                        }
                    }
                }
            })
            //ajax获取群公告
            $.ajax({
                url:"groupChatServlet?method=findAnnounce&groupName="+groupName,
                success:function (date){
                    if(date != "0"){
                        $("#announces").html("公告"+date);
                    }
                }
            })
            //ajax获取表情包
            $.ajax({
                url:"groupChatServlet?method=getMeme&groupName="+groupName+"&userName="+userName,
                success:function (date){
                    if(date == "0"){
                        location = location;
                    }else{
                        $("#biaoqingbao").html("");
                        //拼接
                        var pictures = JSON.parse(date);
                        for(var i = 0; i < pictures.length; i++){
                            number += 1;
                            var src = pictures[i].fileLocation;
                            if((i+1) % 4 == 0){
                                var picture = "<img src='"+ src +"' width='130px' class='get' height='130px' id='"+ src +"' onclick='checkPicture(this)'></img><br>";
                                $("#biaoqingbao").append(picture);
                            }else{
                                var picture = "<img src='"+ src +"' width='130px' class='get' height='130px' id='"+ src +"' onclick='checkPicture(this)'></img>";
                                $("#biaoqingbao").append(picture);
                            }
                        }
                    }
                }
            })

        }

        function getUserName(userName) {
            var username = userName.id;
            //点击弹出选项
            var number = prompt("输入数字执行你想要执行的内容:(1. 禁言 2. 取消禁言 3. 踢出群聊 4. 邀请好友加入 5.设置为群管理员 6.取消为管理员)");
            if(number == "1"){
                if(username == this.userName){
                    alert("不能对自己操作");
                    return;
                }
                //禁言
                $.post("groupChatServlet?method=notToSay&groupName="+groupName+"&userName="+username, function (date){
                    if(date == "0"){
                        alert("你没有权限设置禁言")
                    }else{
                        //已禁言
                        alert("已禁言")
                    }
                });
            }

            if(number =="2"){
                if(username == this.userName){
                    alert("不能对自己操作");
                }
                //取消禁言
                $.post("groupChatServlet?method=toSay&groupName="+groupName+"&userName="+username, function (date){
                    if(date == "0"){
                        alert("你没有权限取消禁言")
                    }else{
                        //已取消禁言
                        alert("已取消禁言");
                    }
                });
            }

            if(number == "3"){
                if(username == this.userName){
                    alert("不能对自己操作");
                    return;
                }
                //踢出群聊
                $.post("groupChatServlet?method=kickOutGroup&userName="+username+"&groupName="+groupName, function (date){
                    if(date == "0"){
                        alert("没找到该用户")
                    }
                    if(date == "1"){
                        alert("已踢出群聊")
                    }
                    if(date == "2"){
                        alert("你没有权力把用户踢出群")
                    }
                });
            }

            if(number == "4"){
                var friendAccount = prompt("请输入要添加的好友的账号");
                $.post("groupChatServlet?method=inviteFriendAddGroup&friendAccount="+friendAccount+"&groupName="+groupName, function (date){
                    if(date == "-1"){
                        alert("找不到群聊");
                    }
                    if(date == "0"){
                        alert("找不到该用户");
                    }
                    if(date == "1"){
                        alert("你和该用户不是好友");
                    }
                    if(date == "2"){
                        alert("该用户已经进群");
                    }
                    if(date == "3"){
                        alert("已邀请进入群聊");
                    }
                    if(date == "4"){
                        alert("已邀请，等待群主或管理员同意");
                    }
                })
            }

            if(number == "5"){
                if(username == this.userName){
                    alert("不能对自己操作");
                    return;
                }
                //设置为群管理员
                $.post("groupChatServlet?method=setToGroupManager&userName="+username+"&groupName="+groupName, function (date){
                    if(date == "0"){
                        alert("你不是群主，没有权限设置管理员")
                    }
                    if(date == "1"){
                        alert("已设置为管理员")
                    }
                });
            }

            if(number == "6"){
                if(username == this.userName){
                    alert("不能对自己操作");
                    return;
                }
                $.post("groupChatServlet?method=unsetToGroupManager&userName="+username+"&groupName="+groupName, function (date){
                    if(date == "0"){
                        alert("你不是群主，没有权限取消设置管理员")
                    }
                    if(date == "1"){
                        alert("已取消设置为管理员")
                    }
                });
            }

        }

        function sendFile(f){
            $.post("groupChatServlet?method=isBan&groupName="+groupName+"&userName="+userName, function (date){
                if(date == "1"){
                    alert("你已被禁言");
                }else{

                    var file = f.files[0];//获取file组件中的文件
                    var file1 = f.files[0];//获取file组件中的文件
                    //判断字节是多少，大于10m的不能上传
                    var size = file1.size;
                    if (size > 1024 * 1024 * 10) {
                        //大于10m的文件不能上传
                        alert("请选择小于10m的文件")
                        return;
                    }
                    //文件名
                    var fileName = file.name;
                    var regex1 = /.+(.JPEG|.jpeg|.JPG|.jpg|.png)$/;
                    if (file != false && regex1.test(fileName)) {
                        //是图片
                        //解码
                        var fr = new FileReader();
                        var fr1 = new FileReader();
                        var formData = new FormData();
                        formData.append('file', f.files[0]);  //添加图片信息的参数
                        //使用ajax上传到数据库
                        $.ajax({
                            url: "fileServlet?method=groupSendPicture&groupName=" + groupName,
                            type: "post",
                            dataType: "json",
                            cache: false,
                            data: formData,
                            processData: false,// 不处理数据
                            contentType: false, // 不设置内容类型
                            success: function (data) {
                            },
                            async: true
                        })
                        //先不读为Buffer
                        fr.readAsDataURL(file);
                        //上传到chat界面
                        fr.onload = function (evt) {
                            //拼接
                            var str = "<div class='on_right' style='font-size: 30px'><img style='width: 40px; height: 40px' class='on_right_content1' src=" + evt.target.result + "></img>"+ ":" + userName +"</div><br><br>";
                            $("#chatArea").append(str);
                            var charDate = sessionStorage.getItem(groupName);
                            if (charDate != null) {
                                str = charDate + str;
                            }
                            sessionStorage.setItem(groupName, str);
                        }

                        //读为流，发送给后台处理
                        fr1.readAsArrayBuffer(file1);
                        fr1.onload = function (evt) {
                            var blob = evt.target.result;
                            ws.send(blob);
                        }
                    }else{
                        //文件
                        //不是图片
                        //解码
                        var fr = new FileReader();
                        var fr1 = new FileReader();
                        //先不读为Buffer
                        fr.readAsDataURL(file);
                        //传送到后台获取路径
                        var formData = new FormData();
                        formData.append('file', f.files[0]);  //添加图片信息的参数
                        $.ajax({
                            url: "fileServlet?method=userChatWithGroupAddFile&groupName=" + groupName,
                            type: "post",
                            dataType: "json",
                            cache: false,
                            data: formData,
                            processData: false,// 不处理数据
                            contentType: false, // 不设置内容类型
                            success: function (data) {
                                //获取文件路径
                                fileDate = data;
                                var str = "<div class='on_right2' style='font-size: 30px'><a style='font-size: small' href='fileServlet?method=downloadGroupFile&filename=" + fileDate + "' class='files'>" + fileName + "</a>"+ ":" + userName +"</br>";
                                var content = "<a  class='files' href='fileServlet?method=downloadGroupFile&filename=" + fileDate + "'>" + fileName + "</a>";
                                var json = {"toName": groupName, "message": content};
                                //拼接
                                $("#chatArea").append(str);
                                //缓存
                                var charDate = sessionStorage.getItem(groupName);
                                if (charDate != null) {
                                    str = charDate + str;
                                }
                                sessionStorage.setItem(groupName, str);
                                //发送div标签
                                ws.send(JSON.stringify(json));
                            },
                            async: true
                        })
                    }
                }
            });
        }

        function select(color){
            var color = $("#select").val();
            document.getElementById("chatArea").style.backgroundColor = color;
        }

        function checkPicture(e){
            $.post("groupChatServlet?method=isBan&groupName="+groupName+"&userName="+userName, function (date){
                if(date == "1"){
                    alert("你已被禁言")
                }else{
                    //点击表情包,获取地址
                    var content = e.id;
                    //展示在聊天界面上
                    var str = "<div class='on_right' style='font-size: 30px'><img style='width: 40px; height: 40px' class='on_right_content3' src="+content+"></img>"+ ":" + userName +"</div><br><br>";
                    var contentd = "<img style='width: 40px; height: 40px' class='on_right_content3' src="+content+"></img>";
                    $("#chatArea").append(str);
                    //浏览器存储聊天消息
                    var charDate = sessionStorage.getItem(groupName);
                    if (charDate != null) {
                        str = charDate + str;
                    }
                    sessionStorage.setItem(groupName, str);
                    //发送
                    var json = {"toName": groupName, "message": contentd, "head": head, "type":"picture"};
                    ws.send(JSON.stringify(json));
                }
            })
        }

        function addMeme(f){
            var file = f.files[0];//获取file组件中的文件
            var fr = new FileReader();
            fr.readAsDataURL(file);
            var fileName = file.name;
            var regex1 = /.+(.JPEG|.jpeg|.JPG|.jpg|.png|.xbm|.tif|.pjp|.svgz|.ico|.tiff|.webp|.bmp|.pjpeg|.avif|)$/;
            //传送到后台获取路径
            if(regex1.test(fileName)){
                //是图片
                var formData = new FormData();
                formData.append('file', f.files[0]);  //添加图片信息的参数
                //使用ajax产生并返回路径
                $.ajax({
                    //文件上传
                    url: "fileServlet?method=groupChatAddMeme&groupName=" + groupName+"&userName="+userName,
                    type: "post",
                    dataType: "json",
                    cache: false,
                    data: formData,
                    processData: false,// 不处理数据
                    contentType: false, // 不设置内容类型
                    success: function (data) {
                        //获取文件路径
                        fileDate = data;
                        number += 1;
                        if(number % 4 == 0){
                            var picture = "<img src='"+ fileDate +"' width='130px' class='get' height='130px' id='"+ fileDate +"' onclick='checkPicture(this)'></img><br>";
                            $("#biaoqingbao").append(picture);
                        }else{
                            var picture = "<img src='"+ fileDate +"' width='130px' class='get' height='130px' id='"+ fileDate +"' onclick='checkPicture(this)'></img>";
                            $("#biaoqingbao").append(picture);
                        }
                    }
                });
            }else{
                //不是图片
                alert("不是图片")
            }
        }
    </script>
</head>
<body>
<div class="content_1">
    <div class="managerHead_1">
        <span id="username"></span><br>
        <span style="color: green" id="chatToWho"></span>
    </div>
    <div class="chatArea" id="chatArea" style="overflow: auto;">
        <%--        <div class="on_left"><img class="heading" id="heading" src=""><div class="on_left_content"></div></div>--%>
        <%--        <div class="on_right"><img class="heading1" id="heading1" src=""><div class="on_right_content"></div></div>--%>
    </div>
    <div class="middle"><br>
        <button id="showRecord" name="showRecord" class="showRecord" disabled>查看历史记录</button>
        <button id="exitGroup" name="exitGroup" class="exitGroup" disabled>退出群聊</button>
        <button id="addAnnounce" name="addAnnounce" class="addAnnounce">发布公告</button>
        <select name="select" class="select" id="select" onchange="select(this)">
            <option value="wheat" selected="selected" class="color">wheat</option>
            <option value="red">红色</option>
            <option value="yellow">黄色</option>
            <option value="green">绿色</option>
            <option value="blue">蓝色</option>
            <option value="pink">粉红色</option>
            <option value="brown">棕色</option>
            <option value="purple">紫色</option>
            <option value="black">黑色</option>
            <option value="white">白色</option>
        </select>
        <i class="iconfont  icon-liaotian-copy" id="icon-liaotian-copy" name="icon-liaotian-copy" title="聊天"></i>
        <i class="iconfont  icon-Ovalx" id="icon-Ovalx" name="icon-Ovalx" title=""></i>
        <i class="iconfont  icon-biaoqingbao" id="icon-biaoqingbao" name="icon-biaoqingbao" title="公告"></i>
        <a class="iconfont  icon-wenjian" id="icon-wenjian" name="icon-wenjian" title="文件"><label
                for="file"></label><input type="file" id="file" name="file" style="opacity: 0" class="file"
                                          onchange="sendFile(this)"></a>
        <a class="iconfont  icon-tianjia" id="icon-tianjia" name="icon-tianjia" title="添加"><label
                for="file"></label><input type="file" id="file1" name="file1" style="opacity: 0" class="file1"
                                          onchange="addMeme(this)"></a>
        <button id="sendMsg" name="sendMsg" class="sendMsg" disabled="disabled">发送消息</button>
    </div>
    <div class="buttom">
        <%--富文本框--%>
        <div contenteditable="false" disabled class="printContent"  id="printContent" placeholder="在此输入要发送的消息"></div>
    </div>
    <%--展示用户区域--%>
    <div class="right">
        <div class="friendList" style="border: black solid 1px; color: #00dbde">群聊列表</div>
        <ul class="allFriend" style="border: black solid 1px" id="allGroups">

        </ul>
        <div class="radio" style="border: black solid 1px; color: #00dbde">群聊成员</div>
        <ul class="allMessages" style="border: black solid 1px" id="broadcastList">
        </ul>
    </div>
    <div class="announce" contenteditable="false" id="announces" name="announces"></div>
    <div class="biaoqingbao" contenteditable="false" id="biaoqingbao" name="biaoqingbao"></div>
</div>
<%--引入表情js--%>
<script src="static/lib/jquery-CustomScrollbar/script/jquery.mCustomScrollbar.min.js"></script>
<script src="static/lib/jquery-emoji/js/jquery.emoji.min.js"></script>
<script src="static/script/index.js"></script>
</body>
</html>
