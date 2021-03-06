package managedbean.deposit.employee;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeViewAccountDoneManagedBean")
@RequestScoped

public class EmployeeViewAccountDoneManagedBean {

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private ExternalContext ec;

    private String customerIdentificationNum;

    public EmployeeViewAccountDoneManagedBean() {
    }

    @PostConstruct
    public void init() {
        customerIdentificationNum = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("customerIdentificationNum").toString();
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public List<BankAccount> getBankAccount() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/EmployeeViewAccountDoneManagedBean: getBankAccount() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        List<BankAccount> bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerIdentificationNum.toUpperCase());

        if (bankAccount.isEmpty()) {
            loggingSessionBeanLocal.createNewLogging("employee", null, "view account", "failed", "invalid customer");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failed! Your identification is invalid", "Failed!"));
        } else {
            loggingSessionBeanLocal.createNewLogging("employee", null, "view account", "successful", null);
        }

        return bankAccount;
    }
}
