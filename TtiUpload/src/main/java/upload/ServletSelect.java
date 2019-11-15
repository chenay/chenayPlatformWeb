package upload;

import jdbc.JdbcTest2;
import user.Database;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ServletSelect",urlPatterns = "/ServletSelect")
public class ServletSelect extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = new JSONObject();

        JdbcTest2 jdbcTest2=new JdbcTest2();
        Database database=new Database(request.getParameter("url"),
                request.getParameter("userName"),
                request.getParameter("password"),
                request.getParameter("alias"));


        if(jdbcTest2.selecttable(request.getParameter("tablename"),database)==null){
            jsonObject.put("message","notExist");
        }else {
            jsonObject.put("message",jdbcTest2.selecttable(request.getParameter("tablename"),database));
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
