package managedbean.deposit.employee;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeViewTransactionDoneManagedBean")
@RequestScoped

public class EmployeeViewTransactionDoneManagedBean implements Serializable{
    
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;
    

    private ExternalContext ec;
    private String bankAccountNum;
    private String customerIdentificationNum;
    
    public EmployeeViewTransactionDoneManagedBean() {
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }
    
    public List<BankAccount> getBankAccount() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/EmployeeViewTransactionDoneManagedBean: getBankAccount() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();

        List<BankAccount> bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerIdentificationNum.toUpperCase());

        if (bankAccount.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your identification is invalid", "Failed!"));
        }

        return bankAccount;
    }
}
