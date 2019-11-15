<%--
  Created by IntelliJ IDEA.
  User: BG.Ding
  Date: 2018/10/31
  Time: 8:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传结果</title>
</head>
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">


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

        /*table-layout: fixed;*/
    }
    table.altrowstable th {
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #a9c6c9;
        background-color: black;
        color: white;
        white-space: nowrap;
        /*text-overflow: ellipsis;*/
        /*overflow: hidden;*/
    }
    table.altrowstable td {
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #a9c6c9;

        white-space: nowrap;
        /*text-overflow: ellipsis;*/
        /*overflow: hidden;*/
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
<textarea id="message"  readonly style="size: auto;display: none">${param.message }</textarea>


<div style="text-align: right;font-size: 20px;"> <button id="recallUpload">撤销上传</button><button type="button" onclick="window.close();">关闭</button></div>
<br><br>
<div style="width: 700px;margin: auto;display: none" id="div">
    第<span id="page"></span>/<span id="allPage"></span>页
    <button type="button" id="firstPage">首页</button>

    <button type="button" id="previousPage">上一页</button>

    <button type="button" id="nextPage" >下一页</button>

    <button type="button" id="lastPage">尾页</button>

    <span style="visibility: hidden">####</span><input type="number" id="GoToPage" style="width: 20px"><span style="visibility: hidden">##</span><button type="button" id="GoTo">GoTo</button>
<br><br>
</div>
<div id="div2" style="width: 1000px;margin: auto;" >
    <table id="table" style="margin: auto" class="altrowstable">

    </table>
</div>

<div style="display: none">
    <%--<input type="text" id="list">--%>
    <input type="text" id="count">
    <input type="text" id="length">
</div>
</body>
<script>



    $(document).ready(function() {
        var uploadId="";
        var list = "";
        var count =0;
        var pageCount=0;
        var page=0;
        var allPage=0;
        var title="";
        var url="";
        var userName="";
        var password="";
        var tableName="";
        var length2=0;

        document.getElementById("message").value=unescape(document.getElementById("message").value);

        if (document.getElementById("message").value != "") {


            $("input,select").css("border-radius", "5px");
            $("input,select").css("height", "30px");
            $("input,select").css("width", "200px");
            $("input,select").css("text-align", "center");
            $("input[type=text]").css("width", "150px");
            $("input[type=number]").css("width", "50px");
            $("input[type=submit], a, button").button();
            $("input[type=submit],a, button").css("font-size", "14px");
            if (document.getElementById("message").value.split(",").length == 3 && document.getElementById("message").value.split(",")[0] == "success") {
                uploadId=document.getElementById("message").value.split(",")[1];
                $.ajax({
                    type: "POST",//提交请求的方式
                    cache: true,//是否有缓存
                    url:"${pageContext.request.contextPath}/ServletUpload",//访问servlet的路径
                    dataType: "json",//没有这个，将把后台放会的json解析成字符串
                    data: {
                        "uploadId":  document.getElementById("message").value.split(",")[1],//(Number(document.getElementById("message").value.split(" ")[1].split(";")[1])+2)/3,
                        "action": "uploadCount"
                    },//把内容序列化
                    async: true,//是否异步
                    error: function (request) {//请求出错
                        document.getElementById("loading").style.display="none";
                        alert("出错");
                    },
                    success: function (data) {//获得返回值
                        document.getElementById("loading").style.display="none";
                        // Array.prototype.remove = function (dx) {
                        //     if (isNaN(dx) || dx > this.length) {
                        //         return false;
                        //     }
                        //     for (var i = 0, n = 0; i < this.length; i++) {
                        //         if (this[i] != this[dx]) {
                        //             this[n++] = this[i]
                        //         }
                        //     }
                        //     this.length -= 1
                        // }
                        tableName=data.message5;

                        url=data.message2[0];
                        userName=data.message2[1];
                        password=data.message2[2];
                        title= data.message4;



                        var thead = document.createElement("thead");
                        var tr = document.createElement("tr");
                        length2 = title.length - 1;
                        for (var i = 0; i < length2; i++) {
                            var th = document.createElement("th");
                            th.innerHTML = title[i];
                            tr.appendChild(th);
                        }
                        thead.appendChild(tr);
                        document.getElementById("table").appendChild(thead);


                        var pageInfo = data.message3;
                        //document.getElementById("list").value = list;
                        allPage=pageInfo[3];
                        page=1;
                        count=pageInfo[0];
                        pageCount=pageInfo[2];
                        document.getElementById("allPage").innerHTML = allPage;
                        document.getElementById("page").innerHTML = page;

                        list =data.message;


                        var tbody = document.createElement("tbody");

                        var i = 0;
                        for (; i < list.length; i++) {
                            var tr2 = document.createElement("tr");
                            for (var j = 0; j < list[i].length; j++) {
                                var td = document.createElement("td");
                                if(list[i][j]!=null){
                                    td.innerHTML = list[i][j];
                                }

                                tr2.appendChild(td);
                            }
                            tbody.appendChild(tr2);
                        }

                        document.getElementById("table").appendChild(tbody);

                        if(length2>5||document.getElementById("table").offsetWidth>1000){
                            document.getElementById("div2").style.overflowX="scroll";
                            document.getElementById("table").style.width=120*Number(length2)+"px";
                        }
                        document.getElementById("div").style.display="block";
                        altRows("table");
                    }
                });
            } else {
                alert(document.getElementById("message").value);
            }

        }

        $("#nextPage").click(function () {
            if(page==allPage){
                return;
            }
            page=page+1;
            document.getElementById("loading").style.display="block";
            $.ajax({
                type: "POST",//提交请求的方式
                cache: true,//是否有缓存
                url:"${pageContext.request.contextPath}/ServletUpload",//访问servlet的路径
                dataType: "json",//没有这个，将把后台放会的json解析成字符串
                data: {
                    "uploadId":  uploadId,//(Number(document.getElementById("message").value.split(" ")[1].split(";")[1])+2)/3,
                    "tableName":tableName,
                    "title":JSON.stringify(title),
                    "action": "showUploadResult",
                    "url":url,
                    "userName":userName,
                    "password":password,
                    "page":page
                },//把内容序列化
                async: true,//是否异步
                error: function (request) {//请求出错
                    document.getElementById("loading").style.display="none";
                    alert("出错");
                },
                success: function (data) {
                    document.getElementById("loading").style.display="none";
                    $("#table tbody").remove();
                    var tbody = document.createElement("tbody");
                    list=data.message;
                    for (var i=0; i < list.length; i++) {
                        var tr2 = document.createElement("tr");
                        for (var j = 0; j < list[i].length; j++) {
                            var td = document.createElement("td");
                            if(list[i][j]!=null){
                                td.innerHTML = list[i][j];
                            }
                            tr2.appendChild(td);
                        }
                        tbody.appendChild(tr2);
                    }

                    document.getElementById("table").appendChild(tbody);
                    document.getElementById("page").innerHTML = page;
                    if(document.getElementById("table").offsetWidth>1000){
                        document.getElementById("div2").style.overflowX="scroll";
                        document.getElementById("table").style.width=120*Number(length2)+"px";
                    }
                    altRows("table");
                }
            })

        })
        $("#previousPage").click(function () {
            if(page==1){
                return;
            }
            page=page-1;
            document.getElementById("loading").style.display="block";
            $.ajax({
                type: "POST",//提交请求的方式
                cache: true,//是否有缓存
                url:"${pageContext.request.contextPath}/ServletUpload",//访问servlet的路径
                dataType: "json",//没有这个，将把后台放会的json解析成字符串
                data: {
                    "uploadId":  uploadId,//(Number(document.getElementById("message").value.split(" ")[1].split(";")[1])+2)/3,
                    "tableName":tableName,
                    "title":JSON.stringify(title),
                    "action": "showUploadResult",
                    "url":url,
                    "userName":userName,
                    "password":password,
                    "page":page
                },//把内容序列化
                async: true,//是否异步
                error: function (request) {//请求出错
                    document.getElementById("loading").style.display="none";
                    alert("出错");
                },
                success: function (data) {

                    document.getElementById("loading").style.display="none";
                    $("#table tbody").remove();
                    var tbody = document.createElement("tbody");
                    list=data.message;
                    for (var i=0; i < list.length; i++) {
                        var tr2 = document.createElement("tr");
                        for (var j = 0; j < list[i].length; j++) {
                            var td = document.createElement("td");
                            if(list[i][j]!=null){
                                td.innerHTML = list[i][j];
                            }
                            tr2.appendChild(td);
                        }
                        tbody.appendChild(tr2);
                    }

                    document.getElementById("table").appendChild(tbody);
                    document.getElementById("page").innerHTML = page;
                    if(document.getElementById("table").offsetWidth>1000){
                        document.getElementById("div2").style.overflowX="scroll";
                        document.getElementById("table").style.width=120*Number(length2)+"px";
                    }
                    altRows("table");
                }
            })
        })
        $("#firstPage").click(function () {
            if(page==1){
                return;
            }
            page=1;
            document.getElementById("loading").style.display="block";
            $.ajax({
                type: "POST",//提交请求的方式
                cache: true,//是否有缓存
                url:"${pageContext.request.contextPath}/ServletUpload",//访问servlet的路径
                dataType: "json",//没有这个，将把后台放会的json解析成字符串
                data: {
                    "uploadId":  uploadId,//(Number(document.getElementById("message").value.split(" ")[1].split(";")[1])+2)/3,
                    "tableName":tableName,
                    "title":JSON.stringify(title),
                    "action": "showUploadResult",
                    "url":url,
                    "userName":userName,
                    "password":password,
                    "page":page
                },//把内容序列化
                async: true,//是否异步
                error: function (request) {//请求出错
                    document.getElementById("loading").style.display="none";
                    alert("出错");
                },
                success: function (data) {

                    document.getElementById("loading").style.display="none";
                    $("#table tbody").remove();
                    var tbody = document.createElement("tbody");
                    list=data.message;
                    for (var i=0; i < list.length; i++) {
                        var tr2 = document.createElement("tr");
                        for (var j = 0; j < list[i].length; j++) {
                            var td = document.createElement("td");
                            if(list[i][j]!=null){
                                td.innerHTML = list[i][j];
                            }
                            tr2.appendChild(td);
                        }
                        tbody.appendChild(tr2);
                    }

                    document.getElementById("table").appendChild(tbody);
                    document.getElementById("page").innerHTML = page;
                    if(document.getElementById("table").offsetWidth>1000){
                        document.getElementById("div2").style.overflowX="scroll";
                        document.getElementById("table").style.width=120*Number(length2)+"px";
                    }
                    altRows("table");
                }
            })
        })
        $("#lastPage").click(function () {
            if(page==allPage){
                return;
            }
            page=allPage;
            document.getElementById("loading").style.display="block";
            $.ajax({
                type: "POST",//提交请求的方式
                cache: true,//是否有缓存
                url:"${pageContext.request.contextPath}/ServletUpload",//访问servlet的路径
                dataType: "json",//没有这个，将把后台放会的json解析成字符串
                data: {
                    "uploadId":  uploadId,//(Number(document.getElementById("message").value.split(" ")[1].split(";")[1])+2)/3,
                    "tableName":tableName,
                    "title":JSON.stringify(title),
                    "action": "showUploadResult",
                    "url":url,
                    "userName":userName,
                    "password":password,
                    "page":page
                },//把内容序列化
                async: true,//是否异步
                error: function (request) {//请求出错
                    document.getElementById("loading").style.display="none";
                    alert("出错");
                },
                success: function (data) {
                    document.getElementById("loading").style.display="none";
                    $("#table tbody").remove();
                    var tbody = document.createElement("tbody");
                    list=data.message;
                    for (var i=0; i < list.length; i++) {
                        var tr2 = document.createElement("tr");
                        for (var j = 0; j < list[i].length; j++) {
                            var td = document.createElement("td");
                            if(list[i][j]!=null){
                                td.innerHTML = list[i][j];
                            }
                            tr2.appendChild(td);
                        }
                        tbody.appendChild(tr2);
                    }

                    document.getElementById("table").appendChild(tbody);
                    document.getElementById("page").innerHTML = page;
                    if(document.getElementById("table").offsetWidth>1000){
                        document.getElementById("div2").style.overflowX="scroll";
                        document.getElementById("table").style.width=120*Number(length2)+"px";
                    }
                    altRows("table");
                }
            })
        })
        $("#GoTo").click(function () {


            var GoToPage=Number(document.getElementById("GoToPage").value);
            if(GoToPage==page||GoToPage<1||GoToPage>allPage){
                return;
            }
            page=GoToPage;
            document.getElementById("loading").style.display="block";
            $.ajax({
                type: "POST",//提交请求的方式
                cache: true,//是否有缓存
                url:"${pageContext.request.contextPath}/ServletUpload",//访问servlet的路径
                dataType: "json",//没有这个，将把后台放会的json解析成字符串
                data: {
                    "uploadId":  uploadId,//(Number(document.getElementById("message").value.split(" ")[1].split(";")[1])+2)/3,
                    "tableName":tableName,
                    "title":JSON.stringify(title),
                    "action": "showUploadResult",
                    "url":url,
                    "userName":userName,
                    "password":password,
                    "page":page
                },//把内容序列化
                async: true,//是否异步
                error: function (request) {//请求出错
                    document.getElementById("loading").style.display="none";
                    alert("出错");
                },
                success: function (data) {
                    document.getElementById("loading").style.display="none";
                    $("#table tbody").remove();
                    var tbody = document.createElement("tbody");
                    list=data.message;
                    for (var i=0; i < list.length; i++) {
                        var tr2 = document.createElement("tr");
                        for (var j = 0; j < list[i].length; j++) {
                            var td = document.createElement("td");
                            if(list[i][j]!=null){
                                td.innerHTML = list[i][j];
                            }
                            tr2.appendChild(td);
                        }
                        tbody.appendChild(tr2);
                    }

                    document.getElementById("table").appendChild(tbody);
                    document.getElementById("page").innerHTML = page;
                    if(document.getElementById("table").offsetWidth>1000){
                        document.getElementById("div2").style.overflowX="scroll";
                        document.getElementById("table").style.width=120*Number(length2)+"px";
                    }
                    altRows("table");
                }
            })
        })

        $("#recallUpload").click(function () {
            if(!confirm("确认撤销上传吗")){
                return;
            }
            document.getElementById("loading").style.display="block";
            $.ajax({
                type: "POST",//提交请求的方式
                cache: true,//是否有缓存
                url:"${pageContext.request.contextPath}/ServletUpload",//访问servlet的路径
                dataType: "json",//没有这个，将把后台放会的json解析成字符串
                data: {
                    "uploadId":   document.getElementById("message").value.split(",")[1],//(Number(document.getElementById("message").value.split(" ")[1].split(";")[1])+2)/3,
                    "action": "recallUpload"
                },//把内容序列化
                async: true,//是否异步
                error: function (request) {//请求出错
                    document.getElementById("loading").style.display="none";
                    alert("出错");
                },
                success: function (data) {//获得返回值
                    document.getElementById("loading").style.display="none";
                    alert("已撤回所有"+data.message+"条记录");
                    window.close();
                }
            })
        })
    })
</script>
</html>
