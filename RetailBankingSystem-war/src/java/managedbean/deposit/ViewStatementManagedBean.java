package managedbean.deposit;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.Statement;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;

@Named(value = "viewStatementManagedBean")
@SessionScoped

public class ViewStatementManagedBean implements Serializable {

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private String statementDate;
    private String statementType;
    private String accountDetails;
    private String bankAccountNum;

    private String customerIdentificationNum;

    private ExternalContext ec;

    public ViewStatementManagedBean() {
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

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public List<Statement> getStatement() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
        List<Statement> statement = bankAccount.getStatement();
        
        if (statement.isEmpty()) {
            loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "view statement", "falied", "invalid account number");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your account number is invalid", "Failed!"));
        } else {
            loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "view statement", "successful", null);
        }

        return statement;
    }
}
