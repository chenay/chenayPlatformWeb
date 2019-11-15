<%--
  Created by IntelliJ IDEA.
  User: BG.Ding
  Date: 2018/9/10
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<html>
<head>
    <title>用户界面</title>
</head>
<%--<script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>--%>

<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">

<script>
    window.onbeforeunload = function () {
        $.ajax({
            type: "POST",//提交请求的方式
            cache: true,//是否有缓存
            url: "${pageContext.request.contextPath}/ServletUpload",//访问servlet的路径
            dataType: "json",//没有这个，将把后台放会的json解析成字符串
            data: {
                "action": "cancel"
            },//把内容序列化
            async: false,//是否异步
            error: function (request) {//请求出错
                // alert("出错");
            },
            success: function () {//获得返回值
            }
        })
    };

    $(function () {

        $("input,textarea,select").css("border-radius", "5px");
        $("input,select").css("height", "30px");
        $("input,select").css("width", "250px");
        $("input,select").css("text-align", "center");
        // $( "input[type=text]").css("width","150px");
        $("input[type=submit], a, button").button();
        $("input[type=submit],a, button").css("font-size", "14px");

    });


    function check() {//循环所有的表单元素； 也可以用jQuery：$("#表单id")[0].elements.length-1

        if (document.getElementById('education').value == "" || document.getElementById('f1').value == "") {
            alert("当前表单不能有空项");
            return false;
        }

        if (!confirm("确认要上传吗")) {
            return false;
        }


        document.getElementById("uploading").style.display = "block";
        // document.getElementById("loading").style.display="block";
        return true;
    }

    // function clickbtn() {
    //      $("input[type=file]").click();
    // }
    //
    function change(btn) {
        // alert(btn.value);
        document.getElementById("showName").value = btn.value;
    }

    function f1Click() {
        document.getElementById("f1").click();
    }
</script>

<script type="text/javascript">
    function CountDown() {
        var i = Number(document.getElementById("i").value);
        if (i == 1000) {
            i = 0;
        }
        i++;
        document.getElementById("i").value = i;
        var ii = i % 5 + 1;
        var point = document.getElementById("uploading").innerHTML.replace(".", "").replace(".", "").replace(".", "").replace(".", "").replace(".", "");
        for (var iii = 0; iii < ii; iii++) {
            point += ".";
        }
        document.getElementById("uploading").innerHTML = point;
    }

    setInterval("CountDown()", 1000);

</script>


<body background="../bakground3.jpg" style="background-size: cover;">
<input type="text" id="i" value="0" style="display: none">

<span style="float: left;font-size: 40px;color: white;font-family: Calibri;font-weight: bolder">TTi Universal Data Upload System</span>
<img id="loading" src="../loading.gif"
     style="display: block;position: fixed;margin:auto;left:0;right:0;top:0;bottom:0;z-index: 9999">

<span id="uploading"
      style="display: none;position: fixed;margin:auto;left:840px;right:0;top: 450px;z-index: 9999;font-size: 35px;width: 600px">Uploading, please wait...</span>


<div style="text-align: right;font-size: 15px;color: white"> 用户：<%=session.getAttribute("name")%>&nbsp;&nbsp;<a
        href="editPassword.jsp"> 修改密码</a><a href="../loginout.jsp"> 退出</a></div>
<br><br>
<form action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data" method="post" onsubmit="return check()"
      style="width: 420px;margin: auto;margin-left:250px;margin-top: 50px;float: left">


    <label id="lblSelect">
        1.上传功能:<select name="education" id="education">
        <option value="" style="display: none">--请选择--</option>
        <option value="" id="blank">--无权限--</option>
    </select>
    </label>
    <br><br>
    <span style="vertical-align: top"><span style="visibility: hidden">1.<span></span></span>功能备注:</span><textarea
        id="ps" readonly style="background-color: lightgrey;width: 60%;" rows="6"></textarea>
    <br>
    <%--<span  style="visibility: hidden;color: red" > ppp</span>--%>

    <img src="arrow1.png" style="width: 100px;height: 100px;margin-left: 150px;margin-top: -5px">
    <%--<input type="file" id="clickbtn2" onclick="clickbtn()" value="浏览" style="width: 260px;">--%>


    <br>2.选择文件:
    <input type="text" id="showName" readonly style="background-color: lightgrey;margin-left: -6px;margin-top: -5px"/>
    <input type="file" name="f1" id="f1" style="display: none" onchange="change(this)"/>
    <button class="xiugaibtn" type="button" onclick="f1Click()" style="float: right">浏览</button>

    <%--<span  style="visibility: hidden;color: red" > ppp</span>--%>
    <img src="arrow1.png" style="width: 100px;height: 100px;margin-left: 150px;margin-top: -5px">


    <br>
    3.上传数据:<input type="submit" value="点击上传..." style="margin-top: -5px"/>


    <%--<button id="button" name="button">提交</button>--%>
</form>

<img src="../uploadExcel.png" style="margin-top: 130px;margin-left:100px;width: 500px;float: left">
<textarea id="message" readonly style="size: auto;display: none">${param.message }</textarea>


<br/>
</body>
<script>
    if (document.getElementById("message").value != "") {
        $("input,select").css("border-radius", "5px");
        $("input,select").css("height", "30px");
        $("input,select").css("width", "200px");
        $("input,select").css("text-align", "center");
        $("input[type=text]").css("width", "150px");
        $("input[type=submit], a, button").button();
        $("input[type=submit],a, button").css("font-size", "14px");
        //alert(document.getElementById("message").value);
        if (document.getElementById("message").value.split(",").length == 3 && document.getElementById("message").value.split(",")[0] == "success") {
            document.getElementById("uploading").style.display = "none";
            document.getElementById("loading").style.display = "none";
            alert("成功上传" + document.getElementById("message").value.split(",")[2] + "行数据");


            window.open("uploadResult.jsp?message=" + escape(document.getElementById("message").value));
        } else {
            document.getElementById("uploading").style.display = "none";
            document.getElementById("loading").style.display = "none";
            alert(document.getElementById("message").value);
        }

    }

    $(document).ready(function () {
        //===================未分页,展示所有数据===============================
        var url = "${pageContext.request.contextPath}/Servlet2";
        //=====向服务器发送post请求
        $.post(url, function (data) {
            document.getElementById("loading").style.display = "none";

            var tables = eval(data);
            var value = "";
            var s = "";
            var str = "";


            if (tables.length != 0) {
                // document.body.innerHTML="";
                // alert("您当前未有权限");

                function removeElement(_element) {
                    var _parentElement = _element.parentNode;
                    if (_parentElement) {
                        _parentElement.removeChild(_element);
                    }
                }

                // document.getElementById("blank").remove();

                removeElement(document.getElementById("blank"));
                // document.getElementById("blank").remove();
            }

            for (var i = 0; i < tables.length; i++) {
                //value='{"tablename":"'+tables[i].tablename+'","attributes":"'+tables[i].attributes+'","type":"'+tables[i].type+'","nullable":"'+tables[i].nullable+'","remark":"'+tables[i].remark+'","database":"'+tables[i].database+'"}';


                str += "<option value=\'" + JSON.stringify(tables[i]).replace(/'/g, "html-quote") + "\'>" + tables[i].remark + "</option>";
            }
            $("#education").append(str);
            // document.getElementById('education').style.display='block';


        }, "json");


        $("#education").change(function () {
            if (($(this).children('option:selected').val() == "")) {
                document.getElementById("ps").value = "";
                return;
            }
            var table = JSON.parse($(this).children('option:selected').val().replace(/html-quote/g, "'"));

            document.getElementById("ps").value = table.ps;
            //document.getElementById("ps").style.visibility="visible";
        })
    });

</script>
</html>
