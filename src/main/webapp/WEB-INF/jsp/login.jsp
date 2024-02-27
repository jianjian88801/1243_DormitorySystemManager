<%@ page contentType="text/html; charset=UTF-8" language="java"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/lib/layui/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <meta charset="utf-8">
    <title>欢迎登录</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
</head>
<body>

<div class="layui-fluid" id="login_box">

    <h2 id="title">学生宿舍管理系统</h2>
    <form class="layui-form" action="" method="post" >
        <div class="layui-form-item">
            <i class="layui-icon layui-icon-username" id="userIcon"></i>
            <input class="layui-input" type="text" name="a_username" required placeholder="请输入用户名" autocomplete="off" >
        </div>
        <div class="layui-form-item">
            <i class="layui-icon layui-icon-password" id="pwdIcon"></i>
            <input class="layui-input" type="password" name="a_password" required placeholder="请输入密码" autocomplete="off"  >
        </div>
        <div class="layui-form-item">
            <i class="layui-icon layui-icon-vercode"></i>
            <input type="text" name="checkCode" placeholder="请输入验证码（不区分大小写）" class="layui-input" maxlength="4">
            <canvas id="canvas" width="100" height="37" onclick="draw()"></canvas>
        </div>
        <div class="layui-form-item">
            <!-- onclick = false的作用是取消自动提交-->
            <button class="layui-btn layui-col-md6" lay-submit="" lay-filter="submit_btn" type="button" id="login" onsubmit="true">登录</button>
        </div>
    </form>
</div>
<script src="${pageContext.request.contextPath}/lib/layui/layui.js"></script>
<script src="${pageContext.request.contextPath}/js/bgeffect.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript">
    var isSumbit = false;
    var msg = "${msg}";
    //定义全局验证码数组
    var show_num = [];
    window.onload = function(){
        draw()
    }
    layui.use(['form', 'layedit', 'laydate'], function() {
        var form = layui.form
            , layer = layui.layer
            , layedit = layui.layedit
            , laydate = layui.laydate
        //调用函数生成验证码图片
        if (msg != null && msg != ""){
            layer.msg(msg);
        }
        //监听提交
        //dataType是需要发送的数据类型
        //contentType是接受的数据类型
        form.on('submit(submit_btn)', function (data) {
            if (data.field.a_username == null || data.field.a_username.trim() == "") {
                layer.msg("账号不能为空", {time: 600});
                return;
            }

            if (data.field.a_password == null || data.field.a_password.trim() == "") {
                layer.msg("密码不能为空", {time: 600});
                return;
            }

            var checkCodeStr = "" + show_num[0] + show_num[1] + show_num[2] + show_num[3];
            if (data.field.checkCode == null || data.field.checkCode == "") {
                layer.msg("验证码不能为空", {time: 600});
                return;
            } else {
                if (data.field.checkCode.toLowerCase() != checkCodeStr) {
                    layer.msg("验证码错误!", {time: 800});
                    return;
                }
            }
            var contentPath = "${pageContext.request.contextPath}";
            //根据用户输入的数据类型，来发起ajax请求
            $.ajax({
                    async:true,
                    url: contentPath+"/login",
                    data: JSON.stringify({
                        "a_username": data.field.a_username,
                        "a_password": data.field.a_password
                    }),
                    type: "post",
                    contentType : "application/json",
                    success: function (data) {
                        console.log(data);
                        if (data == "success") {
                            layer.msg("登录成功", {time: 600});
                            window.location.href=contentPath+"/homepage";  //跳转页面
                        } else {
                            layer.msg("账号或密码错误", {time: 600});
                        }

                    },
                    error: function (jqXHR, textStatus, errorThorown) {
                        console.log("jqXHR:" + jqXHR);
                        console.log("textStatus:" + textStatus);
                        console.log("errorThorown:" + errorThorown);
                        layer.msg("服务器出错，请检查",{time: 600});

                    }
                });
            return false;
        });
    });
    function draw() {
        var canvas_width = $('#canvas').width();
        var canvas_height = $('#canvas').height();
        var canvas = document.getElementById("canvas");//获取到canvas的对象，演员
        var context = canvas.getContext("2d");//获取到canvas画图的环境，演员表演的舞台
        canvas.width = canvas_width;
        canvas.height = canvas_height;
        var sCode = "A,B,C,E,F,G,H,J,K,L,M,N,P,Q,R,S,T,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0";
        var aCode = sCode.split(",");
        var aLength = aCode.length;//获取到数组的长度

        for (var i = 0; i <= 3; i++) {
            var j = Math.floor(Math.random() * aLength);//获取到随机的索引值
            var deg = Math.random() * 30 * Math.PI / 180;//产生0~30之间的随机弧度
            var txt = aCode[j];//得到随机的一个内容
            show_num[i] = txt.toLowerCase();
            var x = 10 + i * 20;//文字在canvas上的x坐标
            var y = 20 + Math.random() * 8;//文字在canvas上的y坐标
            context.font = "bold 23px 微软雅黑";
            context.translate(x, y);
            context.rotate(deg);

            context.fillStyle = randomColor();
            context.fillText(txt, 0, 0);

            context.rotate(-deg);
            context.translate(-x, -y);
        }
        for (var i = 0; i <= 5; i++) { //验证码上显示线条
            context.strokeStyle = randomColor();
            context.beginPath();
            context.moveTo(Math.random() * canvas_width, Math.random() * canvas_height);
            context.lineTo(Math.random() * canvas_width, Math.random() * canvas_height);
            context.stroke();
        }
        for (var i = 0; i <= 30; i++) { //验证码上显示小点
            context.strokeStyle = randomColor();
            context.beginPath();
            var x = Math.random() * canvas_width;
            var y = Math.random() * canvas_height;
            context.moveTo(x, y);
            context.lineTo(x + 1, y + 1);
            context.stroke();
        }
    }
    function randomColor() {//得到随机的颜色值
        var r = Math.floor(Math.random() * 256);
        var g = Math.floor(Math.random() * 256);
        var b = Math.floor(Math.random() * 256);
        return "rgb(" + r + "," + g + "," + b + ")";
    }
</script>
</body>
</html>