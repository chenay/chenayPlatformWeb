<%--
  Created by IntelliJ IDEA.
  User: BG.Ding
  Date: 2018/9/21
  Time: 10:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>开启/关闭连接</title>
</head>
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">

<style>
    body { font-size: 62.5%; }
    label, input { display:block; }
    input.text { margin-bottom:12px; width:95%; padding: .4em; }
    fieldset { padding:0; border:0; margin-top:25px; }
    h1 { font-size: 1.2em; margin: .6em 0; }
    .ui-dialog .ui-state-error { padding: .3em; }
    .validateTips { border: 1px solid transparent; padding: 0.3em; }
</style>

<script>

    $(function() {
        $( "a, button" ).button();
        $( "a, button" ).css("font-size","14px");

    });
    function f(btn){
        if(!confirm("确认删除吗")){
            return;
        }
        document.getElementById("loading").style.display="block";

        var td=btn.parentElement;
        var tr=td.parentElement;
        var td1=tr.cells[2];
        var table = {
            "alias":td1.innerHTML,
            "action":"deleteDatabase"
        };

        $.ajax({
            type:"POST",//提交请求的方式
            cache:true,//是否有缓存
            url:"${pageContext.request.contextPath}/Connect",//访问servlet的路径
            dataType:"json",//没有这个，将把后台放会的json解析成字符串
            data:table,//把内容序列化
            async:true,//是否异步
            error:function(request) {//请求出错
                document.getElementById("loading").style.display="none";

                alert("出错");

            },
            success:function(data) {//获得返回值
                document.getElementById("loading").style.display="none";

                alert(data.message);

                window.location.href="${pageContext.request.contextPath}/administrator/connect.jsp";
            }
        });

    }

    function updateDatabasePassword(btn){
        var password=prompt("请输入新密码");
        if(password==null){
            return;
        }
        if(!confirm("确认修改吗")){
            return;
        }
        if(password=="") {
            aler("密码不能为空");
            return;
        }

        document.getElementById("loading").style.display="block";

        var td=btn.parentElement;
        var tr=td.parentElement;
        var url=tr.cells[0].innerHTML;
        var userName=tr.cells[1].innerHTML;
        var alias=tr.cells[2].innerHTML;
        var table = {
            "alias":alias,
            "action":"updateDatabasePassword",
            "userName":userName,
            "url":url,
            "password":password
        };

        $.ajax({
            type:"POST",//提交请求的方式
            cache:true,//是否有缓存
            url:"${pageContext.request.contextPath}/Connect",//访问servlet的路径
            dataType:"json",//没有这个，将把后台放会的json解析成字符串
            data:table,//把内容序列化
            async:true,//是否异步
            error:function(request) {//请求出错
                document.getElementById("loading").style.display="none";
                alert("出错");
            },
            success:function(data) {//获得返回值
                document.getElementById("loading").style.display="none";
                alert(data.message);
            }
        });

    }

    function updateDatabase(btn){
        if(!confirm("确认修改吗")){
            return;
        }
        document.getElementById("loading").style.display="block";

        var td=btn.parentElement;
        var tr=td.parentElement;
        var td1=tr.cells[2];
        var status=tr.cells[3].innerHTML;
        if(status=="yes"){
            status="no";
        }else{
            status="yes";
        }
        var table = {
            "alias":td1.innerHTML,
            "status":status,
            "action":"updateDatabase"
        };

        $.ajax({
            type:"POST",//提交请求的方式
            cache:true,//是否有缓存
            url:"${pageContext.request.contextPath}/Connect",//访问servlet的路径
            dataType:"json",//没有这个，将把后台放会的json解析成字符串
            data:table,//把内容序列化
            async:true,//是否异步
            error:function(request) {//请求出错
                document.getElementById("loading").style.display="none";

                alert("出错");
            },
            success:function(data) {//获得返回值
                document.getElementById("loading").style.display="none";

                alert(data.message);
                if(data.message=="修改成功"){
                    window.location.href="${pageContext.request.contextPath}/administrator/connect.jsp";
                }
            }
        });

    }


    $(document).ready(function(){
        var table = {
            "action":"selectDatabase"
        };

        $.ajax({
            type:"POST",//提交请求的方式
            cache:true,//是否有缓存
            url:"${pageContext.request.contextPath}/Connect",//访问servlet的路径
            dataType:"json",//没有这个，将把后台放会的json解析成字符串
            data:table,//把内容序列化
            async:true,//是否异步
            error:function(request) {//请求出错
                document.getElementById("loading").style.display="none";

                alert("出错");
            },
            success:function(data) {//获得返回值
                if(data.message.length==0){
                    document.getElementById("loading").style.display="none";
                    alert("未有数据库连接");
                } else{
                    //alert(data.message.attributes);
                    document.getElementById("loading").style.display="none";

                    $("#table tbody").remove();

                    var database=data.message;


                    var length=database.length;
                    var tbody=document.createElement("tbody");
                    for(var i=0;i<length;i++){
                        var tr=document.createElement("tr");
                        var td1=document.createElement("td");
                        var td2=document.createElement("td");
                        var td3=document.createElement("td");
                        var td4=document.createElement("td");
                        var td5=document.createElement("td");
                        var td6=document.createElement("td");
                        var td7=document.createElement("td");
                        td1.innerHTML=database[i].url;
                        td2.innerHTML=database[i].userName;
                        td3.innerHTML=database[i].alias;
                        td4.innerHTML=database[i].status;
                        if(td4.innerHTML=="yes"){
                            td5.innerHTML="<button type='button'   onclick='updateDatabase(this)'>关闭</button>  ";
                        }else {
                            td5.innerHTML="<button type='button'   onclick='updateDatabase(this)'>开启</button>  ";
                        }

                        td6.innerHTML= " <button type='button'   onclick='updateDatabasePassword(this)'>修改密码</button> ";
                        td7.innerHTML= " <button type='button'   onclick='f(this)'>删除</button> ";
                        tr.appendChild(td1);
                        tr.appendChild(td2);
                        tr.appendChild(td3);
                        tr.appendChild(td4);
                        tr.appendChild(td5);
                        tr.appendChild(td6);
                        tr.appendChild(td7);
                        tbody.appendChild(tr);

                    }
                    var tab=document.getElementById("table");
                    tab.appendChild(tbody);
                    tab.style.visibility="visible";
                    altRows('table');
                    $( "a, button" ).button();
                    $( "a, button" ).css("font-size","14px");
                }
            }
        });
    });



    function connect(url1,user1,password1,alias1) {
        var url = url1;
        var user = user1;
        var password = password1;
        var alias = alias1;
        if (url == "" || user == "" || password == "" || alias == "") {
            alert("请输入");
            return false;
        }

        var table = {
            "action": "insertDatabase",
            "url": url,
            "userName": user,
            "password": password,
            "alias": alias
        };
        var img = document.getElementById("loading");
        img.style.display = "block";


        $.ajax({
            type: "POST",//提交请求的方式
            cache: true,//是否有缓存
            url:"${pageContext.request.contextPath}/Connect",//访问servlet的路径
            dataType: "json",//没有这个，将把后台放会的json解析成字符串
            data: table,//把内容序列化
            async: true,//是否异步
            error: function (request) {//请求出错
                img.style.display = "none";
                alert("出错");
            },
            success: function (data) {//获得返回值
                img.style.display = "none";
                alert(data.message);
                if (data.message == "连接成功") {
                    window.location.href="${pageContext.request.contextPath}/administrator/connect.jsp";
                }

            }
        });


    }


    $(function() {
        var url = $( "#url" ),
            user = $( "#user" ),
            password = $( "#password" ),
            alias = $( "#alias" ),
            allFields = $( [] ).add( url ).add( user ).add( password ).add(alias),
            tips = $( ".validateTips" );

        function updateTips( t ) {
            tips
                .text( t )
                .addClass( "ui-state-highlight" );
            setTimeout(function() {
                tips.removeClass( "ui-state-highlight", 1500 );
            }, 500 );
        }

        function checkLength( o, n, min, max ) {
            if ( o.val().length > max || o.val().length < min ) {
                o.addClass( "ui-state-error" );
                updateTips( "" + n + " 的长度必须在 " +
                    min + " 和 " + max + " 之间。" );
                return false;
            } else {
                return true;
            }
        }

        function checkLength2( o, n) {
            if ( o.val().length ==0 ) {
                o.addClass( "ui-state-error" );
                updateTips( "" + n + " 不能为空");
                return false;
            } else {
                return true;
            }
        }
        $( "#dialog-form" ).dialog({
            autoOpen: false,
            height: 378,
            width: 350,
            modal: true,
            buttons: {
                "创建一个数据库连接": function() {
                    var bValid = true;
                    allFields.removeClass( "ui-state-error" );

                    bValid = bValid && checkLength2( url, "数据库" );
                    bValid = bValid && checkLength2( user, "名称" );
                    bValid = bValid && checkLength2( password, "密码" );
                    bValid = bValid && checkLength2( alias, "别称" );


                    if ( bValid ) {
                        // $( "#users tbody" ).append( "<tr>" +
                        //     "<td>" + name.val() + "</td>" +
                        //     "<td>" + email.val() + "</td>" +
                        //     "<td>" + password.val() + "</td>" +
                        //     "</tr>" );
                        connect(url.val(),user.val(),password.val(),alias.val());
                        $( this ).dialog( "close" );
                    }
                },
                Cancel: function() {
                    $( this ).dialog( "close" );
                }
            },
            close: function() {
                allFields.val( "" ).removeClass( "ui-state-error" );
            }
        });

        $( "#connect" )
            .button()
            .click(function() {
                $( "#dialog-form" ).dialog( "open" );
            });
    });



</script>


<script type="text/javascript">
    function altRows(id){
        if(document.getElementsByTagName){

            var table = document.getElementById(id);
            var rows = table.getElementsByTagName("tr");

            for(var i = 1; i < rows.length; i++){
                if(i % 2 == 0){
                    rows[i].className = "evenrowcolor";
                }else{
                    rows[i].className = "oddrowcolor";
                }
            }
        }
    }


</script>
<style type="text/css">
    table.altrowstable {
        width: 1000px;
        text-align: center;
        font-family: verdana,arial,sans-serif;
        font-size:15px;
        color:#333333;
        border-width: 1px;
        border-color: #a9c6c9;
        border-collapse: collapse;
        white-space: nowrap;
    }
    table.altrowstable th {
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #a9c6c9;
        background-color: black;
        color: white;
    }
    table.altrowstable td {
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #a9c6c9;
    }
    .oddrowcolor{
        background-color:#d4e3e5;
    }
    .evenrowcolor{
        background-color:#c3dde0;
    }
</style>

<body background="../bakground3.jpg" style="background-size: cover;" >
<span style="float: left;font-size: 40px;color: white;font-family: Calibri;font-weight: bolder">TTi Universal Data Upload System</span>
<img id="loading" src="../loading.gif" style="display: block;position: fixed;margin:auto;left:0;right:0;top:0;bottom:0;z-index: 9999">



<div id="dialog-form" title="创建新连接" >
    <p class="validateTips"></p>

    <form autocomplete="off">
        <fieldset>
            <label for="url">数据库（格式：地址：端口号/数据库名称,如：10.34.1.11:1528/UATA）</label>
            <input type="text" name="url" id="url" class="text ui-widget-content ui-corner-all " >
            <label for="user">用户名（如:apps)</label>
            <input type="text" name="user" id="user"  class="text ui-widget-content ui-corner-all">
            <input type="text" style="display:none;">
            <label for="password">密码(如:123456)</label>
            <input type="password" style="display:none;">
            <input type="password" name="password" id="password"  class="text ui-widget-content ui-corner-all">
            <label for="alias">别名(如：apps.UATA)</label>
            <input type="text" name="alias" id="alias"  class="text ui-widget-content ui-corner-all">
        </fieldset>
    </form>
</div>


<div style="text-align: right"><button type="button" id="connect" >新建数据库连接</button><a href="administrator.jsp">返回</a></div>
<br><br><br><br>
<table border="1" width="40%" id="table" style="visibility: hidden;margin: auto;margin-top: 70px" class="altrowstable" >
    <thead>
    <tr>
        <th>数据库</th>
        <th>用户名</th>
        <th>别名</th>
        <th style="width: 50px">开启状态</th>
        <th>操作1</th>
        <th>操作2</th>
        <th>操作3</th>
    </tr></thead>

</table>



<img id="loading" src="../loading.gif" style="display: none;position: fixed;margin:auto;left:0;right:0;top:0;bottom:0;">



<%--<div style="margin: auto;width: 400px">--%>
<%--url：<input type="text" id="url" placeholder="（形如：10.34.1.11:1528/UATA）" value="10.34.1.11:1528/UATA"/><br/>--%>
<%--user:<input type="text" id="user" placeholder="（形如：apps）" value="apps"/><br/>--%>
<%--password：<input type="password" id="password" placeholder="(形如：123456）" value="uat1289669"/><br/>--%>
<%--alias:<input type="text" id="alias" placeholder="(形如：apps.UATA）" value="apps.UATA"/><br/>--%>
<%--<br/>--%>
<%--</div>--%>
</body>




</html>
