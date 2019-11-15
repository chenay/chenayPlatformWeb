package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter",urlPatterns = "/*")
public class Filter implements javax.servlet.Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest servletRequest=(HttpServletRequest)req;
        HttpServletResponse servletResponse=(HttpServletResponse)resp;
        HttpSession session=servletRequest.getSession();

        String path=servletRequest.getRequestURI();
        System.out.println("path"+path);

        String basePath= servletRequest.getContextPath();
//        System.out.println("basePath:  "+basePath);

        String staffNumber=(String)session.getAttribute("staffNumber") ;
        String role=(String)session.getAttribute("role") ;
        if(path.contains("/login.jsp") || path.contains("/ServletLogin") || path.contains("/tti2.png") ||
                path.contains("/tti1.jpg") || path.contains("/login.css")){
            chain.doFilter(req, resp);
            return;
        }

        if(staffNumber==null||"".equals(staffNumber)){

            servletResponse.sendRedirect(basePath+"/login.jsp");
        }else {
            if(role.equals("admin")){
                if(path.contains("/user/")){
                    System.out.println(path.indexOf("/user"));
                    ((HttpServletResponse) resp).sendRedirect(basePath+"/administrator/administrator.jsp");
                }else {
                    chain.doFilter(req, resp);
                }
            }else {
                if(path.contains("/administrator/")){
                    ((HttpServletResponse) resp).sendRedirect(basePath+"/user/uploadUser.jsp");
                }else {
                    chain.doFilter(req, resp);
                }
            }
            //chain.doFilter(req, resp);
        }


    }

    public void init(FilterConfig config) throws ServletException {

    }

}
