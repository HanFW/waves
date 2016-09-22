package filter;

//import ejb.infrastructure.entity.Role;
import java.io.IOException;
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

@WebFilter(filterName = "OnlineBankingFilter", urlPatterns = {"/web/onlineBanking/*"}, dispatcherTypes = {DispatcherType.REQUEST})

public class OnlineBankingFilter implements Filter {

    private FilterConfig filterConfig = null;

    public OnlineBankingFilter() {
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

            String requestServletPath = req.getServletPath();

            System.out.println("~");
            System.out.println("~~~~~~ OnlineBankingFilter: " + requestServletPath + " ~~~~~~");

            if (requestServletPath.equals("/web/onlineBanking/infrastructure/customerForgetPIN.xhtml")
                    || requestServletPath.equals("/web/onlineBanking/infrastructure/customerForgetUserId.xhtml")
                    || requestServletPath.equals("/web/onlineBanking/infrastructure/customerLoggedOut.xhtml")
                    || requestServletPath.equals("/web/onlineBanking/infrastructure/customerLogin.xhtml")
                    || requestServletPath.equals("/web/onlineBanking/infrastructure/customerRetrieveUserId.xhtml")) {
                // customer not logged in: redirect directly
                System.out.println("~~~~~~ OnlineBankingFilter: " + requestServletPath + " ~~~ page accessible before customer login");
                chain.doFilter(request, response);
            } else {
                // customer want to get access to online banking pages
                System.out.println("~~~~~~ OnlineBankingFilter: " + requestServletPath + " ~~~ page accessible after customer login");
                if (httpSession.getAttribute("customer") == null) {
                    // customer not logged in: ask customer to login
                    System.out.println("~~~~~~ OnlineBankingFilter: " + requestServletPath + " ~~~ customer not logged in");
                    req.getRequestDispatcher("/web/onlineBanking/infrastructure/customerLoggedOut.xhtml").forward(request, response);
                } else {
                    // customer logged in: check if customer is verified by OTP
                    System.out.println("~~~~~~ OnlineBankingFilter: " + requestServletPath + " ~~~ customer logged in");

                    if (requestServletPath.equals("/web/onlineBanking/OTP")) {
                        System.out.println("~~~~~~ OnlineBankingFilter: " + requestServletPath + " ~~~ pages request for OTP: " + httpSession.getAttribute("beforeOTP"));
                        req.getRequestDispatcher(httpSession.getAttribute("beforeOTP").toString()).forward(request, response);
                    } else if (requestServletPath.equals("/web/onlineBanking/customer/customerChangePIN.xhtml")
                            || requestServletPath.equals("/web/onlineBanking/deposit/customerAddRecipient.xhtml")
                            || requestServletPath.equals("/web/onlineBanking/deposit/customerAddRecipientDone.xhtml")
                            || requestServletPath.equals("/web/onlineBanking/deposit/customerCloseAccount.xhtml")
                            || requestServletPath.equals("/web/onlineBanking/deposit/customerDailyTransferLimit.xhtml")
                            || requestServletPath.equals("/web/onlineBanking/deposit/customerDeleteAccount.xhtml")
                            || requestServletPath.equals("/web/onlineBanking/deposit/customerDeleteRecipient.xhtml")
                            || requestServletPath.equals("/web/onlineBanking/deposit/customerOneTimeTransfer.xhtml")
                            || requestServletPath.equals("/web/onlineBanking/deposit/customerToMyAccount.xhtml")
                            || requestServletPath.equals("/web/onlineBanking/deposit/customerToOthersAccount.xhtml")
                            || requestServletPath.equals("/web/onlineBanking/deposit/customerTransferDone.xhtml")) {

                        // customer need to get verified by OTP
                        System.out.println("~~~~~~ OnlineBankingFilter: " + requestServletPath + " ~~~ page acessible with OTP");

                        if (httpSession.getAttribute("isVerified") == null) {
                            // customer not verified
                            System.out.println("~~~~~~ OnlineBankingFilter: " + requestServletPath + " ~~~ not verified with OTP");
                            httpSession.setAttribute("beforeOTP", requestServletPath);
                            req.getRequestDispatcher("/web/onlineBanking/infrastructure/customerOTP.xhtml").forward(request, response);
                        } else {
                            //customer has alredy been verified by OTP
                            System.out.println("~~~~~~ OnlineBankingFilter: " + requestServletPath + " ~~~ verified with OTP");
                        }
                    }
                }

                chain.doFilter(request, response);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }
}
