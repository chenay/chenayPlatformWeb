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
import java.util.ArrayList;

@WebServlet(name = "Connect",urlPatterns = "/Connect")
public class Connect extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String connect=request.getParameter("connect");
        JSONObject jsonObject = new JSONObject();

        ArrayList<Database> arrayList=new ArrayList<>();

        JdbcTest2 jdbcTest2=new JdbcTest2();

        if(request.getParameter("action").equals("selectDatabase")){
            arrayList=jdbcTest2.selectDatabase();
            jsonObject.put("message", arrayList);
        }else if(request.getParameter("action").equals("deleteDatabase")){
            Database database=new Database();
            database.setAlias(request.getParameter("alias"));
            jsonObject.put("message", jdbcTest2.deleteDatabase(database));
        }else if(request.getParameter("action").equals("insertDatabase")){
            Database database=new Database();
            database.setUrl(request.getParameter("url"));
            database.setUserName(request.getParameter("userName"));
            database.setPassword(request.getParameter("password"));
            database.setAlias(request.getParameter("alias"));
            jsonObject.put("message", jdbcTest2.insertDatabase(database));
        }else if(request.getParameter("action").equals("updateDatabase")){
            Database database=new Database();
            database.setAlias(request.getParameter("alias"));
            database.setStatus(request.getParameter("status"));
            jsonObject.put("message", jdbcTest2.updateDatabase(database));
        }else if(request.getParameter("action").equals("updateDatabasePassword")){
            Database database=new Database();
            database.setAlias(request.getParameter("alias"));
            database.setUrl(request.getParameter("url"));
            database.setUserName(request.getParameter("userName"));
            database.setPassword(request.getParameter("password"));
            jsonObject.put("message", jdbcTest2.updateDatabasePassword(database));
        }




//        if(connect.equals("yes")){
//            JdbcTest2.close();
//            if(!JdbcTest2.Connection(request.getParameter("url"),request.getParameter("user"),request.getParameter("password"))){
//                jsonObject.clear();
//                jsonObject.put("message","error");
//            }
//        }
//        else {
//            JdbcTest2.close();
//        }

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
