//package filter;
//
//import ejb.infrastructure.entity.Role;
//import java.io.IOException;
//import javax.servlet.DispatcherType;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

//
//@WebFilter(filterName = "InternalSystemFilter", urlPatterns = {"/web/internalSystem/*"}, dispatcherTypes = {DispatcherType.REQUEST})
//
//public class InternalSystemFilter implements Filter {
//
//   
//    private FilterConfig filterConfig = null;
//
//    public InternalSystemFilter() {
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        try {
//            HttpServletRequest req = (HttpServletRequest) request;
//            HttpServletResponse res = (HttpServletResponse) response;
//            HttpSession httpSession = req.getSession(true);
//
//            String requestServletPath = req.getServletPath();
//
//            System.out.println("*** Authorization filter: path " + requestServletPath);
//            System.out.println("********** SecurityFilter.doFilter: " + requestServletPath.equals("/employee/login.xhtml"));
//            System.out.println("********** SecurityFilter.doFilter: " + requestServletPath.equals("/employee/changePassword.xhtml"));
//
//            if (!requestServletPath.equals("/web/internalSystem/infrastructure/employeeLogin.xhtml")) {
//                System.err.println("********** SecurityFilter.doFilter: Checking login and access rights control");
//                Boolean isLogin;
//                
//                if(httpSession.getAttribute("employee")!=null)
//                    isLogin=true;
//                else
//                    isLogin=false;
//                  System.out.println("Check log in: "+isLogin);
//
//                if (!isLogin) {
//                    System.err.println("********** SecurityFilter.doFilter: not log in");
//                    req.getRequestDispatcher("/web/index.xhtml").forward(request, response);
//                }
//
//                System.err.println("********** SecurityFilter.doFilter:log in already");
//                chain.doFilter(request, response);
//            } else {
//                chain.doFilter(request, response);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    @Override
//    public void destroy() {
//    }
//}
