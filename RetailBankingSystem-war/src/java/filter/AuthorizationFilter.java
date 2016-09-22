package filter;

import ejb.infrastructure.entity.Role;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
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
import managedbean.infrastructure.EmployeeAccountManagedBean;
import managedbean.infrastructure.EmployeeLoginManagedBean;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"*.xhtml"}, dispatcherTypes = {DispatcherType.REQUEST})

public class AuthorizationFilter implements Filter {

    @EJB
    private EmployeeAdminSessionBeanLocal adminSessionBeanLocal;
    private FilterConfig filterConfig = null;

    public AuthorizationFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {

            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession httpSession = req.getSession(true);
            EmployeeLoginManagedBean login = (httpSession != null) ? (EmployeeLoginManagedBean) httpSession.getAttribute("employeeLoginManagedBean") : null;
            EmployeeAccountManagedBean account = (httpSession != null) ? (EmployeeAccountManagedBean) httpSession.getAttribute("employeeAccountManagedBean") : null;

            Boolean isLogin = (httpSession.getAttribute("isLogin") != null) ? ((Boolean) httpSession.getAttribute("isLogin")) : (false);
//            System.out.println("*** Authorization filter: - find User: " + user.getEmployeeName());
            String requestServletPath = req.getServletPath();

//            String path = req.getRequestURI().substring(req.getContextPath().length());
//            System.out.println("*** Authorization filter: - path:" + path);
            int roleId = 1;
            Role CEO = adminSessionBeanLocal.findRole(Long.valueOf(roleId));
            System.out.println("*** Authorization filter: find role name" + CEO.getRoleName());

            System.out.println("*** Authorization filter: path " + requestServletPath);
            System.out.println("********** SecurityFilter.doFilter: " + requestServletPath.equals("/employee/login.xhtml"));
            System.out.println("********** SecurityFilter.doFilter: " + requestServletPath.equals("/employee/changePassword.xhtml"));
            if (requestServletPath.equals("/web/internalSystem/infrastructure/employeeLogin.xhtml")) {
                System.err.println("********** SecurityFilter.doFilter: Checking login and access rights control");
//                Boolean isLogin = (httpSession.getAttribute("employee") != null) ? ((Boolean) httpSession.getAttribute("employee")) : (false);

                if (login != null && !login.isLoggedIn()) {

                    System.out.println("*** Authorization filter: not log in!! ");
                    req.getRequestDispatcher("/employee/login.xhtml").forward(request, response);
                }
//                    else {
//
//                    System.out.println("*** Authorization filter: logged in!! ");
//                    Employee user = (Employee) httpSession.getAttribute("employee");
//                    System.out.println("*** Authorization filter: user had role " + user.hasRole(CEO));
//                    if (!user.hasRole(CEO)) {
//                        System.out.println("*** Authorization filter: not Authorized!! redirect ");
//                        req.getRequestDispatcher("/employee/changePassword.xhtml").forward(request, response);
//                    } else {
//                        System.out.println("*** Authorization filter: user Authorized!!");
//                        chain.doFilter(request, response);
////                        req.getRequestDispatcher("/employee/userAccoountManagement.xhtml").forward(request, response);
//                    }

                chain.doFilter(request, response);
//                }
            }///employee/login.xhtml end if
            else {
                System.err.println("********** SecurityFilter.doFilter: Checking login and access rights control");

                if (account != null && !account.getLoggedIn()) {
                    System.err.println("********** SecurityFilter.doFilter: not log in");
                    req.getRequestDispatcher("/web/internalSystem/infrastructure/employeeMainPage.xhtml").forward(request, response);
                } else {

                    System.err.println("********** SecurityFilter.doFilter:log in already");
                    chain.doFilter(request, response);
                }
            }///employee/changePassword.xhtml end if
//            else if (requestServletPath.equals("/employee/userAccountManagement.xhtml")) {
//                System.err.println("********** SecurityFilter.doFilter: changepassword");
//                chain.doFilter(request, response);
//            }///employee/userAccountManagement.xhtml end if
//            else if (requestServletPath.equals("/employee/createAccount.xhtml")) {
//                System.err.println("********** SecurityFilter.doFilter: createAccount");
//                chain.doFilter(request, response);
//            }///employee/userAccountManagement.xhtml end if
//            else if (requestServletPath.equals("/employee/editAccount.xhtml")) {
//                System.err.println("********** SecurityFilter.doFilter: editAccount");
//                chain.doFilter(request, response);
//            }///employee/userAccountManagement.xhtml end if
//            else if (requestServletPath.equals("/employee/employeeMainPage.xhtml")) {
//                System.err.println("********** SecurityFilter.doFilter: redirect to employee main page");
//                chain.doFilter(request, response);
//            }///employee/employeeMainPage.xhtml end if

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }
}
