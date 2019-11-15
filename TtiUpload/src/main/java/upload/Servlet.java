package upload;


import user.Table;
import user.User;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import read.ReadCSV;
import read.ReadXLS;
import read.ReadXLSX;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


//@WebServlet(name = "Servlet",urlPatterns = "/")
public class Servlet extends javax.servlet.http.HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String message=null;
        HttpServletRequest servletRequest=(HttpServletRequest) request;
        HttpSession session = servletRequest.getSession();
        session.getAttribute("staffNumber");
        User user=new User();
        user.StaffNumber=(String)session.getAttribute("staffNumber");
        try {
            FileItemFactory factory = new DiskFileItemFactory();
            // 文件上传核心工具类
            ServletFileUpload upload = new ServletFileUpload(factory);
            Table table=null;
            List<String[]> lists=new ArrayList<String[]>();
//            upload.setFileSizeMax(10 * 1024 * 1024); // 单个文件大小限制
//            upload.setSizeMax(50 * 1024 * 1024); // 总文件大小限制
            upload.setHeaderEncoding("UTF-8"); // 对中文文件编码处理
            if (ServletFileUpload.isMultipartContent(request)) {
                List<FileItem> list = upload.parseRequest(request);
                // 遍历
                String [] type=null;
                FileItem fileItem=null;
                for (FileItem item : list) {
                    if (!item.isFormField()) {
                        fileItem=item;

                    } else {
                        JSONObject jsonObject = JSONObject.parseObject(item.getString());
                        table=JSONObject.toJavaObject(jsonObject,Table.class);


                        String data = table.remark;//乱码字符串
                        byte source [] = data.getBytes("iso8859-1");//得到客户机提交的原始数据

                        data = new String (source,"UTF-8");
                        table.remark=data;
                        System.out.println(table);
                    }
                }
                String[] split = fileItem.getName().split("\\.");
                System.out.println(split[split.length-1]+"   "+split.length);
                int length=0;
                try{
                    System.out.println(fileItem.getName());
                    if ( "xlsx".equals(split[split.length-1]) ){
                        File file=File.createTempFile("temp",".xlsx");
                        fileItem.write(file);
                        System.out.println("-- 程序开始 --");
                        long time_1 = System.currentTimeMillis();
                        session.removeAttribute("cancel");
                        ReadXLSX readXLSX=new ReadXLSX();
                        readXLSX.request=request;
                        message=readXLSX.processOneSheet(file,table,user,fileItem.getName());

                        long time_2 = System.currentTimeMillis();
                        System.out.println("-- 程序结束 --");
                        System.out.println("-- 耗时 --"+(time_2 - time_1)+"ms");
                        file.delete();
                    }else if("xls".equals(split[split.length-1])){
                        ReadXLS readXLS=new ReadXLS();
                        session.removeAttribute("cancel");
                        readXLS.request=request;
                        System.out.println("-- 程序开始 --");
                        long time_1 = System.currentTimeMillis();
                        message=readXLS.readXLS(fileItem.getInputStream(),table,user,fileItem.getName());
                        long time_2 = System.currentTimeMillis();
                        System.out.println("-- 程序结束 --");
                        System.out.println("-- 耗时 --"+(time_2 - time_1)+"ms");
                    }else if("csv".equals(split[split.length-1])){
                        ReadCSV readCSV=new ReadCSV();
                        session.removeAttribute("cancel");
                        readCSV.request=request;
                        System.out.println("-- 程序开始 --");
                        long time_1 = System.currentTimeMillis();
                        message=readCSV.readCSV(fileItem.getInputStream(),table,user,fileItem.getName());
                        long time_2 = System.currentTimeMillis();
                        System.out.println("-- 程序结束 --");
                        System.out.println("-- 耗时 --"+(time_2 - time_1)+"ms");
                    }else{
                        message="请确保文件上传类型正确";
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    message="请确保上传文件符合要求";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(message);
//        request.setAttribute("message",message);
//        RequestDispatcher requestDispatcher=request.getRequestDispatcher("/user/uploadUser.jsp");
//        requestDispatcher.forward(request,response);
        response.sendRedirect(request.getContextPath()+"/user/uploadUser.jsp?message="+URLEncoder.encode(message, "utf-8"));
    }

    /**
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
//        String v=request.getParameter("education");
//        System.out.println(v);
    }
}
