package managedbean.deposit;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.Statement;
import ejb.deposit.session.StatementSessionBeanLocal;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@Named(value = "viewStatementDoneManagedBean")
@RequestScoped

public class ViewStatementDoneManagedBean {
    @EJB
    private StatementSessionBeanLocal statementSessionBeanLocal;

    @Resource(name = "retailBankingSystemDataSource")
    private DataSource retailBankingSystemDataSource;
    
    private Long statementId;
    private Long bankAccountId;
    
    public ViewStatementDoneManagedBean() {
    }

    public Long getStatementId() {
        return statementId;
    }

    public void setStatementId(Long statementId) {
        this.statementId = statementId;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
    
    public void viewStatement() throws ClassNotFoundException, IOException, JRException, SQLException {
        System.out.println("=");
        System.out.println("====== deposit/ViewStatementManagedBean: viewStatement() ======");

        System.out.println(statementId);
        Connection connection;

        FacesContext ctx = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) ctx
                .getExternalContext().getResponse();

        InputStream reportStream = ctx.getExternalContext()
                .getResourceAsStream("/E-Statements/myBankStatement.jasper");

//        if (reportStream == null) {
//            System.err.println("********* INputstream is null");
//        } else {
//            System.err.println("********* INputstream is not null");
//        }

        ServletOutputStream servletOutputStream = response.getOutputStream();
        Class.forName("com.mysql.jdbc.Driver");
        connection = retailBankingSystemDataSource.getConnection();

        ctx.responseComplete();
        response.setContentType("application/pdf");

        Statement statement = statementSessionBeanLocal.retrieveStatementById(statementId);
        BankAccount bankAccount = statement.getBankAccount();
        

        Calendar cal = Calendar.getInstance();
        Long startTimeLong = statement.getStartTime();
        Long endTimeLong = statement.getEndTime();

        Map parameters = new HashMap();
        parameters.put("bankAccountId", bankAccount.getBankAccountId());
        parameters.put("startTime", startTimeLong);
        parameters.put("endTime", endTimeLong);

        JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream, parameters, connection);

        connection.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }

    
}
