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

@WebServlet(name = "ServletUser",urlPatterns = "/ServletUser")
public class ServletUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JdbcTest2 jdbcTest2=new JdbcTest2();
        if(request.getParameter("action").equals("selectRole")){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message",jdbcTest2.selectRole());
            String string = jsonObject.toString();
            //response.setContentType("application/json;charset=utf-8");　　　　 //1
            //response.setContentType("text/json;charset=utf-8");　　　　　　　　 //2
            //response.setContentType("text/javascript;charset=utf-8");　　　　　//3
            response.setContentType("application/javascript;charset=utf-8");  //4
            response.getWriter().write(string);
        }else if(request.getParameter("action").equals("createUser")){
            User user=new User();
            user.StaffNumber=request.getParameter("staffNumber");
            user.Name=request.getParameter("name");
            user.Role=request.getParameter("role");
            user.Password=request.getParameter("password");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message",jdbcTest2.insertUser(user));

            String string = jsonObject.toString();
            //response.setContentType("application/json;charset=utf-8");　　　　 //1
            //response.setContentType("text/json;charset=utf-8");　　　　　　　　 //2
            //response.setContentType("text/javascript;charset=utf-8");　　　　　//3
            response.setContentType("application/javascript;charset=utf-8");  //4
            response.getWriter().write(string);
        }else if(request.getParameter("action").equals("selectUser")){
            User user=new User();
            user.StaffNumber=request.getParameter("staffNumber");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message",jdbcTest2.selectUser(user));
            String string = jsonObject.toString();
            //response.setContentType("application/json;charset=utf-8");　　　　 //1
            //response.setContentType("text/json;charset=utf-8");　　　　　　　　 //2
            //response.setContentType("text/javascript;charset=utf-8");　　　　　//3
            response.setContentType("application/javascript;charset=utf-8");  //4
            response.getWriter().write(string);
        }else if(request.getParameter("action").equals("updateUser")){
            HttpServletRequest servletRequest=(HttpServletRequest) request;
            HttpSession session = servletRequest.getSession();
            User user=new User();
            user.StaffNumber=(String)session.getAttribute("staffNumber");
            user.Name=(String)session.getAttribute("name");
            user.Role=(String)session.getAttribute("role");
            user.Password=request.getParameter("password");

            JSONObject jsonObject = new JSONObject();
            if(jdbcTest2.updateUser(user).equals("success")) {
                jsonObject.put("message","success");

                session.setAttribute("password",user.Password);
            }else {
                jsonObject.put("message","error");
            }
            String string = jsonObject.toString();
            //response.setContentType("application/json;charset=utf-8");　　　　 //1
            //response.setContentType("text/json;charset=utf-8");　　　　　　　　 //2
            //response.setContentType("text/javascript;charset=utf-8");　　　　　//3
            response.setContentType("application/javascript;charset=utf-8");  //4
            response.getWriter().write(string);
        }else if(request.getParameter("action").equals("updateUserRole")){
            HttpServletRequest servletRequest=(HttpServletRequest) request;
            HttpSession session = servletRequest.getSession();
            User user=new User();
            user.StaffNumber=request.getParameter("staffNumber");
            user.Name=request.getParameter("name");
            user.Role=request.getParameter("role");
            user.Password=request.getParameter("password");

            JSONObject jsonObject = new JSONObject();
            if(jdbcTest2.updateUser(user).equals("success")) {
                jsonObject.put("message","success");

                session.setAttribute("password",user.Password);
            }else {
                jsonObject.put("message","error");
            }
            String string = jsonObject.toString();
            //response.setContentType("application/json;charset=utf-8");　　　　 //1
            //response.setContentType("text/json;charset=utf-8");　　　　　　　　 //2
            //response.setContentType("text/javascript;charset=utf-8");　　　　　//3
            response.setContentType("application/javascript;charset=utf-8");  //4
            response.getWriter().write(string);
        }else if(request.getParameter("action").equals("deleteUser")){
            User user=new User();
            user.StaffNumber=request.getParameter("staffNumber");
            JSONObject jsonObject = new JSONObject();
            if(jdbcTest2.deleteUser(user).equals("success")) {
                jsonObject.put("message","success");
            }else {
                jsonObject.put("message","error");
            }
            String string = jsonObject.toString();
            //response.setContentType("application/json;charset=utf-8");　　　　 //1
            //response.setContentType("text/json;charset=utf-8");　　　　　　　　 //2
            //response.setContentType("text/javascript;charset=utf-8");　　　　　//3
            response.setContentType("application/javascript;charset=utf-8");  //4
            response.getWriter().write(string);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
