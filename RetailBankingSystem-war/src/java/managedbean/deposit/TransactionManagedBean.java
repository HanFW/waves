package managedbean.deposit;

import ejb.deposit.entity.AccTransaction;
import java.io.IOException;
import javax.ejb.EJB;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.ExternalContext;
import ejb.deposit.session.TransactionSessionBeanLocal;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "transactionManagedBean")
@RequestScoped

public class TransactionManagedBean implements Serializable{
    @EJB
    private TransactionSessionBeanLocal transactionSessionLocal;
    
    private ExternalContext ec;
    private String bankAccountNum;
    
    public TransactionManagedBean() {
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public List<AccTransaction> getAccTransaction() throws IOException
    {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<AccTransaction> accTransaction = transactionSessionLocal.retrieveAccTransactionByBankNum(bankAccountNum);
        
        return accTransaction;
    }
}
