package managedbean.loan.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.loan.entity.CashlineApplication;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.session.LoanManagementSessionBeanLocal;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@Named(value = "customerViewAllLoansManagedBean")
@RequestScoped

public class CustomerViewAllLoansManagedBean {

    @EJB
    private LoanManagementSessionBeanLocal loanManagementSessionBeanLocal;

    @Resource(name = "retailBankingSystemDataSource")
    private DataSource retailBankingSystemDataSource;

    private List<LoanPayableAccount> accounts;
    private List<LoanApplication> applications;
    private List<CashlineApplication> cashlines;
    private Long loanPayableAccountId;

    public CustomerViewAllLoansManagedBean() {
    }

    public Long getLoanPayableAccountId() {
        return loanPayableAccountId;
    }

    public void setLoanPayableAccountId(Long loanPayableAccountId) {
        this.loanPayableAccountId = loanPayableAccountId;
    }

    public void viewLoan(Long loanId) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("loanId", loanId);
        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/loan/customerViewLoan.xhtml?faces-redirect=true");
    }

    public List<LoanPayableAccount> getAccounts() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        accounts = loanManagementSessionBeanLocal.getLoanPayableAccountByIdentification(customer.getCustomerIdentificationNum());
        return accounts;
    }

    public List<LoanApplication> getApplications() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        applications = loanManagementSessionBeanLocal.getLoanApplicationsByIdentification(customer.getCustomerIdentificationNum());
        return applications;
    }

    public List<CashlineApplication> getCashlineApplications() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        cashlines = loanManagementSessionBeanLocal.getCashlineApplicationsByIdentification(customer.getCustomerIdentificationNum());
        return cashlines;
    }

    public void viewLoanStatement() throws ClassNotFoundException, IOException, JRException, SQLException {
        Connection connection;

        FacesContext ctx = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) ctx
                .getExternalContext().getResponse();

        InputStream reportStream = ctx.getExternalContext()
                .getResourceAsStream("/E-Statements/loanStatement.jasper");

        ServletOutputStream servletOutputStream = response.getOutputStream();
        Class.forName("com.mysql.jdbc.Driver");
        connection = retailBankingSystemDataSource.getConnection();

        ctx.responseComplete();
        response.setContentType("application/pdf");

        Map parameters = new HashMap();
        parameters.put("loanPayableAccountId", loanPayableAccountId);

        JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream,
                parameters, connection);

        connection.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }
}
