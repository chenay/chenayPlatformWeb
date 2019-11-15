<%--
  Created by IntelliJ IDEA.
  User: BG.Ding
  Date: 2018/11/11
  Time: 16:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改密码</title>
</head>
<%--<script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>--%>

<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">

<script>

    $(function() {
        $( " a, button" ).button();
        $( "a, button" ).css("font-size","14px");
        $( "input,select").css("border-radius","5px");
        $( "input,select").css("height","30px");
        $( "input,select").css("width","200px");
        $( "input,select").css("text-align","center");
    });

    $(document).ready(function(){
        $("#updateUser").click(function(){
            var oldPassword=document.getElementById("oldPassword").value;
            var newPassword=document.getElementById("newPassword").value;
            var newPassword2=document.getElementById("newPassword2").value;
            if(!confirm("确认要修改密码吗")){
                return;
            }

            if(oldPassword==""||newPassword==""||newPassword2==""){
                alert("请填写必填项");
                return;
            }
            if(oldPassword!="<%=session.getAttribute("password")%>"){
                alert("原密码错误");
                return;
            }
            if(newPassword.indexOf(" ")!=-1){
                alert("新密码不允许含有空格");
                return;
            }
            if(newPassword!=newPassword2){
                alert("新密码不一致");
                return;
            }



            document.getElementById("loading").style.display="block";



            var user = {
                "password":newPassword,
                "action":"updateUser"
            };


            $.ajax({
                type: "POST",//提交请求的方式
                cache: true,//是否有缓存
                url:"${pageContext.request.contextPath}/ServletUser",//访问servlet的路径
                dataType: "json",//没有这个，将把后台放会的json解析成字符串
                data: user,//把内容序列化
                async: true,//是否异步
                error: function (request) {//请求出错
                    document.getElementById("loading").style.display="none";

                    alert("出错");
                },
                success: function (data) {
                    document.getElementById("loading").style.display="none";

                    alert(data.message);
                    window.location.href="${pageContext.request.contextPath}/user/editPassword.jsp";
                }
            });

        });
    });


</script>
<body background="../bakground3.jpg" style="background-size: cover;" >
<span style="float: left;font-size: 40px;color: white;font-family: Calibri;font-weight: bolder">TTi Universal Data Upload System</span>
<img id="loading" src="../loading.gif" style="display: none;position: fixed;margin:auto;left:0;right:0;top:0;bottom:0;z-index: 9999">

<%--<%=session.getAttribute("password")%>--%>
<div style="text-align: right;font-size: 20px;"><button type="button" id="updateUser">提交</button><a href="administrator.jsp">返回</a></div>
<div style="margin:auto;width: 300px;margin-top: 150px;">
    <input type="password" style="display: none" placeholder="原密码">

    原密码：<input type="password" id="oldPassword" placeholder="原密码">
    <br><br>
    新密码：<input type="password" id="newPassword" placeholder="新密码">
    <br><br>
    请确认：<input type="password" id="newPassword2"placeholder="确认新密码"><br>

</div>


</body>
</html>
