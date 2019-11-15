package upload;

import jdbc.JdbcTest2;
import user.Database;
import user.Table;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Servlet3",urlPatterns = "/Servlet3")
public class Servlet3 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JdbcTest2 jdbcTest2=new JdbcTest2();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "success");
        if(request.getParameter("action").equals("update")){
            Table table=new Table();

            table.tableId=Integer.parseInt(request.getParameter("tableId"));
            table.attributes=request.getParameter("attributes");
            table.type=request.getParameter("type");
            table.nullable=request.getParameter("nullable");
            table.remark=request.getParameter("remark");
            table.length=request.getParameter("length");
            table.ps=request.getParameter("ps");
            jsonObject.put("message", jdbcTest2.update(table));

        }else if(request.getParameter("action").equals("insert3")){
            Table table=new Table();
            table.tablename=request.getParameter("tablename");
            table.attributes=request.getParameter("attributes");
            table.type=request.getParameter("type");
            table.nullable=request.getParameter("nullable");
            table.remark=request.getParameter("remark");
            table.database=request.getParameter("database");
            table.length=request.getParameter("length");
            table.ps=request.getParameter("ps");
            jsonObject.put("message", jdbcTest2.insert3(table));
        }else if(request.getParameter("action").equals("selectFuncionTable")){
            Database database=new Database();
            database.setAlias(request.getParameter("alias"));
            jsonObject.put("message1",jdbcTest2.selectFuncionTable(database));
        } else{
            Table table=new Table();
            table.tableId=Integer.parseInt(request.getParameter("tableId"));
            jdbcTest2.delete(table);
        }





        String string = jsonObject.toString();
        System.out.println(string);
        //response.setContentType("application/json;charset=utf-8");　　　　 //1
        //response.setContentType("text/json;charset=utf-8");　　　　　　　　 //2
        //response.setContentType("text/javascript;charset=utf-8");　　　　　//3
        response.setContentType("application/javascript;charset=utf-8");  //4
        response.getWriter().write(string);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
