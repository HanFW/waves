//package jsf.filter;
//
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
//import javax.servlet.http.HttpSession;
//
//
//
//@WebFilter(filterName = "LoginFilter", urlPatterns = {"*.xhtml"}, dispatcherTypes = {DispatcherType.REQUEST})
//
//public class LoginFilter implements Filter
//{
//    private FilterConfig filterConfig = null;
//    
//    
//    
//    public LoginFilter()
//    {
//    }
//
//    
//    
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException 
//    {
//    }
//
//    
//    
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
//    {    
//        try 
//        {
////            System.setSecurityManager(new java.rmi.RMISecurityManager());
//            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
//            HttpSession httpSession = httpServletRequest.getSession(true);
//            String requestServletPath = httpServletRequest.getServletPath();
//            
//            System.out.println("*** Login filter: - path: "+requestServletPath);
//            if(requestServletPath.equals("employee/changPassword.xhtml"))
//            {
//                System.err.println("********** SecurityFilter.doFilter: Checking login and access rights control");
//                
//                Boolean isLogin = (httpSession.getAttribute("employee") != null)?((Boolean)httpSession.getAttribute("employee")):(false);
//                
//                if(!isLogin)
//                    
//                {
//                    httpServletRequest.getRequestDispatcher("/employee/login.xhtml?faces-redirect=true").forward(request, response);
//                }
//                
//                chain.doFilter(request, response);
//            }
//            
//            
//            else
//            {
//                chain.doFilter(request, response);
//            }
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
//
//    
//    
//    @Override
//    public void destroy()
//    {
//    }
//}
