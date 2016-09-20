package managedbean.deposit;

import ejb.deposit.entity.BankAccount;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.Payee;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import ejb.deposit.session.BankAccountSessionBeanLocal;

@Named(value = "accountDataTableBean")
@RequestScoped

public class AccountDataTableBean implements Serializable{

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    private ExternalContext ec;

    public AccountDataTableBean() {
    }

    public List<BankAccount> getBankAccount() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        String customerIC = customerBasic.getCustomerIdentificationNum();

        List<BankAccount> bankAccount = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerIC.toUpperCase());

        if (bankAccount.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your identification is invalid", "Failed!"));
        }

        return bankAccount;
    }

}
