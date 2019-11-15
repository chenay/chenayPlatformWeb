<%--
  Created by IntelliJ IDEA.
  User: BG.Ding
  Date: 2018/10/9
  Time: 16:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>增加用户</title>
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
        $( "input").css("width","200px");
        $( "select").css("width","190px");
        $( "#ps").css("width","190px");
        $( "#tablename").css("width","190px");
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

                    //document.getElementById("blank").remove();

                    removeElement(document.getElementById("blank"));
                    var value="";

                    var str="";
                    for (var i = 0; i < database.length; i++) {
                        str += "<option value=\'" + JSON.stringify(database[i]).replace(/'/g,"html-quote")+ "\'>" + database[i].alias + "</option>";
                    }
                    $("#database").append(str);
                }
            }
        });



        $("#database").change(function(){
            if(document.getElementById("pleaseSelect")!=null){
                removeElement(document.getElementById("pleaseSelect"));
            }
            document.getElementById("loading").style.display="block";

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
                                removeElement(table[0])
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


                        document.getElementById("table_function").style.display="block";
                        if(document.getElementById("blank_table")!=null){
                            //document.getElementById("blank_table").remove();
                            removeElement(document.getElementById("blank_table"));
                        }



                        var table=data.message1;


                        var value="";

                        var str="";
                        for (var i = 0; i < table.length; i++) {
                            // value=table[i].tablename+","+table[i].remark+","+table[i].ps;
                            str += "<option name='table' value=\'" + JSON.stringify(table[i]).replace(/'/g,"html-quote")+ "\'>" + table[i].remark + "</option>";
                        }
                        $("#table2").append(str);

                    }
                }
            });

        });

        $("#table2").change(function(){
            var table=JSON.parse($(this).children('option:selected').val());
            document.getElementById("tablename").value=table.tablename;
            document.getElementById("ps").value=table.ps;
            document.getElementById("tableId").value=table.tableId;
        });

    });








    $(document).ready(function(){




        $("#addRole").click(function () {


            if(document.getElementById("database").value==""){
                alert("请选择数据库")
                return;
            }

            if(document.getElementById("table2").value==""){
                alert("请选择上传功能名称")
                return;
            }
            var role=document.getElementById("role2").value;

            var table=JSON.parse(document.getElementById("table2").value);

            // alert(document.getElementById("table2").value);



            if(role!=""){
                var roles=role.split(";");
                for(var i=0;i<roles.length;i++){
                    if(roles[i]==table.tableId){
                        alert("已添加");
                        return;
                    }
                }
            }








            // var database=data.message;


            // var length=database.length;
            var tbody="";

            tbody=document.createElement("tbody");


            role+=table.tableId+";";
            // for(var i=0;i<length;i++){

            document.getElementById("role2").value=role;
            var tr=document.createElement("tr");
            var td1=document.createElement("td");
            var td2=document.createElement("td");

            // var button=document.createElement("button");
            // button.setAttribute("type","button");
            // button.setAttribute("class","close");
            // button.setAttribute("innerHTML","删除kkk")

            var td3=document.createElement("td");

            var td4=document.createElement("td");

            var td5=document.createElement("td");

            td1.innerHTML=table.database;
            td2.innerHTML=table.tablename;
            td3.innerHTML=table.remark;
            td4.innerHTML=table.ps;
            td5.innerHTML="<span style='display: none'>"+table.tableId+"</span><button type='button'   onclick='deleteRole(this)'>删除</button>";





            tbody.appendChild(tr);
            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            tr.appendChild(td5);
            var tab=document.getElementById("role");
            tab.appendChild(tbody);
            tab.style.visibility="visible";

            altRows("role");
        });






        $("#createUser").click(function () {
            var staffNumber = document.getElementById("staffNumber").value;
            var name = document.getElementById("name").value;
            var role = document.getElementById("role2").value;;
            var password = document.getElementById("password").value;
            var password2 = document.getElementById("password2").value;

            if(staffNumber==""){
                alert("请填写工号");
                return;
            }

            if(staffNumber=="admin"){
                alert("无法对管理员操作");
                return;
            }

            if(name==""){
                alert("请填写姓名");
                return;
            }

            if(password==""){
                alert("请填写密码");
                return;
            }

            if(password2==""){
                alert("请确认密码");
                return;
            }


            if(password!=password2){
                alert("确认密码一致");
                return;
            }

            if(password.indexOf(" ")!=-1){
                alert("密码不允许含有空格");
                return;
            }
            if(!confirm("确认提交吗")){
                return;
            }
            document.getElementById("loading").style.display="block";

            var user = {
                "staffNumber": staffNumber,
                "name": name,
                "role": role,
                "password": password,
                "action": "createUser"
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
                success: function (data) {//获得返回值
                    document.getElementById("loading").style.display="none";

                    alert(data.message);
                }
            });

        });


    });


    function deleteRole(btn){

        if(!confirm("确认删除此权限吗")){
            return;
        }
        var td = btn.parentElement;
        var tr=td.parentElement;
        //alert(td.childNodes[0].innerHTML);
        var tableId=td.childNodes[0].innerHTML;

        function removeElement(_element){
            var _parentElement = _element.parentNode;
            if(_parentElement){
                _parentElement.removeChild(_element);
            }
        }

        // document.getElementById("blank").remove();

        removeElement(tr);
        // tr.remove();

        var role = document.getElementById("role2").value;
        var roles = role.split(";");
        role = "";
        for (var i = 0; i < roles.length; i++) {

            if(roles[i]==""){
                continue;
            }
            if (roles[i]!=tableId) {
                role = role+roles[i] + ";";
            }
        }
        document.getElementById("role2").value = role;

        if (role == "") {
            $("#newRole tbody").remove();
        }
        altRows("newRole");
    }
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
        $( "input").css("width","200px");
        $( "select").css("width","190px");
        $( "#ps").css("width","190px");
        $( "#tablename").css("width","190px");
        $( "input,select").css("text-align","center");
    }


</script>
<style type="text/css">
    table.altrowstable {
        width: 900px;
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


<div style="text-align: right"><button id="createUser" type="button">保存</button>
    <a href="administrator.jsp">返回</a>
</div>
<br><br>
<div style="margin: auto;width: 900px">

    新建用户<br><hr style="color:yellow"/>
    <span style="visibility: hidden">数据库啊</span>工号：<input id="staffNumber" type="text" placeholder="工号">
    <span style="visibility: hidden">数据库啊</span>姓名：<input id="name" type="text" placeholder="姓名"><br><br>

<input type="text" id="role2" style="width: 1000px;display: none"  >
    <input type="text" style="display:none;">
<input type="password" style="display:none;">
    <span style="visibility: hidden">数据库啊</span>密码：<input id="password" type="password" placeholder="密码" value="">
    <span style="visibility: hidden">数据库啊</span>确认：<input id="password2" type="password" placeholder="确认密码"><br><br>
<%--角色(role)<div id="role"></div>--%>


    分配权限<span style="color: red">(请为此用户分配上传表功能使用权限)</span><br><hr style="color:yellow"/>
<div style="margin-top: 3px;float: left">
    <span style="visibility: hidden">数据库</span>数据库：<select name="database" id="database" >
        <option value="" id="pleaseSelect">--请选择--</option>
        <option value="" id="blank">--无权限--</option>
    </select>
</div>

<div style="display: none;margin-top: 11px" id="table_function" >
    <span style="visibility: hidden">kk</span>上传功能名称：<select id="table2"  >
    <option value="" id="pleaseSelectTable">--请选择--</option>
    <option value="" id="blank_table">--无功能--</option>
</select>
    <br><br>

    上传功能备注：<input id="ps" type="text" readonly style="background-color: lightgrey">
    <span style="visibility: hidden">k</span>数据库表名称：<input id="tablename" type="text" disabled>

    <button id="addRole" type="button" style="margin-left: 10px">添加</button>
</div>

    <br><br>
    <table border="1"   id="role" style="visibility: hidden;margin:auto;text-align: center" class="altrowstable">
        <thead>
        <tr>
            <th>数据库</th>
            <th>数据库表名称</th>
            <th>上传功能名称</th>
            <th>上传功能备注</th>
            <th>操作</th>

        </tr></thead>

    </table>

</div>
</body>
</html>
