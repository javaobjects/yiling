<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>
<%@ include file="pathConfig.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE"/>
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <title>商家ERP对接平台</title>
    <link rel="icon" href="<%=path%>/static/images/64_64.bmp" type="image/x-icon"/>
    <link rel="shortcut icon" href="<%=path%>/static/images/favicon.ico" type="image/x-icon"/>
    <link href="<%=path%>/static/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="<%=path%>/static/css/common.css" rel="stylesheet"/>
</head>
<body>
<div class="topper">
    <div><span class="welcome">您好，欢迎您来到以岭商城信息网</span>|&nbsp;&nbsp;<a href="https://pop.59yi.com/"
                                                                     target="_blank">我的以岭商城</a>&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|<span
            class="tel"></span>
    </div>
</div>
<div class="login_header">
    <div class="login_body padding-20">
        <div class="fl" href="#">
            <a href="#" class="margin-r-20"><img src="<%=path%>/static/images/logo.png"/></a>
            <img src="<%=path%>/static/images/l2.jpg"/>
        </div>
    </div>
</div>
<div class="login_content">
    <div class="login_body">
        <div class="login fr margin-t-58" id="login">
            <h2>
                <div class="margin-b-10">商家登录</div>
            </h2>
            <div class="login_form" al="one">
                <!-- <p class="red">这是报错消息</p> -->
                <ul>
                    <li><span><img src="<%=path%>/static/images/user.jpg"/></span><input placeholder="请输入用户名"
                                                                                         type="text" name="account"
                                                                                         id="account"/></li>
                    <li><span><img src="<%=path%>/static/images/password.jpg"/></span><input placeholder="请输入密码"
                                                                                             type="password"
                                                                                             name="password"
                                                                                             id="password"/></li>
                </ul>
                <p class="login_btn">
                    <button type="submit" onclick="login();">登 录</button>
                </p>
            </div>
        </div>
    </div>
</div>
<div class="login_footer">
    <div class="login_body">
        Copyright 2021 www.59yi.com 冀ICP备15023757号-1 | 公安备案号 : 13019902000067
    </div>
</div>
</body>
<script src="<%=path%>/static/js/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="<%=path%>/static/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/static/js/b_common.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/rk/login.js"></script>
</html>
<script>
    $("#login h2 span").on("click", function () {
        $(this).addClass("cur").siblings().removeClass("cur");
        var al = $(this).attr("al");
        $(".login_body .login_form").each(function () {
            $(this).hide();
            if ($(this).attr("al") == al) {
                $(this).show();
            }
        });
    });

    document.onkeydown = function (event) {
        e = event ? event : (window.event ? window.event : null);
        if (e.keyCode == 13) {
            login();
        }
    }

</script>