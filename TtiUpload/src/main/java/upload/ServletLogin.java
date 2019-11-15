package upload;

import jdbc.JdbcTest2;
import user.User;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ServletLogin",urlPatterns = "/ServletLogin")
public class ServletLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user=new User();
        user.StaffNumber=request.getParameter("staffNumber");
        user.Password=request.getParameter("password");

        User user1=new User();


        JdbcTest2 jdbcTest2=new JdbcTest2();
        user1=jdbcTest2.selectLoginUser(user);

        System.out.println(user1);


        if(user1!=null){
            HttpServletRequest servletRequest=(HttpServletRequest) request;
            HttpSession session = servletRequest.getSession();
            session.setAttribute("staffNumber",user1.StaffNumber);
            session.setAttribute("name",user1.Name);
            session.setAttribute("role",user1.Role);
            session.setAttribute("password",user1.Password);

        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", user1);



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
