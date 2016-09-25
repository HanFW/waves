package filter;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.EmployeeAuthorizationFilterSessionBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "InternalSystemFilter", urlPatterns = {"/web/internalSystem/*"}, dispatcherTypes = {DispatcherType.REQUEST})

public class InternalSystemFilter implements Filter {

    private FilterConfig filterConfig = null;

    @EJB
    private EmployeeAuthorizationFilterSessionBeanLocal employeeAuthorizedFilterSessionBeanLocal;

    public InternalSystemFilter() {
    }
//

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession httpSession = req.getSession(true);

            String requestServletPath = req.getServletPath();

            System.out.println("*** Authorization filter: path " + requestServletPath);

            if (!requestServletPath.equals("/web/internalSystem/infrastructure/employeeLogin.xhtml") &&
                        !requestServletPath.equals("/web/internalSystem/infrastructure/employeeLogout.xhtml")) {
                System.err.println("********** SecurityFilter.doFilter: Checking login and access rights control");
                Boolean isLogin;

                if (httpSession.getAttribute("employee") != null) {
                    isLogin = true;
                } else {
                    isLogin = false;
                }
                System.out.println("Check log in: " + isLogin);

                if (!isLogin) {
                    System.err.println("********** SecurityFilter.doFilter: not log in");
                    req.getRequestDispatcher("/web/index.xhtml").forward(request, response);
                } else {
                    //Authentication
                    System.err.println("********** SecurityFilter.doFilter:log in already");
                    if (!requestServletPath.equals("/web/internalSystem/infrastructure/employeeMainPage.xhtml") && 
                        !requestServletPath.equals("/web/internalSystem/infrastructure/employeeChangePassword.xhtml")  &&
                        !requestServletPath.equals("/web/internalSystem/infrastructure/employeeForgetPassword.xhtml")   ) {
                        //Authorization
                        System.out.println("********** SecurityFilter.path "+requestServletPath);
                        Employee user = (Employee) httpSession.getAttribute("employee");
                        if (!isAuthorized(user, requestServletPath)) {
                            req.getRequestDispatcher("/web/internalSystem/infrastructure/employeeMainPage.xhtml").forward(request, response);
                        } else {
                            chain.doFilter(request, response);
                        }
                    }//pages which do not need authorization check
                    else {
                        chain.doFilter(request, response);
                    }
                }
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }

    public Employee getUserIdentitity() {
        FacesContext context = FacesContext.getCurrentInstance();
        Employee user = (Employee) context.getExternalContext().getSessionMap().get("employee");
        return user;
    }

    public boolean isAuthorized(Employee user, String requestServletPath) {

        boolean isAuthorized = employeeAuthorizedFilterSessionBeanLocal.authorizationCheck(user, requestServletPath);

        return isAuthorized;

    }
}
