<%--
  Created by IntelliJ IDEA.
  User: BG.Ding
  Date: 2018/10/9
  Time: 14:46 386217
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录页面</title>

    <link rel="stylesheet" type="text/css" href="login.css"/>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>

    <script >

        function login() {

            var username = document.getElementById("username");
            var pass = document.getElementById("password");

            if (username.value == "") {

                alert("请输入工号");

            } else if (pass.value  == "") {

                alert("请输入密码");

            } else {

                document.getElementById("loading").style.display="block";
                var user = {
                    "staffNumber": username.value,
                    "password": pass.value
                };
                $.ajax({
                    type: "POST",//提交请求的方式
                    cache: true,//是否有缓存
                    url:"${pageContext.request.contextPath}/ServletLogin",//访问servlet的路径
                    dataType: "json",//没有这个，将把后台放会的json解析成字符串
                    data: user,//把内容序列化
                    async: true,//是否异步
                    error: function (request) {//请求出错
                        document.getElementById("loading").style.display="none";
                        alert("出错");
                    },
                    success: function (data) {//获得返回值
                         if(data.message==null){
                             document.getElementById("loading").style.display="none";
                            alert("帐号密码错误");
                        }else {
                            if(data.message.staffNumber=="admin"){
                                window.location.href="${pageContext.request.contextPath}/administrator/administrator.jsp";
                            }else {
                                window.location.href="${pageContext.request.contextPath}/user/uploadUser.jsp";
                            }

                        }
                    }
                });

            }
        }
    </script>
</head>

<body>

<img id="loading" src="loading.gif" style="display: none;position: fixed;margin:auto;left:0;right:0;top:0;bottom:0;z-index: 9998">
<div style="font-size: 40px;text-align: center;margin: auto;margin-top: 30px;color: grey">TTi Universal Data Upload System</div>
<div id="login_frame">

    <p id="image_logo"><img src="tti2.png"></p>

    <form method="post">

        <p><label class="label_input">工号</label><input type="text" value="" placeholder="请输入工号" id="username" class="text_field"/></p>
        <input type="text" style="display: none">

        <input type="password" style="display: none">
        <p><label class="label_input">密码</label><input type="password" value="" placeholder="请输入密码" id="password" class="text_field"/></p>

        <div id="login_control">
            <input type="button" id="btn_login" value="登录" onclick="login();"/>
        </div>
    </form>
</div>

</body>
</html>
