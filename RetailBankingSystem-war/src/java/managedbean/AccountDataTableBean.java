package managedbean;

import entity.BankAccount;
import entity.CustomerBasic;
import entity.Payee;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import session.stateless.BankAccountSessionLocal;

@Named(value = "accountDataTableBean")
@RequestScoped

public class AccountDataTableBean implements Serializable{

    @EJB
    private BankAccountSessionLocal bankAccountSessionLocal;

    private ExternalContext ec;

    public AccountDataTableBean() {
    }

    public List<BankAccount> getBankAccount() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        String customerId = customerBasic.getCustomerIdentificationNum();

        List<BankAccount> bankAccount = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerId.toUpperCase());

        if (bankAccount.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your identification is invalid", "Failed!"));
        }

        return bankAccount;
    }

}
