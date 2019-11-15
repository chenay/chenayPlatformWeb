<%--
  Created by IntelliJ IDEA.
  User: BG.Ding
  Date: 2018/10/17
  Time: 8:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>编辑表</title>
</head>
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
<script>
    $(function() {
        $( " a, button" ).button();
        $( " a, button" ).css("font-size","14px");
        $( "input,select").css("border-radius","5px");
        $( "input,select").css("height","30px");
        $( "input,select").css("width","200px");
        $( "input,select").css("text-align","center");
    });
    $(document).ready(function(){
        function removeElement(_element){
            var _parentElement = _element.parentNode;
            if(_parentElement){
                _parentElement.removeChild(_element);
            }
        }

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
                document.getElementById("loading").style.display="none";

                if(data.message.length==0){
                    alert("未有数据库连接");
                } else{

                    var database=data.message;



                    // document.getElementById("blank").remove();

                    removeElement(document.getElementById("blank"));
                    var value="";

                    var str="";
                    for (var i = 0; i < database.length; i++) {

                        str += "<option value=\'" + JSON.stringify(database[i]).replace(/'/g,"html-quote")+ "\'>" + database[i].alias + "</option>";                    }
                    $("#database").append(str);
                }
            }
        });



        $("#database").change(function(){
            document.getElementById("loading").style.display="block";
            if(document.getElementById("pleaseSelect")!=null){
                removeElement(document.getElementById("pleaseSelect"));
            }

            var database=JSON.parse($(this).children('option:selected').val().replace(/html-quote/g,"'"));



            var table = {
                "alias":database.alias,
                "action":"selectFuncionTable"
            };

            $.ajax({
                type:"POST",//提交请求的方式
                cache:true,//是否有缓存
                url:"${pageContext.request.contextPath}/Servlet3",//访问servlet的路径
                dataType:"json",//没有这个，将把后台放会的json解析成字符串
                data:table,//把内容序列化
                async:true,//是否异步
                error:function(request) {//请求出错
                    document.getElementById("loading").style.display="none";

                    alert("出错");
                },
                success:function(data) {//获得返回值
                    document.getElementById("loading").style.display="none";

                    if(data.message1.length==0){

                        //document.getElementById("table_function").style.display="none";
                        if(document.getElementsByName("table")!=null){

                            var table=document.getElementsByName("table");
                            var length=table.length;
                            for(var i=0;i<length;i++ ){
                                //table[0].remove();
                                removeElement(table[0]);
                            }
                        }

                        document.getElementById("table_function").style.display="none";
                        alert("未有上传表");

                    } else{



                        if(document.getElementsByName("table")!=null){

                            var table=document.getElementsByName("table");
                            var length=table.length;
                            for(var i=0;i<length;i++ ){
                                //table[0].remove();
                                removeElement(table[0]);
                            }
                        }


                        document.getElementById("table_function").style.display="inline-block";
                        if(document.getElementById("blank_table")!=null){
                            //document.getElementById("blank_table").remove();
                            removeElement(document.getElementById("blank_table"));
                        }



                        var table=data.message1;


                        var value="";

                        var str="";
                        for (var i = 0; i < table.length; i++) {
                            value=table[i].tablename;
                            str += "<option name='table' value=" + value+ ">" + table[i].tablename + "</option>";
                        }
                        $("#table2").append(str);
                    }
                }
            });

        });



        var attributes="";
        var type="";
        var nullable="";
        var tlength=0;
        var length=0;

        var count=0;
        var allPage=0;
        var page=0;


        var tableId=0;

        $("#nextPage").click(function () {
            if(count>=length){
                return;
            }
            $("#table tbody").remove();
            var tbody=document.createElement("tbody");
            for(var i=count;i<length&&i<Number(count)+7;i++){
                var tr=document.createElement("tr");
                var td1=document.createElement("td");
                var td2=document.createElement("td");
                var td3=document.createElement("td");
                var td4=document.createElement("td");
                td1.innerHTML=attributes[i];
                td2.innerHTML=type[i];
                td3.innerHTML=nullable[i];
                td4.innerHTML=tlength[i];

                tbody.appendChild(tr);
                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);70
            }
            count=i;
            page=(i-1-(i-1)%7)/7+1;
            if(length%7==0){
                allPage=length/7;
            }else {
                allPage=(length-length%7)/7+1;
            }

            document.getElementById("currentPage").innerHTML=page;
            document.getElementById("allPage").innerHTML=allPage;
            var tab=document.getElementById("table");

            tab.appendChild(tbody);
            altRows("table");
        })


        $("#previousPage").click(function () {
            if(count<8){
                return;
            }
            if(count%7!=0){
                count=count-(count%7)-7;
            }else {
                count=count-2*7;
            }
            $("#table tbody").remove();
            var tbody=document.createElement("tbody");
            for(var i=count;i<length&&i<count+7;i++){
                var tr=document.createElement("tr");
                var td1=document.createElement("td");
                var td2=document.createElement("td");
                var td3=document.createElement("td");
                var td4=document.createElement("td");
                td1.innerHTML=attributes[i];
                td2.innerHTML=type[i];
                td3.innerHTML=nullable[i];
                td4.innerHTML=tlength[i];

                tbody.appendChild(tr);
                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
            }
            count=i;
            page=(i-1-(i-1)%7)/7+1;
            if(length%7==0){
                allPage=length/7;
            }else {
                allPage=(length-length%7)/7+1;
            }

            document.getElementById("currentPage").innerHTML=page;
            document.getElementById("allPage").innerHTML=allPage;
            var tab=document.getElementById("table");

            tab.appendChild(tbody);
            altRows("table");
        })


        $("#firstPage").click(function () {
            count=0;
            $("#table tbody").remove();
            var tbody=document.createElement("tbody");
            for(var i=count;i<length&&i<count+7;i++){
                var tr=document.createElement("tr");
                var td1=document.createElement("td");
                var td2=document.createElement("td");
                var td3=document.createElement("td");
                var td4=document.createElement("td");
                td1.innerHTML=attributes[i];
                td2.innerHTML=type[i];
                td3.innerHTML=nullable[i];
                td4.innerHTML=tlength[i];

                tbody.appendChild(tr);
                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
            }
            count=i;
            page=(i-1-(i-1)%7)/7+1;
            if(length%7==0){
                allPage=length/7;
            }else {
                allPage=(length-length%7)/7+1;
            }

            document.getElementById("currentPage").innerHTML=page;
            document.getElementById("allPage").innerHTML=allPage;
            var tab=document.getElementById("table");

            tab.appendChild(tbody);
            altRows("table");
        })


        $("#lastPage").click(function () {
            count=(allPage-1)*7;
            $("#table tbody").remove();
            var tbody=document.createElement("tbody");
            for(var i=count;i<length&&i<count+7;i++){
                var tr=document.createElement("tr");
                var td1=document.createElement("td");
                var td2=document.createElement("td");
                var td3=document.createElement("td");
                var td4=document.createElement("td");
                td1.innerHTML=attributes[i];
                td2.innerHTML=type[i];
                td3.innerHTML=nullable[i];
                td4.innerHTML=tlength[i];

                tbody.appendChild(tr);
                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
            }
            count=i;
            page=(i-1-(i-1)%7)/7+1;
            if(length%7==0){
                allPage=length/7;
            }else {
                allPage=(length-length%7)/7+1;
            }

            document.getElementById("currentPage").innerHTML=page;
            document.getElementById("allPage").innerHTML=allPage;
            var tab=document.getElementById("table");

            tab.appendChild(tbody);
            altRows("table");
        })
        var action="";
        $("#selecttable").click(function(){


            if(document.getElementById("database").value==""){
                alert("请选择数据库");
                return false;
            }
            var tablename=document.getElementById("table2").value;



            var database=JSON.parse(document.getElementById("database").value.replace(/html-quote/g,"'"));





            if(tablename==""){
                alert("请选择数据库表名称");
                return false;
            }
            document.getElementById("loading").style.display="block";


            var table = {
                "tablename" : tablename,
                "url" : database.url,
                "userName":database.userName,
                "password":database.password,
                "alias":database.alias
            };
            $.ajax({
                type:"POST",//提交请求的方式
                cache:true,//是否有缓存
                url:"${pageContext.request.contextPath}/ServletSelect",//访问servlet的路径
                dataType:"json",//没有这个，将把后台放会的json解析成字符串
                data:table,//把内容序列化
                async:true,//是否异步
                error:function(request) {//请求出错
                    document.getElementById("loading").style.display="none";

                    alert("出错");
                },
                success:function(data) {//获得返回值
                    document.getElementById("loading").style.display="none";

                    if(data.message=="notExist"){
                        alert("此数据库中不存在此表")
                    } else{
                        //alert(data.message.attributes);
                        table2=data.message;

                        $("#table tbody").remove();
                        tableId=data.message.tableId;
                        attributes=data.message.attributes.split(",");
                        type=data.message.type.split(",");
                        nullable=data.message.nullable.split(",");
                        tlength=data.message.length.split(",");

                       length=attributes.length;
                        var tbody=document.createElement("tbody");
                        for(var i=0;i<length&&i<7;i++){
                            var tr=document.createElement("tr");
                            var td1=document.createElement("td");
                            var td2=document.createElement("td");
                            var td3=document.createElement("td");
                            var td4=document.createElement("td");
                            td1.innerHTML=attributes[i];
                            td2.innerHTML=type[i];
                            td3.innerHTML=nullable[i];
                            td4.innerHTML=tlength[i];

                            tbody.appendChild(tr);
                            tr.appendChild(td1);
                            tr.appendChild(td2);
                            tr.appendChild(td3);
                            tr.appendChild(td4);
                        }

                        count=i;
                        page=(i-1-(i-1)%7)/7+1;
                        if(length%7==0){
                            allPage=length/7;
                        }else {
                            allPage=(length-length%7)/7+1;
                        }
                        document.getElementById("currentPage").innerHTML=page;
                        document.getElementById("allPage").innerHTML=allPage;

                        var tab=document.getElementById("table");
                        tab.appendChild(tbody);
                        tab.style.visibility="visible";
                        document.getElementById("table_result").style.display="block";
                        document.getElementById("div").style.display="block";



                        document.getElementById("database1").value=database.alias;
                        document.getElementById("tablename1").value=document.getElementById("table2").value;


                        // document.getElementById("div2").style.display="block";
                        // document.getElementById("tablename1").style.display="block";



                        document.getElementById("remark").value=data.message.remark;
                        if(data.message.ps!=null){
                            document.getElementById("ps").value=data.message.ps;
                        }

                        document.getElementById("update").style.display="inline-block";


                        document.getElementById("delete").style.display="inline-block";


                        altRows("table");
                    }
                }
            });
        });


        $("#update").click(function(){


            var tablename=document.getElementById("tablename1").value;

            var remark=document.getElementById("remark").value;

            var ps=document.getElementById("ps").value;

            var database=document.getElementById("database1").value;



            if(remark==""){
                alert("请输入上传功能名称");
                return false;
            }
            if(ps==""){
                alert("请输入上传功能备注");
                return false;
            }
            if(!confirm("确认修改吗")){
                return;
            }
            document.getElementById("loading").style.display="block";

            var a = "";
            var t= "";
            var n= "";
            var tl="";
            for (var i=0;i<length;i++) {
                // var tdArr = trList.eq(i).find("td");
                // var a = tdArr[0].innerHTML;
                // var t= tdArr[1].innerHTML;
                // var n= tdArr[2].innerHTML;
                // var tl=tdArr[3].innerHTML;
                // a = attributes[i];
                // t= type[i];
                // n= nullable[i];
                // tl=tlength[i];
                if(i<length-1){
                    a=a+attributes[i]+",";
                    t=t+type[i]+",";
                    n=n+nullable[i]+",";
                    tl=tl+tlength[i]+",";
                }else {
                    a=a+attributes[i];
                    t=t+type[i];
                    n=n+nullable[i];
                    tl=tl+tlength[i];
                }
            }



            var table = {
                "tablename" : tablename,
                "attributes" : a,
                "type" : t,
                "nullable" : n,
                "remark" : remark,
                "database":database,
                "length":tl,
                "ps":ps,
                "tableId":tableId,
                "action" : "update"
            };


            $.ajax({
                type:"POST",//提交请求的方式
                cache:true,//是否有缓存
                url:"${pageContext.request.contextPath}/Servlet3",//访问servlet的路径
                dataType:"json",//没有这个，将把后台放会的json解析成字符串
                data:table,//把内容序列化
                async:true,//是否异步
                error:function(request) {//请求出错
                    document.getElementById("loading").style.display="none";

                    alert("出错了");
                },
                success:function(data) {//获得返回值
                    document.getElementById("loading").style.display="none";

                    alert(data.message);
                    if(data.message=="success"){
                        window.location.href="${pageContext.request.contextPath}/administrator/editTable.jsp";
                    }

                }
            });
        });


        $("#delete").click(function(){
            if(!confirm("确认删除上传表功能吗")){
                return;
            }
            document.getElementById("loading").style.display="block";



            var table = {
                "tableId" : tableId,
                "action" : "delete"
            };
            $.ajax({
                type:"POST",//提交请求的方式
                cache:true,//是否有缓存
                url:"${pageContext.request.contextPath}/Servlet3",//访问servlet的路径
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
                    window.location.href="${pageContext.request.contextPath}/administrator/editTable.jsp"
                }
            });
        });


    });
</script>



<script type="text/javascript">
    function altRows(id){
        if(document.getElementsByTagName){

            var table = document.getElementById(id);
            var rows = table.getElementsByTagName("tr");

            for( var i = 1; i < rows.length; i++){
                if(i % 2 == 0){
                    rows[i].className = "evenrowcolor";
                }else{
                    rows[i].className = "oddrowcolor";
                }
            }
        }
        $( " a, button" ).button();
        $( " a, button" ).css("font-size","14px");
        $( "input,select").css("border-radius","5px");
        $( "input,select").css("height","30px");
        $( "input,select").css("width","200px");
        $( "input,select").css("text-align","center");
    }


</script>
<style type="text/css">
    table.altrowstable {
        width: 530px;
        text-align: center;
        font-family: verdana,arial,sans-serif;
        font-size:15px;
        color:#333333;
        border-width: 1px;
        border-color: #a9c6c9;
        border-collapse: collapse;
    }
    table.altrowstable th {
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #a9c6c9;
        background-color: lawngreen;

        white-space: nowrap;
        background-color: black;
        color: white;
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


<div style="text-align: right"><button id="update" type="button" style="display: none">保存</button>
    <button id="delete" type="button" style="display: none">删除</button><a href="administrator.jsp">返回</a></div>
<br><br>
<div style="width: 500px;margin-left:250px;margin-top: 60px ;float: left;">
    <span style="visibility: hidden">数据库</span>数据库：<select name="database" id="database" >
    <option value="" style="display: none" id="pleaseSelect">--请选择--</option>
    <option value="" id="blank">--无权限--</option>
</select>
    <br><br>
    <div  id="table_function" style="display: none">
        数据库表名称：<select id="table2" >
        <option value="" style="display: none">--请选择--</option>
        <option value="" id="blank_table">--无功能--</option>
    </select>
        <br><br>
        <span style="visibility: hidden">数据库</span><button  id="selecttable" type="button">查询</button>
    </div>
    <br><br>
    <div style="display: none;width: 400px;" id="div" >
        <span style="visibility: hidden">数据库</span>数据库：<input id="database1" disabled ><br><br>
        数据库表名称：<input id="tablename1" disabled ><br><br>
        上传功能名称：<input type="text"  id="remark" /><br><br>
        上传功能备注：<input type="text"  id="ps" /><br><br>
        <span style="color: red">Tip:只可修改上传功能名称及备注</span>
    </div>

</div>

<div id="table_result" style="display: none">
    <br><br><br>
    数据库表属性
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第<span id="currentPage"></span>/<span id="allPage"></span>页
    <button type="button" id="firstPage">首页</button>

    <button type="button" id="previousPage">上一页</button>

    <button type="button" id="nextPage">下一页</button>

    <button type="button" id="lastPage">尾页</button>
</div>

<table border="1" width="30%" id="table" class="altrowstable" style="visibility: hidden;margin-top: 20px;margin-left: -100px">

    <thead>
    <tr>
        <th>字段</th>
        <th>类型</th>
        <th>能否为空</th>
        <th>字段长度</th>
    </tr></thead>

</table>




</body>
</html>
