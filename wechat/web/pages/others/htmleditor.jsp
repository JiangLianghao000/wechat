<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/9
  Time: 1:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>富文本编辑器</title>
    <link type="text/css" rel="stylesheet" href="static/css/htmleditor.css"/>
    <style>

        body {
            margin: 0px;
            padding: 0px;
            display: flex;
            overflow: hidden;
            width: 100%;
            height: 100vh;
        }

        .rachEdit {
            background: #aabbcc;
            width: 50%;
            height: 100vh;
            border: 1px solid #ccc;
            margin-left: -100%;
            margin-top: 5.6%;
        }

        .editDiv {
            background: #cba;
            width: 50%;
            height: 100vh;
            border: 1px solid #ccc;

            margin-top: 5.6%;
        }

        .btn {
            background: wheat;
            width: 100%;
            height: 20vh;
            border: 1px solid #ccc;
        }

        .btns {
            border: 0; /* 取消按钮的边界 */
            font-size: 18px; /* 修改按钮文字的大小 */
            color: white; /* 修改按钮上文字的颜色 */
            border-radius: 25px; /* 将按钮的左右边框设置为圆弧 */
            background-image: linear-gradient(to right, #aabbcc 0%, #ccbbaa 100%); /* 为按钮增加渐变颜色 */
            cursor: pointer;
            margin-left: 2%;
            margin-top: 6px;
        }

        .btns1 {
            border: 0; /* 取消按钮的边界 */
            font-size: 18px; /* 修改按钮文字的大小 */
            color: white; /* 修改按钮上文字的颜色 */
            border-radius: 25px; /* 将按钮的左右边框设置为圆弧 */
            background-image: linear-gradient(to right, #00dbde 0%, #fc00ff 100%); /* 为按钮增加渐变颜色 */
            cursor: pointer;
            margin-left: 2%;
            margin-top: 6px;
        }

        .btns2{
            border: 0; /* 取消按钮的边界 */
            font-size: 18px; /* 修改按钮文字的大小 */
            color: white; /* 修改按钮上文字的颜色 */
            border-radius: 25px; /* 将按钮的左右边框设置为圆弧 */
            background-image: linear-gradient(to right, #00dbde 0%, #fc00ff 100%); /* 为按钮增加渐变颜色 */
            cursor: pointer;
            margin-left: 70%;
            margin-top: -25px;
            position: absolute;
        }


    </style>

    <script>

        function show(f) {
            var reader = new FileReader();//创建文件读取对象
            var files = f.files[0];//获取file组件中的文件
            reader.readAsDataURL(files);//文件读取装换为base64类型
            reader.onloadend = function (e) {
                //加载完毕之后获取结果赋值给img
                var content = $("#rachEdit").html();
                alert(content)
            }
        };

        //同步更新
        function showValue(){
            var content = $("#rachEdit").html();
            $("#editDiv").html(content);
        }

        //所有的按键事件
        $(function () {
            //对所有操作
            //1. 超链接
            $("#btn1").click(function () {
                //添加超链接
                var lineVal = prompt("请输入完整链接地址(如：http://www.bilibili.com)：", "");
                if (lineVal != null && lineVal != '') {
                    var patt = /[http|https]+:\/\/[^\s]*/i;
                    if (patt.test(lineVal)) {
                        document.execCommand("createlink", false, lineVal);
                        showValue();
                    } else {
                        alert("链接地址输入格式错误，请重新输入！");
                    }
                }
            });

            //2. 复制
            $("#btn2").click(function () {
                document.execCommand('Copy');
            });
            //3. 粘贴
            $("#btn3").click(function () {
                alert("请按ctrl + v 粘贴");
            });
            //4. 剪切
            $("#btn4").click(function () {
                document.execCommand('Cut');
                showValue();
            });
            //5. 撤消
            $("#btn5").click(function () {
                document.execCommand('Undo');
                showValue();
            });

            //6. 删除
            $("#btn6").click(function () {
                document.execCommand('Delete');
                showValue();
            });

            //7. 黑体
            $("#btn7").click(function () {
                document.execCommand('Bold');
                showValue();
            });

            //8. 斜体
            $("#btn8").click(function () {
                document.execCommand('Italic');
                showValue();
            });

            //9. 下划线
            $("#btn9").click(function () {
                document.execCommand('Underline');
                showValue();
            });

            //10. 字体大小
            $("#btn10").click(function () {
                var size = prompt("请输入字体大小(1 - 7)：", "");
                document.execCommand('FontSize', false, size);
                showValue();
            });

            //11.字体类型
            $("#btn11").click(function () {
                var fontName = prompt("请输入想要用的字体名字(例如：华文琥珀)：tip:你可以百度搜索字体名字", "");
                document.execCommand('fontName', false, fontName);
                showValue();
            });

            //12. 字体颜色
            $("#btn12").click(function () {
                var fontName = prompt("请输入想要用的颜色(例如：red)：\ntip:你可以百度搜索颜色代码", "");
                document.execCommand('foreColor', false, fontName)
                showValue();
            });

            //13. 删除线
            $("#btn13").click(function () {
                document.execCommand('StrikeThrough', false, null);
                showValue();
            });

            //14. 左对齐
            $("#btn14").click(function () {
                document.execCommand('justifyLeft', false, null);
                showValue();
            });

            //15. 右对齐
            $("#btn15").click(function () {
                document.execCommand('justifyRight', false, null);
                showValue();
            });

            //16. 文本对齐
            $("#btn16").click(function () {
                document.execCommand('justifyFull', false, null);
                showValue();
            });

            //17. 文字居中
            $("#btn17").click(function () {
                document.execCommand('justifyCenter', false, null);
                showValue();
            });

            //18.去除链接
            $("#btn18").click(function () {
                document.execCommand('unlink', false, null);
                showValue();
            });


            //20. 完成编辑
            $("#btn20").click(function () {
                var content = $("#editDiv").html();
                $.post("circleServlet?method=saveInCicle&content="+content, function (date){
                        if(date == "1"){
                            alert("已发布");
                        }else if(date == "2"){
                            alert("朋友圈有敏感信息，请重新检测");
                        }else{
                            alert("程序异常！！！")
                        }
                });
            });
        })

        //21选择图片
        function show(f) {
            //文件格式
            //formData.append('file', $('#uploadForm')[0].files[0]);  //添加图片信息的参数
            var reader = new FileReader();//创建文件读取对象
            var files = f.files[0];//获取file组件中的文件
            reader.readAsDataURL(files);//文件读取装换为base64类型
            reader.onloadend = function (e) {
                var formData = new FormData();
                formData.append('file',  $('#picurl')[0].files[0])
                $.ajax({
                    type: 'post',
                    url: "fileServlet?method=addCirclePicture",
                    data: formData,
                    cache: false,
                    processData: false,
                    contentType: false,
                }).success(function (date){
                    //成功就返回地址
                    var content = $("#rachEdit").html();
                    content = content + "<img src=\""+date+"\" width=100 height=100>"
                    $("#rachEdit").html(content);
                    $("#editDiv").html(content);
                }).error(function (){
                    alert("上传失败")
                });
            };
        };

    </script>
    <style>

    </style>
</head>
<body>

<!-- 编辑器 -->
<div class="btn">
    <button type="button" id="btn2" class="btns">复制</button>
    <button type="button" id="btn3" class="btns">粘贴</button>
    <button type="button" id="btn4" class="btns">剪切</button>
    <button type="button" id="btn5" class="btns">撤消</button>
    <button type="button" id="btn6" class="btns">删除选中</button>
    <button type="button" id="btn7" class="btns">黑体</button>
    <button type="button" id="btn8" class="btns">斜体</button>
    <button type="button" id="btn9" class="btns">下划线</button>
    <button type="button" id="btn13" class="btns">删除线</button>
    <button type="button" id="btn14" class="btns">左对齐</button>
    <button type="button" id="btn15" class="btns">右对齐</button>
    <button type="button" id="btn16" class="btns">文本对齐</button>
    <button type="button" id="btn17" class="btns">文字居中</button>
    <br>
    <button type="button" id="btn1" class="btns">超链接</button>
    <button type="button" id="btn18" class="btns">去除链接</button>
    <button type="button" id="btn10" class="btns">字体大小</button>
    <button type="button" id="btn11" class="btns">字体类型</button>
    <button type="button" id="btn12" class="btns">字体颜色</button>
    <button type="button" id="btn20" class="btns1">完成编译</button>
    <input type="file" id="picurl" class="btns1" accept="image/png, image/jpeg, image/gif, image/jpg" onchange="show(this)">


</div>
<div name="richEdit" contenteditable="true" class="rachEdit" id="rachEdit" onkeyup="showValue()"></div>
<div class="editDiv" contenteditable="false" id="editDiv" name="editDiv"></div>
</body>
</html>
