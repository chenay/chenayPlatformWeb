<%--
  Created by IntelliJ IDEA.
  User: BG.Ding
  Date: 2018/11/23
  Time: 10:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>

<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
<script type="text/javascript">

        function CountDown() {
            var i=Number(document.getElementById("i").value);
            i++;
            document.getElementById("i").value=i;
            console.log(i);
        }
        // setInterval("CountDown()",1000);
        function open1() {
            var win =window.open("test2.jsp","win");



        }


</script>
<body>
<input type="text" id="i" value="0" style="display: none">

<button type="button" onclick="open1()" >123</button>
</body>
</html>
