package managedbean.deposit.employee;

import ejb.deposit.entity.BankAccount;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.Statement;
import ejb.deposit.session.BankAccountSessionBeanLocal;
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
import java.util.ArrayList;
import java.util.Calendar;

@Named(value = "employeeViewStatementDoneManagedBean")
@RequestScoped

public class EmployeeViewStatementDoneManagedBean {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private StatementSessionBeanLocal statementSessionBeanLocal;

    @Resource(name = "retailBankingSystemDataSource")
    private DataSource retailBankingSystemDataSource;

    private String statementDate;
    private String statementType;
    private String accountDetails;
    private Long bankAccountId;

    private String customerIdentificationNum;

    private ExternalContext ec;

    public EmployeeViewStatementDoneManagedBean() {
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

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public void viewStatement() throws ClassNotFoundException, IOException, JRException, SQLException {
        
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountById(bankAccountId);

        Connection connection;
        Map parameterMap = new HashMap();

        FacesContext ctx = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) ctx
                .getExternalContext().getResponse();

        InputStream reportStream = ctx.getExternalContext()
                .getResourceAsStream("/E-Statements/bankStatement.jasper");

        if (reportStream == null) {
            System.err.println("********* INputstream is null");
        } else {
            System.err.println("********* INputstream is not null");
        }

        ServletOutputStream servletOutputStream = response.getOutputStream();
        Class.forName("com.mysql.jdbc.Driver");
        connection = retailBankingSystemDataSource.getConnection();

        ctx.responseComplete();
        response.setContentType("application/pdf");
        
        Calendar cal = Calendar.getInstance();
        Long startTimeLong = 1474615723704l;
        Long endTimeLong = 1474616924103l;
        
        Map parameters = new HashMap();
        parameters.put("bankAccountId", bankAccount.getBankAccountId());

        System.err.println("********** Start Jasper");
        
        JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream, parameterMap, connection);

        System.err.println("********** End Jasper");
        
        connection.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }

    public List<Statement> getStatement() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();

        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);
        List<BankAccount> bankAccounts = customerBasic.getBankAccount();
        List<Statement> statement = new ArrayList();

        for (int i = 0; i < bankAccounts.size(); i++) {
            statement = statementSessionBeanLocal.retrieveStatementByAccNum(bankAccounts.get(i).getBankAccountNum());

            if (statement.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your account number is invalid", "Failed!"));
            } else {
                return statement;
            }
        }

        return statement;
    }
}
