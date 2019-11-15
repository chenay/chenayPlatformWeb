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
    <title>编辑用户</title>
</head>
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">


<style>
    html {overflow-y: scroll}
</style>
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
                alert("出错");
            },
            success:function(data) {//获得返回值
                if(data.message.length==0){
                    alert("未有数据库连接");
                } else{

                    var database=data.message;

                    function removeElement(_element){
                        var _parentElement = _element.parentNode;
                        if(_parentElement){
                            _parentElement.removeChild(_element);
                        }
                    }

                    // document.getElementById("blank").remove();

                    removeElement(document.getElementById("blank"));

                    var value="";

                    var str="";
                    for (var i = 0; i < database.length; i++) {
                       // value=database[i].url+","+database[i].userName+","+database[i].password+","+database[i].alias;

                        str += "<option value=\'" + JSON.stringify(database[i]).replace(/'/g,"html-quote")+ "\'>" + database[i].alias + "</option>";
                    }
                    $("#database").append(str);
                }
            }
        });





        $("#database").change(function(){
            // var selected=$(this).children('option:selected').val();
            if(document.getElementById("pleaseSelect")!=null){
                removeElement(document.getElementById("pleaseSelect"));
            }

            var database=JSON.parse($(this).children('option:selected').val().replace(/html-quote/g,"'"));
            document.getElementById("loading").style.display="block";
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
                                removeElement(table[0])
                            }
                        }

                        document.getElementById("table_function").style.visibility="hidden";
                        alert("未有上传表");

                    } else{



                        if(document.getElementsByName("table")!=null){

                            var table=document.getElementsByName("table");
                            var length=table.length;
                            for(var i=0;i<length;i++ ){
                                removeElement(table[0])
                            }
                        }


                        document.getElementById("table_function").style.visibility="visible";
                        if(document.getElementById("blank_table")!=null){


                            // document.getElementById("blank").remove();

                            removeElement(document.getElementById("blank_table"));
                        }



                        var table=data.message1;


                        var value="";

                        var str="";
                        for (var i = 0; i < table.length; i++) {


                            str += "<option name='table' value=\'" + JSON.stringify(table[i]).replace(/'/g,"html-quote") + "\'>" + table[i].remark + "</option>";
                        }
                        $("#table2").append(str);
                    }
                }
            });

        });
    });








    $(document).ready(function(){



        var users="";
        var count=0;
        var allPage=0;
        var page=0;





        $("#nextPage").click(function () {
            if(count>=users.length){
                return;
            }
            $("#user tbody").remove();


            var tbody=document.createElement("tbody");
            for(var i=count;i<users.length&&i<Number(count)+3;i++){
                var tr=document.createElement("tr");
                var td1=document.createElement("td");
                td1.innerHTML=i+1;
                var td2=document.createElement("td");
                td2.innerHTML=users[i].staffNumber;

                var td3=document.createElement("td");

                td3.innerHTML=users[i].name;
                var td4=document.createElement("td");
                td4.innerHTML=users[i].createDate;

                var td5=document.createElement("td");

                td5.innerHTML=users[i].updateDate;

                var td6=document.createElement("td");
                if(users[i].role==null){
                    td6.innerHTML="<span style='display: none'>"+""+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editPassword(this)'>修改密码</button>"
                }else {
                    td6.innerHTML="<span style='display: none'>"+users[i].role+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editPassword(this)'>修改密码</button>"

                }
                var td7=document.createElement("td");
                if(users[i].role==null){
                    td7.innerHTML="<span style='display: none'>"+""+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editRole(this)'>修改权限</button>"
                }else {
                    td7.innerHTML="<span style='display: none'>"+users[i].role+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editRole(this)'>修改权限</button>"
                }
                var td8=document.createElement("td");
                td8.innerHTML="<button type='button' onclick='deleteUser(this)' >删除</button>"

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
                tr.appendChild(td5);
                tr.appendChild(td6);


                tr.appendChild(td7);

                tr.appendChild(td8);
                tbody.appendChild(tr);
            }
            document.getElementById("user").appendChild(tbody);
            document.getElementById("user").style.visibility="visible";
            count=i;
            page=(i-1-(i-1)%3)/3+1;
            if(users.length%3==0){
                allPage=users.length/3;
            }else {
                allPage=(users.length-users.length%3)/3+1;
            }

            document.getElementById("currentPage").innerHTML=page;
            document.getElementById("allPage").innerHTML=allPage;
            altRows("user");
        })




        $("#previousPage").click(function () {
            if(count<4){
                return;
            }
            if(count%3!=0){
                count=count-(count%3)-3;
            }else {
                count=count-2*3;
            }
            if(count>=users.length){
                return;
            }
            $("#user tbody").remove();


            var tbody=document.createElement("tbody");
            for(var i=count;i<users.length&&i<count+3;i++){
                var tr=document.createElement("tr");
                var td1=document.createElement("td");
                td1.innerHTML=i+1;
                var td2=document.createElement("td");
                td2.innerHTML=users[i].staffNumber;

                var td3=document.createElement("td");

                td3.innerHTML=users[i].name;
                var td4=document.createElement("td");
                td4.innerHTML=users[i].createDate;

                var td5=document.createElement("td");

                td5.innerHTML=users[i].updateDate;

                var td6=document.createElement("td");
                if(users[i].role==null){
                    td6.innerHTML="<span style='display: none'>"+""+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editPassword(this)'>修改密码</button>"
                }else {
                    td6.innerHTML="<span style='display: none'>"+users[i].role+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editPassword(this)'>修改密码</button>"

                }
                var td7=document.createElement("td");
                if(users[i].role==null){
                    td7.innerHTML="<span style='display: none'>"+""+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editRole(this)'>修改权限</button>"
                }else {
                    td7.innerHTML="<span style='display: none'>"+users[i].role+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editRole(this)'>修改权限</button>"
                }
                var td8=document.createElement("td");
                td8.innerHTML="<button type='button' onclick='deleteUser(this)' >删除</button>"

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
                tr.appendChild(td5);
                tr.appendChild(td6);


                tr.appendChild(td7);

                tr.appendChild(td8);
                tbody.appendChild(tr);
            }
            document.getElementById("user").appendChild(tbody);
            document.getElementById("user").style.visibility="visible";
            count=i;
            page=(i-1-(i-1)%3)/3+1;

            if(users.length%3==0){
                allPage=users.length/3;
            }else {
                allPage=(users.length-users.length%3)/3+1;
            }

            document.getElementById("currentPage").innerHTML=page;
            document.getElementById("allPage").innerHTML=allPage;
            altRows("user");
        })

        $("#table2").change(function(){
            var table=JSON.parse($(this).children('option:selected').val().replace(/html-quote/g,"'"));
            document.getElementById("tablename").value=table.tablename;
            document.getElementById("ps").value=table.ps;
            document.getElementById("tableId").value=table.tableId;
        });




        $("#firstPage").click(function () {
            count=0;

            $("#user tbody").remove();


            var tbody=document.createElement("tbody");
            for(var i=count;i<users.length&&i<count+3;i++){
                var tr=document.createElement("tr");
                var td1=document.createElement("td");
                td1.innerHTML=i+1;
                var td2=document.createElement("td");
                td2.innerHTML=users[i].staffNumber;

                var td3=document.createElement("td");

                td3.innerHTML=users[i].name;
                var td4=document.createElement("td");
                td4.innerHTML=users[i].createDate;

                var td5=document.createElement("td");

                td5.innerHTML=users[i].updateDate;

                var td6=document.createElement("td");
                if(users[i].role==null){
                    td6.innerHTML="<span style='display: none'>"+""+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editPassword(this)'>修改密码</button>"
                }else {
                    td6.innerHTML="<span style='display: none'>"+users[i].role+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editPassword(this)'>修改密码</button>"

                }
                var td7=document.createElement("td");
                if(users[i].role==null){
                    td7.innerHTML="<span style='display: none'>"+""+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editRole(this)'>修改权限</button>"
                }else {
                    td7.innerHTML="<span style='display: none'>"+users[i].role+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editRole(this)'>修改权限</button>"
                }
                var td8=document.createElement("td");
                td8.innerHTML="<button type='button' onclick='deleteUser(this)' >删除</button>"

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
                tr.appendChild(td5);
                tr.appendChild(td6);


                tr.appendChild(td7);

                tr.appendChild(td8);
                tbody.appendChild(tr);
            }
            document.getElementById("user").appendChild(tbody);
            document.getElementById("user").style.visibility="visible";
            count=i;
            page=(i-1-(i-1)%3)/3+1;
            if(users.length%3==0){
                allPage=users.length/3;
            }else {
                allPage=(users.length-users.length%3)/3+1;
            }

            document.getElementById("currentPage").innerHTML=page;
            document.getElementById("allPage").innerHTML=allPage;
            altRows("user");
        })



        $("#lastPage").click(function () {

            count=(allPage-1)*3;

            $("#user tbody").remove();


            var tbody=document.createElement("tbody");
            for(var i=count;i<users.length&&i<count+3;i++){
                var tr=document.createElement("tr");
                var td1=document.createElement("td");
                td1.innerHTML=i+1;
                var td2=document.createElement("td");
                td2.innerHTML=users[i].staffNumber;

                var td3=document.createElement("td");

                td3.innerHTML=users[i].name;
                var td4=document.createElement("td");
                td4.innerHTML=users[i].createDate;

                var td5=document.createElement("td");

                td5.innerHTML=users[i].updateDate;

                var td6=document.createElement("td");
                if(users[i].role==null){
                    td6.innerHTML="<span style='display: none'>"+""+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editPassword(this)'>修改密码</button>"
                }else {
                    td6.innerHTML="<span style='display: none'>"+users[i].role+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editPassword(this)'>修改密码</button>"

                }
                var td7=document.createElement("td");
                if(users[i].role==null){
                    td7.innerHTML="<span style='display: none'>"+""+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editRole(this)'>修改权限</button>"
                }else {
                    td7.innerHTML="<span style='display: none'>"+users[i].role+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editRole(this)'>修改权限</button>"
                }
                var td8=document.createElement("td");
                td8.innerHTML="<button type='button' onclick='deleteUser(this)' >删除</button>"

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
                tr.appendChild(td5);
                tr.appendChild(td6);


                tr.appendChild(td7);

                tr.appendChild(td8);
                tbody.appendChild(tr);
            }
            document.getElementById("user").appendChild(tbody);
            document.getElementById("user").style.visibility="visible";
            count=i;
            page=(i-1-(i-1)%3)/3+1;
            if(users.length%3==0){
                allPage=users.length/3;
            }else {
                allPage=(users.length-users.length%3)/3+1;
            }

            document.getElementById("currentPage").innerHTML=page;
            document.getElementById("allPage").innerHTML=allPage;
            altRows("user");
        })


        $("#selectUser").click(function () {

            var staffNumber=document.getElementById("sN").value;



            document.getElementById("loading").style.display="block";

            var user = {
                "staffNumber":staffNumber,
                "action":"selectUser"
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

                    alert("出错了");
                },
                success: function (data) {//获得返回值
                    document.getElementById("loading").style.display="none";


                    if(data.message.length==0){
                        document.getElementById("result").style.display="none";
                        alert("不存在此工号");
                        return;
                    }
                    $("#user tbody").remove();
                    document.getElementById("result").style.display="block";



                    users=data.message;


                    if(users[0].role=="admin"){
                        alert("无法对管理员操作");
                        return;
                    }

                    var tbody=document.createElement("tbody");
                    var i="";
                    for(i=0;i<users.length&&i<3;i++){


                        var tr=document.createElement("tr");
                        var td1=document.createElement("td");
                        td1.innerHTML=i+1;
                        var td2=document.createElement("td");
                        td2.innerHTML=users[i].staffNumber;

                        var td3=document.createElement("td");

                        td3.innerHTML=users[i].name;
                        var td4=document.createElement("td");
                        td4.innerHTML=users[i].createDate;

                        var td5=document.createElement("td");

                        td5.innerHTML=users[i].updateDate;

                        var td6=document.createElement("td");



                        if(users[i].role==null){
                            td6.innerHTML="<span style='display: none'>"+""+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editPassword(this)'>修改密码</button>"
                        }else {
                            td6.innerHTML="<span style='display: none'>"+users[i].role+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editPassword(this)'>修改密码</button>"

                        }









                        var td7=document.createElement("td");

                        if(users[i].role==null){
                            td7.innerHTML="<span style='display: none'>"+""+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editRole(this)'>修改权限</button>"
                        }else {
                            td7.innerHTML="<span style='display: none'>"+users[i].role+"</span ><span style='display: none'>"+users[i].password+"</span><button type='button' onclick='editRole(this)'>修改权限</button>"

                        }
                        var td8=document.createElement("td");
                        td8.innerHTML="<button type='button' onclick='deleteUser(this)' >删除</button>"
                        tr.appendChild(td1);
                        tr.appendChild(td2);
                        tr.appendChild(td3);
                        tr.appendChild(td4);
                        tr.appendChild(td5);
                        tr.appendChild(td6);
                        tr.appendChild(td7);
                        tr.appendChild(td8);
                        tbody.appendChild(tr);
                    }
                    count=i;
                    page=(i-1-(i-1)%3)/3+1;
                    if(users.length%3==0){
                        allPage=users.length/3;
                    }else {
                        allPage=(users.length-users.length%3)/3+1;
                    }


                    document.getElementById("user").appendChild(tbody);
                    document.getElementById("user").style.visibility="visible";

                    document.getElementById("currentPage").innerHTML=page;
                    document.getElementById("allPage").innerHTML=allPage;

                    altRows("user");

                }
            });


        });




        $("#cancel").click(function () {

            document.getElementById("div1").style.display="block";
            document.getElementById("div2").style.display="none";
            document.getElementById("updateUser").style.display="none";
            document.getElementById("selectUser").style.display="inline-block";
            document.getElementById("cancel").style.display="none";
            document.getElementById("back").style.display="inline-block";
        })

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

            var table=JSON.parse(document.getElementById("table2").value.replace(/html-quote/g,"'"));



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
            var tab=document.getElementById("newRole");
            tab.appendChild(tbody);
            tab.style.visibility="visible";

            altRows("newRole");
        });







        $("#updateUser").click(function () {
            var staffNumber = document.getElementById("staffNumber").value;
            var name = document.getElementById("name").value;
            var role = document.getElementById("role2").value;
            var password="";
            if(document.getElementById("newPassword").value!=""){
                password = document.getElementById("newPassword").value;
            }else {
                password = document.getElementById("oldPassword").value;
            }



            if(!confirm("确认修改吗")){
                return;
            }


            document.getElementById("loading").style.display="block";


            var user = {
                "staffNumber": staffNumber,
                "name": name,
                "role": role,
                "password":password,
                "action": "updateUserRole"
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
                    if(data.message=="success"){
                        $("#selectUser").click();
                        document.getElementById("div1").style.display="block";
                        document.getElementById("div2").style.display="none";
                        document.getElementById("updateUser").style.display="none";
                        document.getElementById("selectUser").style.display="inline-block";
                        document.getElementById("cancel").style.display="none";
                        document.getElementById("back").style.display="inline-block";
                    }
                }
            });

        });









        $(function() {
            var newPassword = $( "#newPassword" ),
                confirmPassword = $( "#confirmPassword" ),
                allFields = $( [] ).add( newPassword ).add( confirmPassword ),
                tips = $( ".validateTips" );

            function updateTips( t ) {
                tips
                    .text( t )
                    .addClass( "ui-state-highlight" );
                setTimeout(function() {
                    tips.removeClass( "ui-state-highlight", 1500 );
                }, 500 );
            }

            function checkLength( o, n ) {
                if ( o.val().length ==0 ) {
                    o.addClass( "ui-state-error" );
                    updateTips( "" + n + " 不能为空" );
                    return false;
                } else {
                    return true;
                }
            }


            function check( o, n) {
                if ( o.val()!=n.val() ) {
                    o.addClass( "ui-state-error" );
                    updateTips( "新密码与确认新密码不一致" );
                    return false;
                } else if(o.val().indexOf(" ")!=-1){
                    o.addClass( "ui-state-error" );
                    updateTips( "新密码不允许含有空格" );
                    return false;
                }
                else{
                    return true;
                }
            }



            $( "#dialog-form" ).dialog({
                autoOpen: false,
                height: 378,
                width: 350,
                modal: true,
                buttons: {
                    "保存": function() {
                        var bValid = true;
                        allFields.removeClass( "ui-state-error" );

                        bValid = bValid && checkLength( newPassword, "新密码", 3, 160 );
                        bValid = bValid && checkLength( confirmPassword, "确认密码", 3, 80 );
                        bValid=  bValid && check(newPassword,confirmPassword);


                        if ( bValid ) {
                            $("#updateUser").click();
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




        });






    });


    function editPassword(btn) {
        document.getElementById("staffNumber").value =btn.parentElement.parentElement.cells[1].innerHTML;
        document.getElementById("name").value =btn.parentElement.parentElement.cells[2].innerHTML;
        document.getElementById("role2").value =btn.parentElement.childNodes[0].innerHTML;
        document.getElementById("oldPassword").value =btn.parentElement.childNodes[1].innerHTML;
        $( "#dialog-form" ).dialog( "open" );
    };



    function editRole(btn) {
        document.getElementById("loading").style.display="block";
        document.getElementById("updateUser").style.display="inline-block";
        document.getElementById("selectUser").style.display="none";
        document.getElementById("div1").style.display="none";
        document.getElementById("div2").style.display="block";
        document.getElementById("cancel").style.display="inline-block";
        document.getElementById("back").style.display="none";

        document.getElementById("staffNumber").value = btn.parentElement.parentElement.cells[1].innerHTML;
        document.getElementById("name").value = btn.parentElement.parentElement.cells[2].innerHTML;
        document.getElementById("role2").value = btn.parentElement.childNodes[0].innerHTML;
        document.getElementById("oldPassword").value = btn.parentElement.childNodes[1].innerHTML;

        // alert(document.getElementById("role2").value);
        var user = {
            "role": document.getElementById("role2").value,
        };


        $.ajax({
            type: "POST",//提交请求的方式
            cache: true,//是否有缓存
            url:"${pageContext.request.contextPath}/Servlet2",//访问servlet的路径
            dataType: "json",//没有这个，将把后台放会的json解析成字符串
            data: user,//把内容序列化
            async: true,//是否异步
            error: function (request) {//请求出错
                document.getElementById("loading").style.display="none";

                alert("出错");
            },
            success: function (data) {//获得返回值
                document.getElementById("loading").style.display="none";
                var tables=data.message;

                $("#role tbody").remove();
                $("#newRole tbody").remove();
                // var role=document.getElementById("role2").value;



                document.getElementById("div").style.display = "block";

                if (tables.length != 0) {


                    for (var i = 0; i < tables.length; i++) {


                        var tbody = document.createElement("tbody");

                        var tr = document.createElement("tr");
                        var td1 = document.createElement("td");
                        var td2 = document.createElement("td");

                        var td3 = document.createElement("td");
                        var td4 = document.createElement("td");

                        td1.innerHTML = tables[i].database;
                        td2.innerHTML = tables[i].tablename;
                        td3.innerHTML = tables[i].remark;
                        td4.innerHTML = tables[i].ps;

                        tbody.appendChild(tr);
                        tr.appendChild(td1);
                        tr.appendChild(td2);
                        tr.appendChild(td3);
                        tr.appendChild(td4);
                        var tab = document.getElementById("role");
                        tab.appendChild(tbody);
                        tab.style.visibility = "visible";






                    }
                }


                if (tables.length != 0) {

                    for (var i = 0; i < tables.length; i++) {


                        var tbody = document.createElement("tbody");

                        var tr = document.createElement("tr");
                        var td1 = document.createElement("td");
                        var td2 = document.createElement("td");

                        var td3 = document.createElement("td");

                        var td4 = document.createElement("td");
                        var td5 = document.createElement("td");
                        td1.innerHTML = tables[i].database;
                        td2.innerHTML = tables[i].tablename;
                        td3.innerHTML = tables[i].remark;
                        td4.innerHTML = tables[i].ps;
                        td5.innerHTML = "<span style='display: none'>"+tables[i].tableId+"</span><button type='button'   onclick='deleteRole(this)'>删除</button>";


                        tbody.appendChild(tr);
                        tr.appendChild(td1);
                        tr.appendChild(td2);
                        tr.appendChild(td3);
                        tr.appendChild(td4);
                        tr.appendChild(td5);
                        var tab = document.getElementById("newRole");
                        tab.appendChild(tbody);
                        tab.style.visibility = "visible";


                        altRows("role");

                        altRows("newRole");



                    }
                }

            }
        });





    }

    function deleteUser(btn) {
        if (!confirm("确认删除此用户吗")) {
            return;
        }
        document.getElementById("loading").style.display="none";

        var staffNumber = btn.parentElement.parentElement.cells[1].innerHTML;
        document.getElementById("loading").style.display="block";


        var user = {
            "staffNumber": staffNumber,
            "action": "deleteUser"
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
                window.location.href="${pageContext.request.contextPath}/administrator/editUser.jsp";
            }
        });

    };

    function deleteRole(btn) {

        if(!confirm("确认删除此权限吗")){
            return;
        }
        var td = btn.parentElement;
        var tr=td.parentElement;
        //alert(td.childNodes[0].innerHTML);
        var tableId=td.childNodes[0].innerHTML;
        $(tr).remove();


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

            for(var i = 1; i < rows.length; i++){
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
        font-size:11px;
        color:#333333;
        border-width: 1px;
        border-color: #a9c6c9;
        border-collapse: collapse;
        font-size: 15px;
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



<style>
    #dialog-form { font-size: 62.5%; }
    /*label, input { display:block; }*/
    label{display: block}
    input.text { margin-bottom:12px; width:95%; padding: .4em; }
    fieldset { padding:0; border:0; margin-top:25px; }
    h1 { font-size: 1.2em; margin: .6em 0; }
    .ui-dialog .ui-state-error { padding: .3em; }
    .validateTips { border: 1px solid transparent; padding: 0.3em; }
</style>

<body background="../bakground3.jpg" style="background-size: cover;background-repeat: no-repeat" >
<span style="float: left;font-size: 40px;color: white;font-family: Calibri;font-weight: bolder">TTi Universal Data Upload System</span>
<img id="loading" src="../loading.gif" style="display: none;position: fixed;margin:auto;left:0;right:0;top:0;bottom:0;z-index: 9999">




<div id="dialog-form" title="修改密码" >
    <p class="validateTips"></p>
    <form>
        <fieldset>
            <input type="password" style="display: none">
            <label for="newPassword">新密码</label>
            <input type="password" name="newPassword" id="newPassword" class="text ui-widget-content ui-corner-all">
            <label for="confirmPassword">确认新密码</label>
            <input type="password" name="confirmPassword" id="confirmPassword" class="text ui-widget-content ui-corner-all">
        </fieldset>
    </form>
</div>





    <div style="text-align: right"><button id="updateUser" type="button" style="display: none">保存</button><button type="button" id="selectUser">查询</button>
        <button id="cancel" type="button" style="display: none">取消</button> <a href="administrator.jsp" id="back">返回</a>
    </div>

<br><br>
<div id="div1">

    <div style="width: 900px;margin: auto">
        请输入查询条件<br><hr style="color:yellow"/>


        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;工号：<input id="sN" type="text" placeholder="工号"><br><br>
        查询结果<br><hr style="color:yellow"/>
        <div id="result" style="display: none">


            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第<span id="currentPage"></span>/<span id="allPage"></span>页
            <button type="button" id="firstPage">首页</button>

            <button type="button" id="previousPage">上一页</button>

            <button type="button" id="nextPage">下一页</button>

            <button type="button" id="lastPage">尾页</button>
            <br><br>
            <table border="1" class="altrowstable" width="700px" id="user" style="visibility: hidden;margin: auto">
                <thead>
                <tr>
                    <th style="width: 50px">序号</th>
                    <th style="width: 50px">工号</th>
                    <th style="width: 80px">姓名</th>
                    <th>创建时间</th>
                    <th>更新时间</th>
                    <th style="width: 100px" >操作1</th>
                    <th style="width: 100px">操作2</th>
                    <th style="width: 100px">操作3</th>
                </tr></thead>

            </table>

        </div>






        <div id="div" style="display: none">



            <%--角色(role)<div id="role"></div>--%>




            <%--<div style="margin: auto;float: left;margin-top: 3px;">--%>
                <%--数据库<select name="database" id="database" >--%>
                <%--<option value="" style="display: none">--请选择--</option>--%>
                <%--<option value="" id="blank">--无权限--</option>--%>
            <%--</select>--%>
            <%--</div>--%>

            <%--<div style="display: none;" id="table_function">--%>
                <%--表名称<select id="table2" >--%>
                <%--<option value="" style="display: none">--请选择--</option>--%>
                <%--<option value="" id="blank_table">--无功能--</option>--%>
            <%--</select>--%>
                <%--<button id="addRole" type="button">添加权限</button>--%>
            <%--</div>--%>



        </div>
    </div>


</div>







    <div id="div2" style="display: none;width: 900px;margin: auto">
    
    
        员工信息<br><hr style="color:yellow"/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;工号：<input id="staffNumber" type="text" placeholder="工号" disabled>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;姓名：<input id="name" type="text" placeholder="姓名" disabled><br><br>
    
        已有权限<br><hr style="color:yellow"/>
    
        <table border="1" class="altrowstable" width="450px" id="role" style="visibility: hidden;margin: auto">
            <thead>
            <tr>
                <th>数据库</th>
                <th>数据库表名称</th>
                <th>上传功能名称</th>
                <th>上传功能备注</th>
            </tr></thead>
        </table>
        <br>
        分配权限<br><hr style="color:yellow"/>
        <div style="margin-top: 3px;float: left">
            <span style="visibility: hidden">数据库</span>数据库：<select name="database" id="database" style="width: 90px">
            <option value="" id="pleaseSelect" style="display: none">--请选择--</option>
            <option value="" id="blank">--无权限--</option>
        </select>
        </div>
        <div style="visibility: hidden;margin-top: 11px" id="table_function" >
            <span style="visibility: hidden">k</span>上传功能名称：<select id="table2"  >
            <option value="" style="display: none">--请选择--</option>
            <option value="" id="blank_table">--无功能--</option>
        </select>
            <br><br>
            上传功能备注：<input id="ps" type="text" readonly style="background-color: lightgrey"><span style="visibility: hidden">k</span>
            数据库表名称：<input id="tablename" type="text" disabled>
            <button id="addRole" type="button" style="margin-left: 10px">添加</button>
        </div>
    
    
        <table border="1" class="altrowstable" width="450px" id="newRole" style="visibility: hidden;margin: auto">
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

<div style="display:none;">

    密码<input id="oldPassword" type="text" readonly><br>
    权限<input id="role2" type="text" readonly><br>
    <input type="text" id="tableId">
</div>
</body>
</html>
