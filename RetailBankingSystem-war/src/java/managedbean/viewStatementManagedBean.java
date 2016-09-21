package managedbean;

import ejb.deposit.entity.BankAccount;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.Statement;
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
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import ejb.deposit.session.StatementSessionBeanLocal;

@Named(value = "viewStatementManagedBean")
@RequestScoped
public class viewStatementManagedBean {
    @EJB
    private StatementSessionBeanLocal statementSessionBeanLocal;
    
    @Resource(name = "retailBankingSystemDataSource")
    private DataSource retailBankingSystemDataSource;

    private String statementDate;
    private String statementType;
    private String accountDetails;
    private Long customerBasicId;
    
    private ExternalContext ec;

    public viewStatementManagedBean() {
    }

    public String getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(String statementDate) {
        this.statementDate = statementDate;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public String getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(String accountDetails) {
        this.accountDetails = accountDetails;
    }

    public Long getCustomerBasicId() {
        return customerBasicId;
    }

    public void setCustomerBasicId(Long customerBasicId) {
        this.customerBasicId = customerBasicId;
    }
    
    public void viewStatement() throws ClassNotFoundException, IOException, JRException, SQLException{
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        BankAccount bankAccount = customerBasic.getBankAccount().get(0);
        
        Connection connection;
        Map parameterMap = new HashMap();

        FacesContext ctx = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) ctx
                .getExternalContext().getResponse();

        InputStream reportStream = ctx.getExternalContext()
                .getResourceAsStream("/E-Statements/myStatement.jasper");

        if(reportStream == null)
            System.err.println("********* INputstream is null");
        else
            System.err.println("********* INputstream is not null");
        
        
        ServletOutputStream servletOutputStream = response.getOutputStream();
        Class.forName("com.mysql.jdbc.Driver");
        connection = retailBankingSystemDataSource.getConnection();

        ctx.responseComplete();
        response.setContentType("application/pdf");
        
        Map parameters = new HashMap();
        parameters.put("BankAccountId", bankAccount.getBankAccountId());

        JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream, parameterMap, connection);

        connection.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }

    public List<Statement> getStatement() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        String customerIC = customerBasic.getCustomerIdentificationNum();

        List<Statement> statement = statementSessionBeanLocal.retrieveStatementByCusIC(customerIC.toUpperCase());

        if (statement.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your identification is invalid", "Failed!"));
        }

        return statement;
    }
}
