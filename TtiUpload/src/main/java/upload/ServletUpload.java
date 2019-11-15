package upload;

import jdbc.JdbcTest2;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ServletUpload",urlPatterns = "/ServletUpload")
public class ServletUpload extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JdbcTest2 jdbcTest2=new JdbcTest2();
      String  action =request.getParameter("action");

        if(request.getParameter("action").equals("showUploadResult")){

            //String s="[\"a\",\"b\"]";
            JSONArray jsonArray=JSONArray.parseArray(request.getParameter("title"));
            List<String> title=jsonArray.toJavaList(String.class);

            //List<String> title=Arrays.asList(request.getParameter("title").split(","));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message",jdbcTest2.showUploadResult(Integer.parseInt(request.getParameter("uploadId")),
                    Integer.parseInt(request.getParameter("page")),request.getParameter("url"),request.getParameter("userName"),
                    request.getParameter("password"),title,request.getParameter("tableName")));
            String string = jsonObject.toString();
            response.setContentType("application/javascript;charset=utf-8");  //4
            response.getWriter().write(string);
        }else if(request.getParameter("action").equals("recallUpload")){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message",jdbcTest2.recallUpload(Integer.parseInt(request.getParameter("uploadId"))));
            String string = jsonObject.toString();
            response.setContentType("application/javascript;charset=utf-8");  //4
            response.getWriter().write(string);
        }
        else if(request.getParameter("action").equals("uploadCount")){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message",jdbcTest2.uploadCount(Integer.parseInt(request.getParameter("uploadId"))));

            String [] database=new String[]{jdbcTest2.url,jdbcTest2.userName,jdbcTest2.password};
            jsonObject.put("message2",database);

            int [] pageInfo=new int[]{jdbcTest2.count,jdbcTest2.page,jdbcTest2.pageCount,jdbcTest2.allPage};
            jsonObject.put("message3",pageInfo);

            jsonObject.put("message4",jdbcTest2.title);

            jsonObject.put("message5",jdbcTest2.tablename);
            String string = jsonObject.toString();
            response.setContentType("application/javascript;charset=utf-8");  //4
            response.getWriter().write(string);
        }else if(request.getParameter("action").equals("cancel")){
            HttpSession session = request.getSession();
            session.setAttribute("cancel","Y");
            System.out.println("cancel");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
