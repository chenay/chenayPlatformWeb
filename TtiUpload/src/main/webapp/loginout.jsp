<%--
  Created by IntelliJ IDEA.
  User: BG.Ding
  Date: 2018/10/11
  Time: 10:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>退出</title>
</head>
<body>
<%

    session.removeAttribute("name");
    session.removeAttribute("staffNumber");
    session.removeAttribute("role");
    session.removeAttribute("password");
    response.sendRedirect("login.jsp");
%>
</body>
</html>
