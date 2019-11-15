package upload;

import jdbc.JdbcTest2;
import user.Table;
import user.User;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Servlet2",urlPatterns = "/Servlet2")
public class Servlet2 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user=new User();
        if(request.getParameter("role")!=null){
            user.StaffNumber="allFunction";
            user.Role=request.getParameter("role");
            JdbcTest2 jdbcTest2=new JdbcTest2();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message",jdbcTest2.selectTable(user));

            String jsonString = jsonObject.toString();
            //回写给浏览器
            response.setContentType("application/javascript;charset=utf-8");
            response.getWriter().write(jsonString);

        }else {
            HttpServletRequest servletRequest=(HttpServletRequest)request;
            HttpSession session=servletRequest.getSession();
            user.Role=(String)session.getAttribute("role");
            JdbcTest2 jdbcTest2=new JdbcTest2();
            List<Table> tables=jdbcTest2.selectTable(user);

            String jsonString = JSONObject.toJSONString(tables);
            System.out.println(jsonString);
            //回写给浏览器
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(jsonString);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
