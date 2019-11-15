<%--
  Created by IntelliJ IDEA.
  User: BG.Ding
  Date: 2018/9/21
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理员界面</title>
</head>

<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
<script>
    $(function() {
        $( "#menu" ).menu();
    });
</script>
<style>
    .ui-menu { width: 150px; }
</style>
<%--<style>--%>
    <%--.one{--%>
        <%--width: 700px;--%>
        <%--height: 200px;--%>
        <%--margin: 200px auto;--%>
    <%--}--%>
    <%--.one a{--%>
        <%--width: 140px;--%>
        <%--height: 30px;--%>
        <%--text-decoration: none;--%>
        <%--display: block;--%>
        <%--text-align: center;--%>
        <%--line-height: 30px;--%>

    <%--}--%>
    <%--.one ul  li{--%>
        <%--float: left;//设置浮动，让标签并排显示--%>
    <%--list-style: none;//设置li的的样式--%>
    <%--}--%>
    <%--a:link {--%>
        <%--background: #FFFFFF;--%>
        <%--color: #000000;--%>
        <%--border:1px solid  #999999 ;//给超链接a加边框--%>

    <%--}--%>
<%--</style>--%>



<body background="../bakground3.jpg" style="background-size: cover;" >
<span style="float: left;font-size: 40px;color: white;font-family: Calibri;font-weight: bolder">TTi Universal Data Upload System</span>
<div style="text-align: right;font-size: 15px;color: white"> 管理员：<%=session.getAttribute("name")%><span style="visibility: hidden">111</span></div>
<ul id="menu" style="margin-left: -320px;margin-top: 150px;font-size: 25px;float: left;width: 200px;">
    <li><a href="connect.jsp">数据库</a></li>
    <li>
        <a href="#">表</a>
        <ul>
            <li><a href="createTable.jsp">新建上传</a></li>
            <li><a href="editTable.jsp">修改上传</a></li>
        </ul>
    </li>

    <li>
        <a href="#">用户</a>
        <ul>
            <li><a href="createUser.jsp">新增用户</a></li>
            <li><a href="editUser.jsp">编辑用户</a></li>
        </ul>
    </li>
    <li><a href="editPassword.jsp">修改密码</a></li>
    <li><a href="../loginout.jsp">退出登录</a></li>
</ul>

<img src="../uploadExcel.png" style="margin-top: 130px;margin-left:100px;width: 500px;float: left">
</body>

</html>
